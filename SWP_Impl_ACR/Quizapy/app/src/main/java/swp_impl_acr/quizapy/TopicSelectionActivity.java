package swp_impl_acr.quizapy;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.Button;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import swp_impl_acr.quizapy.Cursor.Cursor;
import swp_impl_acr.quizapy.Database.DataSource.StuffDataSource;
import swp_impl_acr.quizapy.Database.DataSource.TopicDataSource;
import swp_impl_acr.quizapy.Database.Entity.Topic;
import swp_impl_acr.quizapy.Helper.CollectionUtils;
import swp_impl_acr.quizapy.Helper.RespiratoryTrainer;
import swp_impl_acr.quizapy.RespiratoryTrainerSimulation.Buttons;
import swp_impl_acr.quizapy.RespiratoryTrainerSimulation.EventListenerInterface;

/**
 * choosing a topic (out of up to 9 randomly selected from available topics)
 */
public class TopicSelectionActivity extends AppCompatActivity implements EventListenerInterface {
    private ConstraintLayout layout;
    private List<Button> topicButtons;
    private List<Topic> topics;
    private SessionStorage sessionStorage;
    private Cursor cursor;
    private ObjectAnimator animator;
    private TopicDataSource topicDataSource;
    private StuffDataSource stuffDataSource;


    private static final int LEFT = 0;
    private static final int MIDDLE = 1;
    private static final int RIGHT = 2;

    private int currentPosition = LEFT;

    private boolean stopAnimation = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout = (ConstraintLayout) View.inflate(this, R.layout.activity_topic_selection, null);
        setContentView(layout);

        if (!RespiratoryTrainer.isConnected()) {
            getSimulatorButtons();
        }

        topicButtons = new ArrayList<>();

        topicButtons.add((Button)(findViewById(R.id.topic_button_1)));
        topicButtons.add((Button)(findViewById(R.id.topic_button_2)));
        topicButtons.add((Button)(findViewById(R.id.topic_button_3)));
        topicButtons.add((Button)(findViewById(R.id.topic_button_4)));
        topicButtons.add((Button)(findViewById(R.id.topic_button_5)));
        topicButtons.add((Button)(findViewById(R.id.topic_button_6)));
        topicButtons.add((Button)(findViewById(R.id.topic_button_7)));
        topicButtons.add((Button)(findViewById(R.id.topic_button_8)));
        topicButtons.add((Button)(findViewById(R.id.topic_button_9)));

        sessionStorage = SessionStorage.getInstance();
        try {
            stuffDataSource = new StuffDataSource();
            topicDataSource = new TopicDataSource();
            topics = topicDataSource.getChoosableTopics(sessionStorage.getPoints(), Integer.parseInt(stuffDataSource.getValue("questions_in_sequence").toString()));
            topics = CollectionUtils.generateRandomList(topics, 9);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int count = topics.size();
        int i = 0;
        for(Button button:topicButtons){
            button.setBackgroundColor(Color.LTGRAY);
            if(i<count){
                button.setText(topics.get(i).getName());
            } else {
                button.setVisibility(View.INVISIBLE);
            }
            i++;
        }

        cursor = new Cursor(this, 0,0);
        layout.addView(cursor);
        cursor.setVisibility(View.INVISIBLE);

        layout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                        for(Button button:topicButtons){
                            if(button.getVisibility()==View.INVISIBLE){
                                button.setWidth(topicButtons.get(0).getWidth());
                                button.setHeight(topicButtons.get(0).getHeight());
                            }
                        }

                        cursor.moveCursor(topicButtons.get(0).getLeft(),topicButtons.get(0).getTop());
                        //cursor.setVisibility(View.VISIBLE);
                        startCursorAnimation();

                    }

                }
        );


    }

    /**
     * adds Buttons to simulate the Respiratory Trainer to the bottom of the screen
     */
    private void getSimulatorButtons() {
        Buttons buttons = new Buttons(this, null, Buttons.BUTTON_HOLD_BREATH);
        layout.addView(buttons);

        ConstraintSet set = new ConstraintSet();
        set.clone(layout);

        set.connect(buttons.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
        set.connect(buttons.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
        set.connect(buttons.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);

        set.applyTo(layout);

        buttons.addEventListener(this);

    }

    /**
     * sets Cursor animation and starts it
     */
    private void startCursorAnimation() {
        animator = ObjectAnimator.ofFloat(cursor, "y", topicButtons.get(0).getTop()+25,topicButtons.get(2).getBottom()-25);
        animator.setDuration(2000);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentY = (Float) animation.getAnimatedValue();

                for(Button button:topicButtons){
                    if(currentY >= button.getTop()
                            && currentY <= button.getBottom()
                            && cursor.getX()+50 >= button.getLeft()
                            && cursor.getX()+50 <= button.getRight()) {
                        button.setBackgroundColor(Color.CYAN);
                        if(button.getVisibility() == View.INVISIBLE){
                            cursor.moveCursor(topicButtons.get(0).getLeft(),topicButtons.get(0).getTop());
                            currentPosition=RIGHT;
                            animator.cancel();
                        }
                    } else {
                        button.setBackgroundColor(Color.LTGRAY);
                    }
                }
            }
        });
        animator.addListener(new ValueAnimator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(!stopAnimation){
                    switch(currentPosition){
                        case LEFT:
                            cursor.moveCursor(topicButtons.get(3).getLeft(),topicButtons.get(3).getTop());
                            currentPosition=MIDDLE;
                            break;
                        case MIDDLE:
                            cursor.moveCursor(topicButtons.get(6).getLeft(),topicButtons.get(6).getTop());
                            currentPosition=RIGHT;
                            break;
                        case RIGHT:
                            cursor.moveCursor(topicButtons.get(0).getLeft(),topicButtons.get(0).getTop());
                            currentPosition=LEFT;
                            break;
                    }
                    startCursorAnimation();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        animator.start();
    }

    /**
     * save topic in sessionStorage
     */
    private void chooseTopic(){
        int color = Color.LTGRAY;
        int i = 0;
        for(Button button:topicButtons) {
            Drawable background = button.getBackground();
            if (background instanceof ColorDrawable) {
                color = ((ColorDrawable) background).getColor();
            }
            if(color == Color.CYAN){
                sessionStorage.setTopic(topics.get(i));
            }
            i++;
        }
    }

    @Override
    public void onBreathInStart() {

    }

    @Override
    public void onBreathInStop() {

    }

    @Override
    public void onBreathOutStart() {

    }

    @Override
    public void onBreathOutStop() {

    }

    @Override
    public void onHoldBreathStart() {
        stopAnimation=true;
        chooseTopic();
        next();
    }

    @Override
    public void onHoldBreathStop() {

    }

    @Override
    public void onBreathingRateChange() {

    }


    @Override
    public void graduallyBreathInStart(){

    }
    @Override
    public void graduallyBreathInStop(){

    }
    @Override
    public void graduallyBreathOutStart(){

    }
    @Override
    public void graduallyBreathOutStop(){

    }

    /**
     * starts next activity
     */
    public void next(){
        int j=3;
        if(sessionStorage.getPoints()<=3){
            j=sessionStorage.getPoints();
        }
        int gradCount = 0;
        for(int i = 1; i<=j; i++){
            try {
                int count = topicDataSource.getAllUnansweredQuestionsByDifficultyCount(sessionStorage.getTopic().getId(),i);
                if(count>=Integer.parseInt(stuffDataSource.getValue("questions_in_sequence"))){
                    gradCount++;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        Intent in;
        if(gradCount>=2){
            in = new Intent(getBaseContext(), ComplexityScaleActivity.class);
        } else {
            in = new Intent(getBaseContext(), QuestionActivity.class);
        }
        startActivity(in);
        finish();
    }



    public void test(){}
}

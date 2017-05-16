package swp_impl_acr.quizapy;

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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import swp_impl_acr.quizapy.Cursor.Cursor;
import swp_impl_acr.quizapy.Database.DataSource.TopicDataSource;
import swp_impl_acr.quizapy.Database.Entity.Answer;
import swp_impl_acr.quizapy.Database.Entity.Question;
import swp_impl_acr.quizapy.Helper.CollectionUtils;
import swp_impl_acr.quizapy.Helper.RespiratoryTrainer;
import swp_impl_acr.quizapy.RespiratoryTrainerSimulation.Buttons;
import swp_impl_acr.quizapy.RespiratoryTrainerSimulation.EventListenerInterface;

/**
 * activity where the user answers the questions one at a time
 */
public class QuestionActivity extends AppCompatActivity implements EventListenerInterface{
    private ConstraintLayout layout;
    private List<Button> answerButtons;
    private Cursor cursor = null;
    private ObjectAnimator animator;
    private TextView text;
    private List<Question> questions;
    private GameConfig gameConfig;
    private List<Answer> answers;

    /**
     * constant for selectMode: no user action happened yet
     */
    private static final int NOTHING = 0;
    /**
     * constant for selectMode: user started to choose answer with breathing in and locks the answer in with breathing out
     */
    private static final int IN_OUT = 1;
    /**
     * constant for selectMode: user started to choose answer with breathing out and locks the answer in with breathing in
     */
    private static final int OUT_IN = 2;

    /**
     * value represents if the user started choosing the answer with breathing in or breathing out
     */
    private int selectMode = NOTHING;
    private TextView text2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout = (ConstraintLayout) View.inflate(this, R.layout.activity_question, null);
        setContentView(layout);

        text2 = (TextView) findViewById(R.id.text);
        String newString;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                newString= null;
            } else {
                newString= extras.getString("GRAD");
            }
        } else {
            newString= (String) savedInstanceState.getSerializable("GRAD");
        }
        text2.setText(newString);









        answerButtons = new ArrayList<>();

        answerButtons.add((Button)(findViewById(R.id.button)));
        answerButtons.add((Button)(findViewById(R.id.button2)));
        answerButtons.add((Button)(findViewById(R.id.button3)));
        answerButtons.add((Button)(findViewById(R.id.button4)));

        text = (TextView)(findViewById(R.id.questionText));

        gameConfig = GameConfig.getInstance();
        try {
            TopicDataSource topicDataSource = new TopicDataSource();
            questions= topicDataSource.getAllUnansweredQuestionsByDifficulty(gameConfig.getTopic().getId(), gameConfig.getDifficulty());
        } catch (Exception e) {
            e.printStackTrace();
        }
        displayQuestion();


        if (!RespiratoryTrainer.isConnected()) {
            getSimulatorButtons();
        }

        cursor = new Cursor(this, 0,0);
        layout.addView(cursor);
        cursor.setVisibility(View.INVISIBLE);

        layout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    /**
                     * sets answerbuttons to the same width and places cursor above the topmost button
                     */
                    @Override
                    public void onGlobalLayout() {
                        layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                        int minLeft = answerButtons.get(0).getLeft();
                        int maxRight = answerButtons.get(0).getRight();

                        for(Button button:answerButtons){
                            if(button.getLeft()<minLeft){
                                minLeft=button.getLeft();
                            }
                            if(button.getRight()>maxRight){
                                maxRight=button.getRight();
                            }
                        }

                        for(Button button:answerButtons){
                            button.setWidth(maxRight-minLeft);
                        }

                        cursor.moveCursor(minLeft-50,answerButtons.get(0).getTop());
                        cursor.setVisibility(View.VISIBLE);

                    }

                }
        );

    }

    /**
     * displays the current question and its answers
     */
    private void displayQuestion() {
        text.setText(questions.get(0).getName());
        answers = questions.get(0).getAnswers();
        answers = CollectionUtils.generateRandomList(answers, answers.size());

        int i = 1;
        for(Button button:answerButtons){
            button.setBackgroundColor(Color.LTGRAY);
            button.setText(answers.get(i-1).getName());
            i++;
        }
    }

    /**
     * adds Buttons to simulate the Respiratory Trainer to the bottom of the screen
     */
    private void getSimulatorButtons() {
        Buttons buttons = new Buttons(this, null);
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
     * either starts cursoranimation or saves the current selected answer and moves to the nextaction depending on the selected mode
     */
    @Override
    public void onBreathInStart() {
        switch(selectMode){
            case NOTHING:
                selectMode=IN_OUT;
            case IN_OUT:
                startCursorAnimation();
                break;
            case OUT_IN:
                saveSelectedAnswer();
                next();
                break;
            default:
                break;
        }
    }

    /**
     * stops the animation
     */
    @Override
    public void onBreathInStop() {
        animator.cancel();
    }

    /**
     * either starts the animation or saves and moves to next action depending on the mode
     */
    @Override
    public void onBreathOutStart() {
        switch(selectMode){
            case NOTHING:
                selectMode=OUT_IN;
            case OUT_IN:
                startCursorAnimation();
                break;
            case IN_OUT:
                saveSelectedAnswer();
                next();
                break;
            default:
                break;
        }
    }

    /**
     * stops the animation
     */
    @Override
    public void onBreathOutStop() {
        animator.cancel();

    }

    @Override
    public void onHoldBreathStart() {

    }

    @Override
    public void onHoldBreathStop() {

    }

    /**
     * sets Cursor animation and starts it
     */
    private void startCursorAnimation() {
        animator = ObjectAnimator.ofFloat(cursor, "y", answerButtons.get(0).getTop()+25,answerButtons.get(3).getBottom()-25);
        animator.setDuration(5000);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentY = (Float) animation.getAnimatedValue();

                for(Button button:answerButtons){
                    if(currentY >= button.getTop() && currentY <= button.getBottom()) {
                        button.setBackgroundColor(Color.CYAN);
                    } else {
                        button.setBackgroundColor(Color.LTGRAY);
                    }
                }
            }
        });
        animator.start();
    }

    /**
     * if the questionList is empty: starts the next activity (QuestionListActivity)
     * otherwise displays the next question and resets the cursor;
     */
    private void next() {
        questions.remove(0);
        if(questions.size()==0){
            Intent in = new Intent(getBaseContext(), QuestionListActivity.class);
            startActivity(in);
            finish();
        } else {
            displayQuestion();
            cursor.moveCursor(answerButtons.get(0).getLeft()-50,answerButtons.get(0).getTop());
            selectMode = NOTHING;
        }
    }

    /**
     * adds the selected answer to the AnswerList in the GameConfig instance
     */
    private void saveSelectedAnswer() {
        int color = Color.LTGRAY;
        int i = 0;
        for(Button button:answerButtons) {
            Drawable background = button.getBackground();
            if (background instanceof ColorDrawable) {
                color = ((ColorDrawable) background).getColor();
            }
            if(color == Color.CYAN){
                gameConfig.addAnswer(answers.get(i));
            }
            i++;
        }
    }
}

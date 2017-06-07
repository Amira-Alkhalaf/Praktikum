package swp_impl_acr.quizapy;

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
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import swp_impl_acr.quizapy.Database.DataSource.OptionDataSource;
import swp_impl_acr.quizapy.Database.DataSource.TopicDataSource;
import swp_impl_acr.quizapy.Database.Entity.Answer;
import swp_impl_acr.quizapy.Helper.RespiratoryTrainer;
import swp_impl_acr.quizapy.RespiratoryTrainerSimulation.Buttons;
import swp_impl_acr.quizapy.RespiratoryTrainerSimulation.EventListenerInterface;

/**
 * activity where the user answers the questions one at a time
 */
public abstract class QuestionActivity extends AppCompatActivity implements EventListenerInterface{
    protected ConstraintLayout layout;
    protected List<Button> answerButtons;
    protected TextView questionText;
    protected SessionStorage sessionStorage;
    protected List<Answer> answers;
    protected OptionDataSource optionDataSource;
    protected int simButtons;
    protected TextView difficulty;
    protected TextView mode;
    protected Buttons controlButtons;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout = (ConstraintLayout) View.inflate(this, R.layout.activity_question, null);
        setContentView(layout);

        difficulty = (TextView) findViewById(R.id.text);
        mode = (TextView) findViewById(R.id.chosenMode);

        answerButtons = new ArrayList<>();
        answerButtons.add((Button)findViewById(R.id.button));
        answerButtons.add((Button)findViewById(R.id.button2));
        answerButtons.add((Button)findViewById(R.id.button3));
        answerButtons.add((Button)findViewById(R.id.button4));

        questionText = (TextView)(findViewById(R.id.questionText));

        sessionStorage = SessionStorage.getInstance();
        try {
            optionDataSource = new OptionDataSource();
            TopicDataSource topicDataSource = new TopicDataSource();
        } catch (Exception e) {
            e.printStackTrace();
        }

        difficulty.setText(gradToString(sessionStorage.getDifficulty()));
        TextView chosenTopic = (TextView) findViewById(R.id.chosen_topic);
        chosenTopic.setText(sessionStorage.getTopic().getName());
        displayQuestion();


        if (!RespiratoryTrainer.isConnected()) {
            getSimulatorButtons();
        }

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
                            if(button.getLeft()<minLeft && button.isEnabled()){
                                minLeft=button.getLeft();
                            }
                            if(button.getRight()>maxRight && button.isEnabled()){
                                maxRight=button.getRight();
                            }
                        }

                        for(Button button:answerButtons){
                            button.setWidth(maxRight-minLeft);
                        }

                        setCursor(minLeft);

                    }

                }
        );

    }
    protected void setCursor(int position){}

    /**
     * displays the current question and its answers
     */
    protected void displayQuestion() {
        questionText.setText(sessionStorage.getQuestions().get(0).getName());
        getAnswers();

        int i = 1;
        for(Button button:answerButtons){
            if(i<= answers.size()) {
                button.setTag(i-1);
                button.setBackgroundColor(Color.LTGRAY);
                button.setText(answers.get(i - 1).getName());
                button.setEnabled(true);
            } else {
                button.setTag(i-1);
                button.setEnabled(false);
                button.setVisibility(View.INVISIBLE);
            }
            i++;
        }
    }

    abstract void getAnswers();

    /**
     * adds Buttons to simulate the Respiratory Trainer to the bottom of the screen
     */
    void getSimulatorButtons(){
        controlButtons = new Buttons(this, null, simButtons);
        controlButtons.setId(1111);
        layout.addView(controlButtons);

        ConstraintSet set = new ConstraintSet();
        set.clone(layout);

        set.connect(controlButtons.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
        set.connect(controlButtons.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
        set.connect(controlButtons.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);

        set.applyTo(layout);

        controlButtons.addEventListener(this);
    }


    /**
     * if the questionList is empty: starts the next activity (QuestionListActivity)
     * otherwise displays the next question and resets the cursor;
     */
    protected void next() {
        sessionStorage.getQuestions().remove(0);
        Intent in = null;
        if(sessionStorage.getQuestions().size()==0){
            in = new Intent(getBaseContext(), QuestionListActivity.class);
        } else if(sessionStorage.getMode()==SessionStorage.MODE_5) {
            int r2 = new Random().nextInt(5 - 1) + 1;
            switch (r2) {
                case 1:
                    in = new Intent(getBaseContext(), Mode1.class);
                    break;
                case 2:
                    in = new Intent(getBaseContext(), Mode2.class);
                    break;
                case 3:
                    in = new Intent(getBaseContext(), Mode3.class);
                    break;
                case 4:
                    in = new Intent(getBaseContext(), Mode4.class);
                    break;
                default:
                    break;
            }
        } else if(sessionStorage.getMode()==SessionStorage.MODE_3){
            in = new Intent(getBaseContext(), Mode3.class);
        }else if(sessionStorage.getMode()==SessionStorage.MODE_2){
            in = new Intent(getBaseContext(), Mode2.class);
        }else if(sessionStorage.getMode()==SessionStorage.MODE_4){
            in = new Intent(getBaseContext(), Mode4.class);
        }else {
            in = new Intent(getBaseContext(), Mode1.class);
        }


        in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(in);
        finish();
        overridePendingTransition(0, 0);
    }

    /**
     * adds the selected answer to the AnswerList in the SessionStorage instance
     */
    protected void saveSelectedAnswer() {
        int color = Color.LTGRAY;
        int i = 0;
        for(Button button:answerButtons) {
            Drawable background = button.getBackground();
            if (background instanceof ColorDrawable) {
                color = ((ColorDrawable) background).getColor();
            }
            if(color == Color.CYAN){
                sessionStorage.addAnswer(answers.get(i));
                break;
            }
            i++;
        }
    }

    /**
     * returns string corresponding to set difficulty
     * @param grad
     * @return
     */
    protected String gradToString(int grad){
        switch(grad){
            case 1:
                return "low";
            case 2:
                return "med";
            case 3:
                return "high";
            default:
                return "error";
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

    }

    @Override
    public void onHoldBreathStop() {

    }

    @Override
    public void onBreathingRateChange(int progress) {

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
    @Override
    public void onRepeatedlyBreathINandOUTstart(){

    }
    @Override
    public void onRepeatedlyBreathINandOUTstop(){

    }

}

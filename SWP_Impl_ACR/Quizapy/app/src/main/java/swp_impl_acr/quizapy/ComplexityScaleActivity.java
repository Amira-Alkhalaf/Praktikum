package swp_impl_acr.quizapy;

/**
 * Created by Amira on 5/16/2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.List;
import java.util.Random;

import swp_impl_acr.quizapy.Database.DataSource.StuffDataSource;
import swp_impl_acr.quizapy.Database.DataSource.TopicDataSource;
import swp_impl_acr.quizapy.Database.Entity.Question;
import swp_impl_acr.quizapy.Helper.CollectionUtils;
import swp_impl_acr.quizapy.Helper.RespiratoryTrainer;
import swp_impl_acr.quizapy.RespiratoryTrainerSimulation.Buttons;
import swp_impl_acr.quizapy.RespiratoryTrainerSimulation.EventListenerInterface;

/**
 * Created by Amira on 5/16/2017.
 */

public class ComplexityScaleActivity extends AppCompatActivity implements EventListenerInterface {

    private ImageView scaleImage;
    private TextView currentGrad;
    private ConstraintLayout layout;
    private float rotation = 0;

    private static final int LOW = 1;
    private static final int MED = 2;
    private static final int HIGH = 3;

    private int grad = LOW;

    private TopicDataSource topicDataSource;
    private SessionStorage sessionStorage;
    private StuffDataSource stuffDataSource;

    private Toast gradNotAvailableToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout =(ConstraintLayout) View.inflate(this, R.layout.activity_complexityscale, null);
        setContentView(layout);

        gradNotAvailableToast = Toast.makeText(ComplexityScaleActivity.this, "Die gewählte Schwierigkeit ist nicht verfügbar. Bitte eine andere wählen.", Toast.LENGTH_LONG);

        scaleImage = (ImageView) findViewById(R.id.image);
        currentGrad = (TextView) findViewById(R.id.console);
        currentGrad.setText(gradToString());



        if (!RespiratoryTrainer.isConnected()) {
            getSimulatorButtons();
        }
        try {
            topicDataSource = new TopicDataSource();
            stuffDataSource = new StuffDataSource();
        } catch (Exception e) {
            e.printStackTrace();
        }
        sessionStorage = SessionStorage.getInstance();
        TextView chosenTopic = (TextView) findViewById(R.id.chosenTopic);
        chosenTopic.setText(sessionStorage.getTopic().getName());
    }



    /**
     * adds Buttons to simulate the Respiratory Trainer to the top of the screen
     */
    private void getSimulatorButtons() {       // mode1
        Buttons buttons = new Buttons(this, null, Buttons.BUTTON_BREATH_IN | Buttons.BUTTON_BREATH_OUT);

        layout.addView(buttons);

        buttons.addEventListener(this);
    }

    /**
     * returns true if the desired difficulty is available
     * @return
     */
    private boolean isGradChoosable(){
        int count = 0;
        try {
            count = topicDataSource.getAllUnansweredQuestionsByDifficultyCount(sessionStorage.getTopic().getId(), sessionStorage.getDifficulty());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(count >= Integer.parseInt(stuffDataSource.getValue("questions_in_sequence"))){
            return true;
        } else {
            return false;
        }
    }

    /**
     * returns string of grad
     * @return
     */
    private String gradToString(){
        switch(grad){
            case LOW:
                return "low";
            case MED:
                return "med";
            case HIGH:
                return "high";
            default:
                return "error";
        }
    }

    /**
     * onclick changes grad
     */
    @Override
    public void onBreathInStart() {
        rotation = rotation - 120;
        scaleImage.setRotation(rotation);
        switch(grad){
            case LOW:
                grad=MED;
                break;
            case MED:
                grad=HIGH;
                break;
            case HIGH:
                grad=LOW;
                break;
        }
        currentGrad.setText(gradToString());
        scaleImage.invalidate();
    }

    @Override
    public void onBreathInStop() {

    }

    /**
     * onclick sets grad and starts question activity
     * if difficulty is available for chosen topic
     */
    @Override
    public void onBreathOutStart() {
        sessionStorage.setDifficulty(grad);

        if(isGradChoosable() && sessionStorage.getPoints() >= grad){
            gradNotAvailableToast.cancel();
            try {
                List<Question> questions;
                questions = topicDataSource.getAllUnansweredQuestionsByDifficulty(sessionStorage.getTopic().getId(), sessionStorage.getDifficulty());
                questions = CollectionUtils.generateRandomList(questions, Integer.parseInt(stuffDataSource.getValue("questions_in_sequence")));
                sessionStorage.setQuestions(questions);
            } catch (Exception e) {
                e.printStackTrace();
            }
            setMode();
        } else {
            gradNotAvailableToast.show();
            // todo: Shake screen or disable a diffiulty if its not available or something
        }
    }

    private void setMode() {
        Random rand = new Random();
        int r1 = rand.nextInt(101-1) + 1;
        int val = 0;
        Intent b2 = null;
        if(r1 <= (val += Integer.parseInt(stuffDataSource.getValue("frequency_mode_1").toString()))){
            sessionStorage.setMode(SessionStorage.MODE_1);
            Toast.makeText(this, "MODUS 1", Toast.LENGTH_SHORT).show();
            b2 = new Intent(ComplexityScaleActivity.this, Mode1.class);
        } else if(r1 <= (val += Integer.parseInt(stuffDataSource.getValue("frequency_mode_2").toString()))){
            sessionStorage.setMode(SessionStorage.MODE_2);
            Toast.makeText(this, "MODUS 2", Toast.LENGTH_SHORT).show();
            b2 = new Intent(ComplexityScaleActivity.this, Mode2.class);
        } else if(r1 <= (val += Integer.parseInt(stuffDataSource.getValue("frequency_mode_3").toString()))) {
            sessionStorage.setMode(SessionStorage.MODE_3);
            Toast.makeText(this, "MODUS 3", Toast.LENGTH_SHORT).show();
            b2 = new Intent(ComplexityScaleActivity.this, Mode3.class);
        } else if(r1 <= (val += Integer.parseInt(stuffDataSource.getValue("frequency_mode_4").toString()))){
            sessionStorage.setMode(SessionStorage.MODE_4);
            Toast.makeText(this, "MODUS 4", Toast.LENGTH_SHORT).show();
            b2 = new Intent(ComplexityScaleActivity.this, Mode4.class);
        } else {
            sessionStorage.setMode(SessionStorage.MODE_5);
            Toast.makeText(this, "MODUS 5", Toast.LENGTH_SHORT).show();
            int r2 = rand.nextInt(5-1) +1;
            switch(r2){
                case 1: b2 = new Intent(ComplexityScaleActivity.this, Mode1.class);
                    break;
                case 2: b2 = new Intent(ComplexityScaleActivity.this, Mode2.class);
                    break;
                case 3: b2 = new Intent(ComplexityScaleActivity.this, Mode3.class);
                    break;
                case 4: b2 = new Intent(ComplexityScaleActivity.this, Mode4.class);
                    break;
                default: break;
            }
        }
        startActivity(b2);
        finish();

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
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

import swp_impl_acr.quizapy.Database.DataSource.TopicDataSource;
import swp_impl_acr.quizapy.Helper.RespiratoryTrainer;
import swp_impl_acr.quizapy.RespiratoryTrainerSimulation.Buttons;
import swp_impl_acr.quizapy.RespiratoryTrainerSimulation.EventListenerInterface;

/**
 * Created by Amira on 5/16/2017.
 */

public class ComplexityScaleActivity extends AppCompatActivity implements EventListenerInterface{

    private ImageView scaleImage;
    private TextView currentGrad;
    private ConstraintLayout layout;
    private float rotation = 0;

    private static final int LOW = 1;
    private static final int MED = 2;
    private static final int HIGH = 3;

    private int grad = LOW;

    private TopicDataSource topicDataSource;
    private GameConfig gameConfig;

    private Toast gradNotAvailableToast;
    private AvailablePoints points = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout =(ConstraintLayout) View.inflate(this, R.layout.activity_complexityscale, null);
        setContentView(layout);

        gradNotAvailableToast = Toast.makeText(ComplexityScaleActivity.this, "Die gewählte Schwierigkeit ist nicht verfügbar. Bitte eine andere wählen.", Toast.LENGTH_LONG);

        points = (AvailablePoints) getApplicationContext();
        scaleImage = (ImageView) findViewById(R.id.image);
        currentGrad = (TextView) findViewById(R.id.console);
        currentGrad.setText(gradToString());



        if (!RespiratoryTrainer.isConnected()) {
            getSimulatorButtons();
        }
        try {
            topicDataSource = new TopicDataSource();
        } catch (Exception e) {
            e.printStackTrace();
        }
        gameConfig = GameConfig.getInstance();
        TextView chosenTopic = (TextView) findViewById(R.id.chosenTopic);
        chosenTopic.setText(gameConfig.getTopic().getName());
    }



    /**
     * adds Buttons to simulate the Respiratory Trainer to the top of the screen
     */
    private void getSimulatorButtons() {       // mode1
        Buttons buttons = new Buttons(this, null);

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
            count = topicDataSource.getAllUnansweredQuestionsByDifficultyCount(gameConfig.getTopic().getId(),gameConfig.getDifficulty());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(count >= 10){
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
        gameConfig.setDifficulty(grad);

        if(isGradChoosable() && points.getPoints() >= grad){
            gradNotAvailableToast.cancel();
            Intent b2 = new Intent(ComplexityScaleActivity.this,Mode1.class);
            startActivity(b2);
            finish();
        } else {
            gradNotAvailableToast.show();
            // todo: Shake screen or disable a diffiulty if its not available or something
        }
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
}
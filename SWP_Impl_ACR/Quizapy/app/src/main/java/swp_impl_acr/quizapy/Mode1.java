package swp_impl_acr.quizapy;


import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

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

public class Mode1 extends AppCompatActivity implements EventListenerInterface {
    private ConstraintLayout layout;
    private List<Button> answerButtons;


    private ObjectAnimator animator;
    private TextView questionText;
    private List<Question> questions;
    private GameConfig gameConfig;
    private List<Answer> answers;
    TextView TimerText;
    SeekBar seekbar;
    boolean clicked= false;
    boolean clicked2=false;

    private Integer secondLeft=60;
    private Integer clickCounter=0;
    private Integer lastFrequency;
    AlertDialog.Builder builder;

    private static final int NOTHING = 0;


    private TextView difficulty;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout = (ConstraintLayout) View.inflate(this, R.layout.activity_mode1, null);
        setContentView(layout);


        builder = new AlertDialog.Builder(this);

        difficulty = (TextView) findViewById(R.id.textMode);
        seekbar =(SeekBar) findViewById(R.id.seekBar);
        TimerText =(TextView)findViewById(R.id.timerText);
        questionText = (TextView)(findViewById(R.id.questionTextMode));
        //Button button1 = (Button)findViewById(R.id.buttonMode1);
        Button button2 = (Button)findViewById(R.id.buttonMode2);
        Button button3 = (Button)findViewById(R.id.buttonMode3);
        Button button4 = (Button)findViewById(R.id.buttonMode4);

        seekbar.setMax(50);
        seekbar.setProgress(15);

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int minutes =(int)progress/60;
                int seconds = progress - minutes * 60;
                TimerText.setText(Integer.toString(seconds)+"s");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        answerButtons = new ArrayList<Button>();
        //answerButtons.add(button1);
        answerButtons.add(button2);
        answerButtons.add(button3);
        answerButtons.add(button4);






        gameConfig = GameConfig.getInstance();
        try {
            TopicDataSource topicDataSource = new TopicDataSource();
            questions = topicDataSource.getAllUnansweredQuestionsByDifficulty(gameConfig.getTopic().getId(), gameConfig.getDifficulty());
            questions = CollectionUtils.generateRandomList(questions, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        difficulty.setText(gradToString(gameConfig.getDifficulty()));
        TextView chosenTopic = (TextView) findViewById(R.id.chosen_topicMode);
        chosenTopic.setText(gameConfig.getTopic().getName());
        displayQuestion();



        if (!RespiratoryTrainer.isConnected()) {
            getSimulatorButtons();

        }


    }


    /**
     * adds Buttons to simulate the Respiratory Trainer to the top of the screen
     */
    private void getSimulatorButtons() {
        Buttons buttons = new Buttons(this, null);

        layout.addView(buttons);

        buttons.addEventListener(this);

    }



    @Override
    public void onBreathInStart() {
        clickCounter+=2;


       // Toast.makeText(getApplicationContext()," Wiederholtes Ausatmen",Toast.LENGTH_LONG).show();



        //scaleImage.invalidate();

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

    /**
     * returns string corresponding to set difficulty
     * @param grad
     * @return
     */
    private String gradToString(int grad){
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





    /**
     * displays the current question and its answers
     */
    private void displayQuestion() {
        questionText.setText(questions.get(0).getName());
        answers = questions.get(0).getAnswers();
        answers = CollectionUtils.generateRandomList(answers, answers.size());

        int i = 1;
        for(Button button:answerButtons){
            button.setBackgroundColor(Color.LTGRAY );
            button.setText(answers.get(i-1).getName());
            i++;
        }
    }



    public void onClick(View view){

        if(!clicked) {
             (view).setBackgroundColor(Color.CYAN );
             seekbar.setEnabled(false);                     //set SeekBar to be unmoveable
             clicked=true;
             builder.setMessage("Bitte, erzeugen Sie wiederholtes Ausatmen");
             AlertDialog dialog = builder.create();
             dialog.show();

            final int secondToCountDown = seekbar.getProgress();
            new CountDownTimer(secondToCountDown*1000, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {
                        int second = Math.round(millisUntilFinished / 1000);
                        if(secondToCountDown-1==second){
                            lastFrequency=clickCounter;
                        }else if (lastFrequency == clickCounter) {


                            Log.d("message", "great");
                            // Toast.makeText(getApplicationContext(), "ُyou are doing right", Toast.LENGTH_SHORT).show();
                        } else if (lastFrequency < clickCounter) {
                            Log.d("message", "go lower");
                            builder.setMessage("go lower");
                            AlertDialog dialog = builder.create();
                            dialog.show();

                            lastFrequency=clickCounter;
                            //Toast.makeText(getApplicationContext(), "ُmake it faster", Toast.LENGTH_SHORT).show();
                        }else if (lastFrequency > clickCounter) {
                            Log.d("message", "go faster");
                            builder.setMessage("go faster");
                            AlertDialog dialog = builder.create();
                            dialog.show();
                            lastFrequency=clickCounter;
                            //Toast.makeText(getApplicationContext(), "ُmake it slowler", Toast.LENGTH_SHORT).show();
                        }

                        secondLeft=second;
                        TimerText.setText(second + "s");
                        TimerText.setTextColor(Color.parseColor("#FFFFFF"));
                        clickCounter=0;

                    }

                    @Override
                    public void onFinish() {
                        TimerText.setText("0 s");
                        TimerText.setTextColor(Color.parseColor("#FFA61113"));
                        Intent b2 = new Intent(Mode1.this,Mode2.class);
                        startActivity(b2);
                    }
        }.start();





    } else{ Toast.makeText(getApplicationContext(),"ُEine Antwort ist ausgewählt",Toast.LENGTH_LONG).show();}


    }


public  void resetTimer (){
    TimerText.setText("15s");
    seekbar.setProgress(15);
    seekbar.setEnabled(false);

}




}
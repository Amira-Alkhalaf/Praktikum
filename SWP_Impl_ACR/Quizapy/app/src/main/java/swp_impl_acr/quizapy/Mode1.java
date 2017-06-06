package swp_impl_acr.quizapy;


import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import swp_impl_acr.quizapy.Helper.CollectionUtils;
import swp_impl_acr.quizapy.RespiratoryTrainerSimulation.Buttons;


/**
 * Mode1 : where the user answers the questions one at a time
 */
public class Mode1 extends QuestionActivity {

    private int timeToHold = 30;
    private int breathingFrequency = 15;
    private int userFrequency = 0;

    private SeekBar seekbar;
    private TextView timerText;
    private ImageView imageView;
    private TextView helpText;
    private TextView helpText2;

    private boolean clicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        simButtons = Buttons.SEEKBAR_BREATHING_RATE;
        super.onCreate(savedInstanceState);
        mode.setText("Modus 1");

        View child = (ConstraintLayout) View.inflate(this, R.layout.activity_mode1,null);
        layout.addView(child);
        ConstraintSet set2 = new ConstraintSet();
        set2.clone(layout);
        set2.connect(child.getId(), ConstraintSet.TOP, questionText.getId(), ConstraintSet.BOTTOM, 0);
        set2.applyTo(layout);

        seekbar = (SeekBar)findViewById(R.id.seekBar);
        timerText = (TextView)findViewById(R.id.timerText);
        timerText.setText(timeToHold + "s");
        imageView = (ImageView)findViewById(R.id.imageView);
        helpText = (TextView)findViewById(R.id.helpTextMode1);
        helpText2 = (TextView)findViewById(R.id.userFrequency);
        helpText.setText("Zielfrequenz: "+breathingFrequency);
        helpText2.setText("Benutzer Frequenz: "+userFrequency);


        seekbar.setVisibility(View.INVISIBLE);
        clicked = false;

        for(Button button:answerButtons){
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!clicked){
                        clicked=true;
                        v.setBackgroundColor(Color.CYAN);
                        Toast.makeText(getApplicationContext(), "ُErzeugen Sie wiederholtes Ausatmen und Einatmen", Toast.LENGTH_SHORT).show();
                        startTimer(v);
                    }
                }
            });
        }
    }


    @Override
    public void onBreathingRateChange(int progress) {
        userFrequency=progress;
        helpText2.setText("Benutzer Frequenz: "+userFrequency);
    }


    @Override
    void getAnswers() {
        answers = sessionStorage.getQuestions().get(0).getAnswers(4);
        answers = CollectionUtils.generateRandomList(answers, answers.size());
    }

    private void startTimer(final View v){

        final int secondToCountDown = timeToHold;
        new CountDownTimer(secondToCountDown * 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                Log.i("message", String.valueOf(millisUntilFinished / 1000));
                int second = Math.round(millisUntilFinished / 1000);
                CompareFrequency();
                timerText.setText(second + "s");
                timerText.setTextColor(Color.parseColor("#FFFFFF"));
            }

                /*
                 * An answer will be saved when the timer ends.
                 */

            @Override
            public void onFinish() {

                if (userFrequency <= breathingFrequency*1.10 && userFrequency >= breathingFrequency*0.90) {
                    Toast.makeText(getApplicationContext(), "ُDie Antwort wurde ausgewählt", Toast.LENGTH_SHORT).show();
                } else if (userFrequency < breathingFrequency*1.10) {
                    v.setBackgroundColor(Color.LTGRAY);
                    int i = (int)v.getTag()-1;
                    boolean foundAnswer = false;
                    while(!foundAnswer) {
                        if (i == -1) {
                            i = 3;
                        }
                        Button answer = answerButtons.get(i);
                        if (answer.isEnabled() && answer.getVisibility() == View.VISIBLE) {
                            answer.setBackgroundColor(Color.CYAN);
                            Toast.makeText(getApplicationContext(), "ُDie vorausgehende Antwort wurde ausgewählt", Toast.LENGTH_LONG).show();
                            foundAnswer = true;
                        } else {
                            i--;
                        }
                    }
                } else if (userFrequency > breathingFrequency*0.90){
                    v.setBackgroundColor(Color.LTGRAY);
                    int i = (int)v.getTag()+1;
                    boolean foundAnswer = false;
                    while(!foundAnswer) {
                        if (i == 4) {
                            i = 0;
                        }
                        Button answer = answerButtons.get(i);
                        if (answer.isEnabled() && answer.getVisibility() == View.VISIBLE) {
                            answer.setBackgroundColor(Color.CYAN);
                            Toast.makeText(getApplicationContext(), "ُDie nachfolgende Antwort wurde ausgewählt", Toast.LENGTH_LONG).show();
                            foundAnswer = true;
                        } else {
                            i++;
                        }
                    }
                }
                timerText.setText("0 s");
                timerText.setTextColor(Color.parseColor("#FFA61113"));
                saveSelectedAnswer();
                next();
            }
        }.start();
    }

    
    /**
     * Compares the entered frequency and the previously initialized frequency
     */
    public void CompareFrequency() {  // if(secondToCountDown-1==second) {
        //  Frequency=clickCounter; }
        if (userFrequency <= breathingFrequency*1.10 && userFrequency >= breathingFrequency*0.90) {
            Log.d("message", "great "+breathingFrequency+" "+userFrequency);
            helpText.setText("Zielfrequenz: "+breathingFrequency);
        }
        else if (userFrequency > breathingFrequency*1.10) {

            Log.d("message", "go lower "+breathingFrequency+" "+userFrequency);
            helpText.setText("Zielfrequenz: "+breathingFrequency+" Atme langsamer");
            //lastFrequency=clickCounter;
        }
        else if (userFrequency < breathingFrequency*0.90) {
            Log.d("message", "go faster "+breathingFrequency+" "+userFrequency);
            helpText.setText("Zielfrequenz: "+breathingFrequency+" Atme schneller");
            //  lastFrequency=clickCounter;
        }
    }
}


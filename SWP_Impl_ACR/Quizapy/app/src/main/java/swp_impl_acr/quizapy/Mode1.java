package swp_impl_acr.quizapy;


import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import swp_impl_acr.quizapy.Helper.CollectionUtils;
import swp_impl_acr.quizapy.RespiratoryTrainerSimulation.Buttons;


/*
 * Mode1 : where the user answers the questions one at a time
 */


public class Mode1 extends QuestionActivity implements View.OnClickListener {


    boolean clicked2 = false;
    private Integer secondLeft = 60;

    private Integer clickCounter = 0;

    private Integer Frequency = 1;

    private SeekBar seekbar;
    private TextView timerText;
    private ImageView imageView;

    /* Variable to determine whether the frequency entered equals the previously
    * initialized frequency, is less than that, or is bigger than that.
    */

    private Integer FrequencyRate = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        simButtons = Buttons.BUTTON_RepeatedlyBreathINandOUT;
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
        imageView = (ImageView)findViewById(R.id.imageView);


        /*
         *  seekbar to display time selection
         */

        seekbar.setMax(60);
        seekbar.setProgress(15);

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int minutes = (int) progress / 60;
                int seconds = progress - minutes * 60;
                timerText.setText(Integer.toString(seconds) + "s");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }


    @Override
    void getAnswers() {
        answers = sessionStorage.getQuestions().get(0).getAnswers(4);
        answers = CollectionUtils.generateRandomList(answers, answers.size());
    }



    /*
     *  Counts the number of breaths taken ( inhales followed by exhales)
     */

    @Override
    public void onRepeatedlyBreathINandOUTstart() {
        clickCounter++;

    }



/*
 *  The timer starts when a choice is made.
 */

    @Override
    public void onClick(final View v) {
        if (!clicked2) {
            clicked2 = true;
            (v).setBackgroundColor(Color.CYAN);
            seekbar.setEnabled(false);                              // make seekbar invisible
            Toast.makeText(getApplicationContext(), "ُErzeugen Sie wiederholtes Ausatmen und Einatmen", Toast.LENGTH_SHORT).show();
            final int secondToCountDown = seekbar.getProgress();
            new CountDownTimer(secondToCountDown * 1000, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                     Log.i("message", String.valueOf(millisUntilFinished / 1000));
                    int second = Math.round(millisUntilFinished / 1000);
                    if (second % 2 == 0) {
                          CompareFrequency();
                          clickCounter = 0;
                          secondLeft = second;
                          timerText.setText(second + "s");
                          timerText.setTextColor(Color.parseColor("#FFFFFF"));
                    } else {
                          secondLeft = second;
                          timerText.setText(second + "s");
                          timerText.setTextColor(Color.parseColor("#FFFFFF"));
                    }


                }

                /*
                 * An answer will be saved when the timer ends.
                 */

                @Override
                public void onFinish() {

                    if (FrequencyRate == 0) {
                        Toast.makeText(getApplicationContext(), "ُDie Antwort wurde ausgewählt", Toast.LENGTH_SHORT).show();
                    } else if (FrequencyRate == 1) {
                        v.setBackgroundColor(Color.LTGRAY);
                        switch (v.getId()) {
                            case R.id.button: {
                                Toast.makeText(getApplicationContext(), "ُDie vorausgehende Antwort wurde ausgewählt", Toast.LENGTH_LONG).show();
                                button4.setBackgroundColor(Color.CYAN);
                            }
                            break;
                            case R.id.button2: {
                                Toast.makeText(getApplicationContext(), "ُDie vorausgehende Antwort wurde ausgewählt", Toast.LENGTH_LONG).show();
                                button.setBackgroundColor(Color.CYAN);
                            }
                            break;
                            case R.id.button3: {
                                Toast.makeText(getApplicationContext(), "ُDie vorausgehende Antwort wurde ausgewählt", Toast.LENGTH_LONG).show();
                                button2.setBackgroundColor(Color.CYAN);
                            }
                            break;
                            case R.id.button4: {
                                Toast.makeText(getApplicationContext(), "ُDie vorausgehende Antwort wurde ausgewählt", Toast.LENGTH_LONG).show();
                                button3.setBackgroundColor(Color.CYAN);
                            }
                            break;
                        }

                    } else {
                        v.setBackgroundColor(Color.LTGRAY);
                        switch (v.getId()) {
                            case R.id.button: {
                                Toast.makeText(getApplicationContext(), "ُDie nachfolgendee Antwort wurde ausgewählt", Toast.LENGTH_LONG).show();
                                button2.setBackgroundColor(Color.CYAN);
                            }
                            break;
                            case R.id.button2: {
                                Toast.makeText(getApplicationContext(), "ُDie nachfolgende Antwort wurde ausgewählt", Toast.LENGTH_LONG).show();
                                button3.setBackgroundColor(Color.CYAN);
                            }
                            break;
                            case R.id.button3: {
                                Toast.makeText(getApplicationContext(), "ُDie nachfolgende Antwort wurde ausgewählt", Toast.LENGTH_LONG).show();
                                button4.setBackgroundColor(Color.CYAN);
                            }
                            break;
                            case R.id.button4: {
                                Toast.makeText(getApplicationContext(), "ُDie nachfolgende Antwort wurde ausgewählt", Toast.LENGTH_LONG).show();
                                button.setBackgroundColor(Color.CYAN);
                            }
                            break;
                        }

                    }


                    timerText.setText("0 s");
                    timerText.setTextColor(Color.parseColor("#FFA61113"));
                    saveSelectedAnswer();
                    next();
                }

            }.start();
        } else {
            Toast.makeText(getApplicationContext(), "ُEine Antwort ist ausgewählt", Toast.LENGTH_LONG).show();
        }


    }

    
    /*
     * Compares the entered frequency and the previously initialized frequency
     */


    public void CompareFrequency() {  // if(secondToCountDown-1==second) {
        //  Frequency=clickCounter; }
        if (Frequency == clickCounter) {
            Log.d("message", "great "+Frequency+" "+clickCounter);
            FrequencyRate=0;
        }
        else if (Frequency < clickCounter) {

            Log.d("message", "go lower "+Frequency+" "+clickCounter);
            Toast.makeText(getApplicationContext(),"ُgo lower "+Frequency+" "+clickCounter,Toast.LENGTH_SHORT).show();
            FrequencyRate=1;
            //lastFrequency=clickCounter;
        }
        else if (Frequency > clickCounter) {
            Log.d("message", "go faster "+Frequency+" "+clickCounter);
            Toast.makeText(getApplicationContext(),"ُgo faster "+Frequency+" "+clickCounter,Toast.LENGTH_SHORT).show();
            FrequencyRate=2;
            //  lastFrequency=clickCounter;
        }
    }






}


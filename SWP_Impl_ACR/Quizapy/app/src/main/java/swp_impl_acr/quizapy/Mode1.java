package swp_impl_acr.quizapy;


import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import swp_impl_acr.quizapy.Helper.CollectionUtils;
import swp_impl_acr.quizapy.RespiratoryTrainerSimulation.Buttons;

public class Mode1 extends QuestionActivity implements View.OnClickListener {

    boolean clicked= false;
    boolean clicked2=false;
    private Button button2,button3,button4;
    private Integer secondLeft=60;
    private Integer clickCounter=0;
    private Integer lastFrequency;
    AlertDialog.Builder builder;
    private Integer FrequencyRate=0;
    private static final int NOTHING = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        simButtons = Buttons.BUTTON_RepeatedlyBreathINandOUT;
        super.onCreate(savedInstanceState);
        mode.setText("Modus 1");
        imageView.setVisibility(View.VISIBLE);
        TimerText.setVisibility(View.VISIBLE);
        seekbar.setVisibility(View.VISIBLE);



        seekbar.setMax(60);
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


       // button3.setOnClickListener(this);







    }



    @Override
    void getAnswers() {
        answers = sessionStorage.getQuestions().get(0).getAnswers();
        answers = CollectionUtils.generateRandomList(answers, answers.size());
    }

    /**
     * either starts cursoranimation or saves the current selected answer and moves to the nextaction depending on the selected mode
     */

    @Override
    public void onRepeatedlyBreathINandOUTstart() {
        // if(view.getTag().toString()equals(Integer.toString("richtig")))

        clicked=true;
        clickCounter+=2;

    }

    /**
     * stops the animation
     */
    @Override
    public void onRepeatedlyBreathINandOUTstop () {

    }

    public void Timer(){
        seekbar.setEnabled(false);

        //set SeekBar to be unmoveable
        //    builder.setMessage("Bitte, erzeugen Sie wiederholtes Ausatmen");
        // AlertDialog dialog = builder.create();
        // dialog.show();

        final int secondToCountDown = seekbar.getProgress();
        new CountDownTimer(secondToCountDown*1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                int second = Math.round(millisUntilFinished / 1000);

                TimerText.setText( millisUntilFinished / 1000 +"s");
                TimerText.setTextColor(Color.parseColor("#FFFFFF"));
                clickCounter=0;
            }

            @Override
            public void onFinish() {
                TimerText.setText("0 s");
                TimerText.setTextColor(Color.parseColor("#FFA61113"));

            } }.start(); }




    @Override
    public void onClick(final View v) {
        if(!clicked2)
        {  clicked2=true;
            (v).setBackgroundColor(Color.CYAN );
            seekbar.setEnabled(false);

            final int secondToCountDown = seekbar.getProgress();
            new CountDownTimer(secondToCountDown*1000, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    int second = Math.round(millisUntilFinished / 1000);
                    if(secondToCountDown-1==second) {
                        lastFrequency=clickCounter; }
                    else if (lastFrequency == clickCounter) {
                        Log.d("message", "great");
                        FrequencyRate=0;
                    }
                    else if (lastFrequency < clickCounter) {
                        Log.d("message", "go lower");

                        FrequencyRate=1;
                        lastFrequency=clickCounter;
                    }
                    else if (lastFrequency > clickCounter) {
                        Log.d("message", "go faster");

                        FrequencyRate=2;
                        lastFrequency=clickCounter;       }
                    secondLeft=second;
                    TimerText.setText(second + "s");
                    TimerText.setTextColor(Color.parseColor("#FFFFFF"));
                    clickCounter=0;

                }

                @Override
                public void onFinish() {

                    TimerText.setText("0 s");
                    TimerText.setTextColor(Color.parseColor("#FFA61113"));
                    saveSelectedAnswer();
                    saveSelectedAnswer();
                        next();}

            }.start(); }


        else{ Toast.makeText(getApplicationContext(),"ُEine Antwort ist ausgewählt",Toast.LENGTH_LONG).show();}


    }
}


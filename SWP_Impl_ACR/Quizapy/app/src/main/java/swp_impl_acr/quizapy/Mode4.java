package swp_impl_acr.quizapy;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import swp_impl_acr.quizapy.Helper.CollectionUtils;
import swp_impl_acr.quizapy.RespiratoryTrainerSimulation.Buttons;


public class Mode4 extends QuestionActivity {

boolean clicked= false;
AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        simButtons = Buttons.BUTTON_BREATH_IN | Buttons.BUTTON_BREATH_OUT| Buttons.BUTTON_HOLD_BREATH;
        super.onCreate(savedInstanceState);
        mode.setText("Modus 4");
        button.setVisibility(View.INVISIBLE);
        button2.setVisibility(View.INVISIBLE);
        button3.setVisibility(View.INVISIBLE);
        button4.setVisibility(View.INVISIBLE);

        Toast.makeText(getApplicationContext(),"halten Sie kurz die Luft an",Toast.LENGTH_SHORT).show();

    }

    @Override
    void getAnswers() {
        answers = sessionStorage.getQuestions().get(0).getAnswers(2);
        answers = CollectionUtils.generateRandomList(answers, answers.size());
    }

    /**
     * either starts cursoranimation or saves the current selected answer and moves to the nextaction depending on the selected mode
     */
    @Override
    public void onBreathInStart() {

            button.setBackgroundColor(Color.CYAN );
            saveSelectedAnswer();
            next();


    }

    /**
     * either starts the animation or saves and moves to next action depending on the mode
     */
    @Override
    public void onBreathOutStart() {
            button2.setBackgroundColor(Color.CYAN );
            saveSelectedAnswer();
            next();


    }

    public void onHoldBreathStart(){


        clicked=true;
        button2.postDelayed(new Runnable() {
            public void run() {
                button.setVisibility(View.VISIBLE);
                button2.setVisibility(View.VISIBLE);
            }
        }, 2000);


    }

}



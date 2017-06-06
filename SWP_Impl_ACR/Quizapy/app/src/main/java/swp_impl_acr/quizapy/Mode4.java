package swp_impl_acr.quizapy;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import swp_impl_acr.quizapy.Helper.CollectionUtils;
import swp_impl_acr.quizapy.RespiratoryTrainerSimulation.Buttons;

import static swp_impl_acr.quizapy.RespiratoryTrainerSimulation.Buttons.breathIn;
import static swp_impl_acr.quizapy.RespiratoryTrainerSimulation.Buttons.breathOut;


public class Mode4 extends QuestionActivity {

    private boolean hasQuestionSeen;
    Toast helpText1;
    Toast helpText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        simButtons = Buttons.BUTTON_BREATH_IN | Buttons.BUTTON_BREATH_OUT| Buttons.BUTTON_HOLD_BREATH;
        super.onCreate(savedInstanceState);
        mode.setText("Modus 4");
        questionText.setVisibility(View.INVISIBLE);
        for(Button button:answerButtons){
            button.setVisibility(View.INVISIBLE);
        }
        hasQuestionSeen = false;

        helpText1 = Toast.makeText(getApplicationContext(),"halten Sie die Luft an um die Frage einzublenden",Toast.LENGTH_SHORT);
        helpText2 = Toast.makeText(getApplicationContext(),"Einatmen um die obere Antwort auszuwählen, Ausatmen um die untere ANtwort auszuwählen",Toast.LENGTH_LONG);

        helpText1.show();
        breathIn.setEnabled(false);
        breathOut.setEnabled(false);




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
        if(hasQuestionSeen){
            helpText2.cancel();
            helpText1.cancel();
            answerButtons.get(0).setBackgroundColor(Color.CYAN);
            Toast.makeText(getApplicationContext(),"Die erste Antwort ausgewählt wurde",Toast.LENGTH_SHORT).show();
            saveSelectedAnswer();
            next();
        }
    }

    /**
     * either starts the animation or saves and moves to next action depending on the mode
     */
    @Override
    public void onBreathOutStart() {
        if(hasQuestionSeen){
            helpText1.cancel();
            helpText2.cancel();
            answerButtons.get(1).setBackgroundColor(Color.CYAN);
            Toast.makeText(getApplicationContext(),"Die zweite Antwort ausgewählt wurde",Toast.LENGTH_SHORT).show();
            saveSelectedAnswer();
            next();
        }
    }

    public void onHoldBreathStart(){
    helpText1.cancel();

        for(Button button:answerButtons){
            if(button.isEnabled()){
                button.setVisibility(View.VISIBLE);
            }
        }
        questionText.setVisibility(View.VISIBLE);
        hasQuestionSeen=true;
        helpText2.show();
    }

    public void onHoldBreathStop(){

        breathIn.setEnabled(true);
        breathOut.setEnabled(true);
        helpText2.cancel();
        for(Button button:answerButtons){
            if(button.isEnabled()){
                button.setVisibility(View.INVISIBLE);
            }
        }
        questionText.setVisibility(View.INVISIBLE);
       // helpText1.show();
    }

}



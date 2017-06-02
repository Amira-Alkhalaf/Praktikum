package swp_impl_acr.quizapy;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;

import swp_impl_acr.quizapy.Cursor.Cursor;
import swp_impl_acr.quizapy.Helper.CollectionUtils;
import swp_impl_acr.quizapy.RespiratoryTrainerSimulation.Buttons;

public class Mode3 extends QuestionActivity {

    /**
     * constant for selectMode: no user action happened yet
     */
    protected static final int NOTHING = 0;
    /**
     * constant for selectMode: user started to choose answer with breathing in and locks the answer in with breathing out
     */
    protected static final int IN_OUT = 1;
    /**
     * constant for selectMode: user started to choose answer with breathing out and locks the answer in with breathing in
     */
    protected static final int OUT_IN = 2;

    /**
     * value represents if the user started choosing the answer with breathing in or breathing out
     */
    protected int selectMode = NOTHING;

    protected Cursor cursor = null;
    protected ObjectAnimator animator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        simButtons = Buttons.BUTTON_BREATH_IN | Buttons.BUTTON_BREATH_OUT;
        super.onCreate(savedInstanceState);
        mode.setText("Modus 3");

        cursor = new Cursor(this, 0,0);
        layout.addView(cursor);
        cursor.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void setCursor() {
        cursor.moveCursor(answerButtons.get(0).getLeft()-50,answerButtons.get(0).getTop());
        cursor.setVisibility(View.VISIBLE);
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
    public void graduallyBreathOutStart(){

    }
    @Override
    public void graduallyBreathOutStop(){

    }


    /**
     * sets Cursor animation and starts it
     */
    protected void startCursorAnimation() {
        animator = ObjectAnimator.ofFloat(cursor, "y", answerButtons.get(0).getTop()+25,answerButtons.get(answers.size()-1).getBottom()-25);
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

}

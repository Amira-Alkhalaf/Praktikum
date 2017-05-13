package swp_impl_acr.quizapy;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;

import swp_impl_acr.quizapy.Cursor.Cursor;
import swp_impl_acr.quizapy.Helper.RespiratoryTrainer;
import swp_impl_acr.quizapy.RespiratoryTrainerSimulation.Buttons;
import swp_impl_acr.quizapy.RespiratoryTrainerSimulation.EventListenerInterface;


public class TestActivity extends AppCompatActivity implements EventListenerInterface{
    private ConstraintLayout layout;
    private Buttons buttons;
    private Cursor cursor = null;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private TranslateAnimation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout = (ConstraintLayout) View.inflate(this, R.layout.activity_test, null);
        setContentView(layout);

        button1 = (Button) (findViewById(R.id.button));
        button2 = (Button) (findViewById(R.id.button2));
        button3 = (Button) (findViewById(R.id.button3));
        button4 = (Button) (findViewById(R.id.button4));


        if (!RespiratoryTrainer.isConnected()) {
            getButtons();
        }

        cursor = new Cursor(this, 0,0);
        layout.addView(cursor);
        cursor.setVisibility(View.INVISIBLE);


    }


    private void getButtons() {
        buttons = new Buttons(this, null);
        layout.addView(buttons);

        ConstraintSet set = new ConstraintSet();
        set.clone(layout);

        set.connect(buttons.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
        set.connect(buttons.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
        set.connect(buttons.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);

        set.applyTo(layout);

        buttons.addEventListener(this);

    }

    @Override
    public void onBreathInStart() {
        Rect rect = new Rect();
        button1.getGlobalVisibleRect(rect);

        cursor.setVisibility(View.VISIBLE);
        cursor.moveCursor(rect.left-50,rect.top);
        cursor.invalidate();

        Rect rect2 = new Rect();
        button4.getGlobalVisibleRect(rect2);
        //cursor.animate().translationYBy(rect2.bottom-rect.top).setDuration(5000);

        animation = new TranslateAnimation(0,0,0,rect2.bottom - rect.top);
        animation.setDuration(5000);
        animation.setRepeatMode(Animation.RESTART);
        animation.setRepeatCount(Animation.INFINITE);
        cursor.setAnimation(animation);
        cursor.animate();
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
}

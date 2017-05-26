package swp_impl_acr.quizapy.RespiratoryTrainerSimulation;


import android.content.Context;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import swp_impl_acr.quizapy.R;

/**
 * offers Buttons for Touch Control in case no Respiratory Trainer is connected
 */
public class Buttons extends ConstraintLayout implements View.OnTouchListener {

    private Button breathIn;
    private Button breathOut;
    private Button holdBreath;
    private Context context;

    private List<EventListenerInterface> eventListeners;

    /**
     * constructor
     *
     * @param context
     * @param attrs
     */
    public Buttons(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        inflate(context, R.layout.simulator_buttons, this);

        breathIn = (Button)findViewById(R.id.breathIn);
        breathOut = (Button)findViewById(R.id.breathOut);
        holdBreath = (Button)findViewById(R.id.holdBreath);

        breathIn.setOnTouchListener(this);
        breathOut.setOnTouchListener(this);
        holdBreath.setOnTouchListener(this);

        eventListeners = new ArrayList<>();
    }



    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            
            switch(v.getId()){
                case R.id.breathIn:
                    for(EventListenerInterface ev:eventListeners){
                        ev.onBreathInStart();
                    }
                    break;
                case R.id.breathOut:
                    for(EventListenerInterface ev:eventListeners){
                        ev.onBreathOutStart();
                    }
                    break;
                case R.id.holdBreath:
                    for(EventListenerInterface ev:eventListeners){
                        ev.onHoldBreathStart();
                    }
                    break;
            }
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            switch(v.getId()){
                case R.id.breathIn:
                    for(EventListenerInterface ev:eventListeners){
                        ev.onBreathInStop();
                    }
                    break;
                case R.id.breathOut:
                    for(EventListenerInterface ev:eventListeners){
                        ev.onBreathOutStop();
                    }
                    break;
                case R.id.holdBreath:
                    for(EventListenerInterface ev:eventListeners){
                        ev.onHoldBreathStop();
                    }
                    break;
            }
        }
        return true;
    }

    /**
     * adds eventListener
     *
     * @param eventListener
     */
    public void addEventListener(EventListenerInterface eventListener){
        eventListeners.add(eventListener);
    }

    /**
     * removes eventListener
     *
     * @param eventListener
     */
    public void removeEventListener(EventListenerInterface eventListener) {
        eventListeners.remove(eventListener);
    }
}

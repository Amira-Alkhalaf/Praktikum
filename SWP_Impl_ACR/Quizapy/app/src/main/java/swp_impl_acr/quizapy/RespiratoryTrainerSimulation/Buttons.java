package swp_impl_acr.quizapy.RespiratoryTrainerSimulation;


import android.content.Context;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * offers Buttons for Touch Control in case no Respiratory Trainer is connected
 */
public class Buttons extends LinearLayout {

    public static final int BUTTON_BREATH_IN = 1;
    public static final int BUTTON_BREATH_OUT = 2;
    public static final int BUTTON_HOLD_BREATH = 4;
    public static final int SEEKBAR_BREATHING_RATE = 8;
    public static final int BUTTON_graduallyBreathIN=16;
    public static final int BUTTON_graduallyBreathOUT=32;



    private Button breathIn;
    private Button breathOut;
    private Button holdBreath;
    private SeekBar breathingRate;
    private Context context;
    private Button graduallyBreathIN;
    private Button graduallyBreathOUT ;

    private List<EventListenerInterface> eventListeners;

    /**
     * constructor
     *
     * @param context
     * @param attrs
     */
    public Buttons(Context context, @Nullable AttributeSet attrs, int buttons) {
        super(context, attrs);
        this.context = context;

        if( (buttons & BUTTON_BREATH_IN) != 0) {
            breathIn = new Button(context);
            breathIn.setId(BUTTON_BREATH_IN);
            breathIn.setText("Einatmen");
            breathIn.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            this.addView(breathIn);
            breathIn.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        for(EventListenerInterface ev:eventListeners){
                            ev.onBreathInStart();
                        }
                    } else if(event.getAction() == MotionEvent.ACTION_UP) {
                        for(EventListenerInterface ev:eventListeners){
                            ev.onBreathInStop();
                        }
                    }
                    return true;
                }
            });
        }
        if( (buttons & BUTTON_BREATH_OUT) != 0) {
            breathOut = new Button(context);
            breathOut.setId(BUTTON_BREATH_OUT);
            breathOut.setText("Ausatmen");
            breathOut.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            this.addView(breathOut);
            breathOut.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        for(EventListenerInterface ev:eventListeners){
                            ev.onBreathOutStart();
                        }
                    } else if(event.getAction() == MotionEvent.ACTION_UP) {
                        for(EventListenerInterface ev:eventListeners){
                            ev.onBreathOutStop();
                        }
                    }
                    return true;
                }
            });
        }
        if( (buttons & BUTTON_HOLD_BREATH) != 0) {
            holdBreath = new Button(context);
            holdBreath.setId(BUTTON_HOLD_BREATH);
            holdBreath.setText("Anhalten");
            holdBreath.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            this.addView(holdBreath);
            holdBreath.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        for(EventListenerInterface ev:eventListeners){
                            ev.onHoldBreathStart();
                        }
                    } else if(event.getAction() == MotionEvent.ACTION_UP) {
                        for(EventListenerInterface ev:eventListeners){
                            ev.onHoldBreathStop();
                        }
                    }
                    return true;
                }
            });
        }
        if( (buttons & BUTTON_graduallyBreathIN) != 0) {
            graduallyBreathIN = new Button(context);
            graduallyBreathIN.setId(BUTTON_graduallyBreathIN);
            graduallyBreathIN.setText("Stufenweises Einatmen");
            graduallyBreathIN.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            this.addView(graduallyBreathIN);
            graduallyBreathIN.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        for(EventListenerInterface ev:eventListeners){
                            ev.graduallyBreathInStart();
                        }
                    } else if(event.getAction() == MotionEvent.ACTION_UP) {
                        for(EventListenerInterface ev:eventListeners){
                            ev.graduallyBreathInStop();
                        }
                    }
                    return true;
                }
            });
        }

        if( (buttons & BUTTON_graduallyBreathOUT) != 0) {
            graduallyBreathOUT = new Button(context);
            graduallyBreathOUT.setId(BUTTON_graduallyBreathOUT);
            graduallyBreathOUT.setText("Stufenweises Ausatmen");
            graduallyBreathOUT.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            this.addView(graduallyBreathOUT);
            graduallyBreathOUT.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        for(EventListenerInterface ev:eventListeners){
                            ev.graduallyBreathOutStart();
                        }
                    } else if(event.getAction() == MotionEvent.ACTION_UP) {
                        for(EventListenerInterface ev:eventListeners){
                            ev.graduallyBreathOutStop();
                        }
                    }
                    return true;
                }
            });
        }



        if( (buttons & SEEKBAR_BREATHING_RATE) != 0) {
            breathingRate = new SeekBar(context);
            breathingRate.setMax(100);
            breathingRate.setProgress(0);
            breathingRate.setId(SEEKBAR_BREATHING_RATE);
            breathingRate.setLayoutParams(new LayoutParams(400, LayoutParams.MATCH_PARENT));

            this.addView(breathingRate);
            breathingRate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    for (EventListenerInterface ev : eventListeners) {
                        ev.onBreathingRateChange();
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }

        eventListeners = new ArrayList<>();
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

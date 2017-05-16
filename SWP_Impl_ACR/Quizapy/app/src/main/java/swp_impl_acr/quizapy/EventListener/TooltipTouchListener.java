package swp_impl_acr.quizapy.EventListener;

import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * Listener to display tooltips on touch
 */
public class TooltipTouchListener implements View.OnTouchListener {
    private TextView helpText = null;

    /**
     * constructor
     * @param helpText the text which should be displayed
     */
    public TooltipTouchListener(TextView helpText) {
        this.helpText = helpText;
    }

    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            this.helpText.setVisibility(View.VISIBLE);
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            this.helpText.setVisibility(View.INVISIBLE);
        }
        return true;
    }
}


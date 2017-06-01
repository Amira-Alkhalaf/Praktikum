package swp_impl_acr.quizapy;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;

import swp_impl_acr.quizapy.RespiratoryTrainerSimulation.Buttons;

public class Mode4 extends QuestionActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        simButtons = 0;
        super.onCreate(savedInstanceState);
        mode.setText("Modus 4");
    }

    @Override
    void getAnswers() {
        answers = new ArrayList<>();
    }

}

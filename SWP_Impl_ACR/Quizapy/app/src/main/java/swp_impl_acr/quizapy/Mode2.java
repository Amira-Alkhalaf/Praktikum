package swp_impl_acr.quizapy;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;


public class Mode2 extends QuestionActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        simButtons = 0;
        super.onCreate(savedInstanceState);
        mode.setText("Modus 2");
    }

    @Override
    void getAnswers() {
        answers = new ArrayList<>();
    }
}
package swp_impl_acr.quizapy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import static swp_impl_acr.quizapy.R.id.button5;
import static swp_impl_acr.quizapy.R.id.button6;

public class TypesOfQuestions extends AppCompatActivity {

    private Button button1,button2;
    private ImageView imageView;
    private TextView console;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_typesofquestions);

        button1 = (Button) findViewById(button5);
        button2=(Button)findViewById(button6);
        imageView = (ImageView) findViewById(R.id.image2);
        console = (TextView) findViewById(R.id.console2);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                RotateAnimation rotateAnimation = new RotateAnimation(0, 360f,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);

                rotateAnimation.setInterpolator(new LinearInterpolator());
                rotateAnimation.setDuration(500);
                rotateAnimation.setRepeatCount(Animation.INFINITE);

                findViewById(R.id.image2).startAnimation(rotateAnimation);

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent b2 = new Intent(TypesOfQuestions.this,ComplexityScaleActivity.class);
                startActivity(b2);
            }
        });





    }





    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}





package swp_impl_acr.quizapy;

/**
 * Created by Amira on 5/16/2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Amira on 5/16/2017.
 */

public class ComplexityScaleActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView console;
    private Button button1,button2;
    private float rotation = 0;
    private String grad = "low";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complexityscale);

        imageView = (ImageView) findViewById(R.id.image);
        button1 = (Button) findViewById(R.id.button);
        button2=(Button)findViewById(R.id.button2);
        console = (TextView) findViewById(R.id.console);
        console.append(grad);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rotation = rotation + 120;
                imageView.setRotation(rotation);
                switch(grad){
                    case "low":  grad="high";
                        break;
                    case "med":  grad="low";
                        break;
                    case "high":  grad="med";
                        break;
                }
                console.setText(grad);
            }
        });


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent b2 = new Intent(ComplexityScaleActivity.this,QuestionActivity.class);
                b2.putExtra("GRAD", grad);
                startActivity(b2);
            }
        });





    }





    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
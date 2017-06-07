package swp_impl_acr.quizapy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import swp_impl_acr.quizapy.Database.DataSource.OptionDataSource;

public class OptionsActivity extends AppCompatActivity {
    private Button saveButton;
    private OptionDataSource optionDataSource;
    private TextView questionsInSequence;
    private TextView frequencyMode1;
    private TextView frequencyMode2;
    private TextView frequencyMode3;
    private TextView frequencyMode4;
    private TextView frequencyMode5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        try {
            optionDataSource = new OptionDataSource();
        } catch (Exception e) {
            e.printStackTrace();
        }
        questionsInSequence = (TextView) findViewById(R.id.editQuestionsInSequenceText);
        questionsInSequence.setText(optionDataSource.getValue("questions_in_sequence"));
        frequencyMode1 = (TextView) findViewById(R.id.editMode1);
        frequencyMode1.setText(optionDataSource.getValue("frequency_mode_1"));
        frequencyMode2 = (TextView) findViewById(R.id.editMode2);
        frequencyMode2.setText(optionDataSource.getValue("frequency_mode_2"));
        frequencyMode3 = (TextView) findViewById(R.id.editMode3);
        frequencyMode3.setText(optionDataSource.getValue("frequency_mode_3"));
        frequencyMode4 = (TextView) findViewById(R.id.editMode4);
        frequencyMode4.setText(optionDataSource.getValue("frequency_mode_4"));
        frequencyMode5 = (TextView) findViewById(R.id.editMode5);
        frequencyMode5.setText(optionDataSource.getValue("frequency_mode_5"));

        saveButton = (Button)findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateOptions()){
                    optionDataSource.setValue("questions_in_sequence", questionsInSequence.getText().toString());
                    optionDataSource.setValue("frequency_mode_1", frequencyMode1.getText().toString());
                    optionDataSource.setValue("frequency_mode_2", frequencyMode2.getText().toString());
                    optionDataSource.setValue("frequency_mode_3", frequencyMode3.getText().toString());
                    optionDataSource.setValue("frequency_mode_4", frequencyMode4.getText().toString());
                    optionDataSource.setValue("frequency_mode_5", frequencyMode5.getText().toString());
                    Toast.makeText(OptionsActivity.this, "Speicherung erfolgreich", Toast.LENGTH_SHORT).show();
                    Intent b2 = new Intent(OptionsActivity.this,StartActivity.class);
                    startActivity(b2);
                }

            }
        });

    }

    private boolean validateOptions() {
        if (Integer.parseInt(questionsInSequence.getText().toString()) < 10) {
            Toast.makeText(this, "Fragenanzahl in einer Sequenz muss mindestens 10 betragen", Toast.LENGTH_LONG).show();
            return false;
        } else if ((Integer.parseInt(frequencyMode1.getText().toString())
                + Integer.parseInt(frequencyMode2.getText().toString())
                + Integer.parseInt(frequencyMode3.getText().toString())
                + Integer.parseInt(frequencyMode4.getText().toString())
                + Integer.parseInt(frequencyMode5.getText().toString()))
                != 100) {
            Toast.makeText(this, "Die Häufigkeiten sollten zusammen 100 betragen", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

}

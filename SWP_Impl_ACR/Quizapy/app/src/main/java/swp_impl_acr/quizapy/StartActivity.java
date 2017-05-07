package swp_impl_acr.quizapy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Arrays;

import swp_impl_acr.quizapy.Database.DataSource.QuestionDataSource;
import swp_impl_acr.quizapy.Database.QuizapyDataSource;
import swp_impl_acr.quizapy.EventListener.TooltipTouchListener;
import swp_impl_acr.quizapy.Helper.ImportParser;
import swp_impl_acr.quizapy.Helper.RespiratoryTrainerDetector;

public class StartActivity extends AppCompatActivity {

    public QuizapyDataSource databaseConnection = null;
    public GameConfig gameConfig = null;
    private final String SQL_ERROR_TAG = "SQL ERROR";

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_import_questions:
                InputStream in = getResources().openRawResource(
                        getResources().getIdentifier("testdata",
                                "raw", getPackageName()));
                try {
                    ImportParser.parseQuestionJSON(in);
                    TextView test = (TextView) findViewById(R.id.allQuestionsText);
                    TextView test2 = (TextView) findViewById(R.id.answeredQuestionsText);
                    QuestionDataSource questionDataSource = new QuestionDataSource();
                    test.setText(Integer.toString(questionDataSource.getAllQuestionsCount()));
                    test2.setText(Integer.toString(questionDataSource.getAllAnsweredQuestionsCount()));

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_toolbar, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);

        setSupportActionBar(myToolbar);

        try {
            databaseConnection = QuizapyDataSource.getInstance(getApplicationContext());
        } catch (SQLException e) {
            Log.d(SQL_ERROR_TAG, Arrays.toString(e.getStackTrace()));
        }

        try {
            TextView test = (TextView) findViewById(R.id.allQuestionsText);
            TextView test2 = (TextView) findViewById(R.id.answeredQuestionsText);
            QuestionDataSource questionDataSource = new QuestionDataSource();
            test.setText(questionDataSource.getAllQuestionsCount());
            test2.setText(questionDataSource.getAllAnsweredQuestionsCount());
        } catch (Exception e) {
            Log.d(SQL_ERROR_TAG, Arrays.toString(e.getStackTrace()));
        }

        Log.d("Atem Trainer", RespiratoryTrainerDetector.isRespiratoryTrainerConnected()? "is connected":"is not connected");

        final TextView bonusPointsText = (TextView) findViewById(R.id.bonusPointsText);
        final ImageButton bonusPointsButton = (ImageButton) findViewById(R.id.bonusPointsButton);
        bonusPointsButton.setOnTouchListener(new TooltipTouchListener(bonusPointsText));

        final TextView questionsText = (TextView) findViewById(R.id.questionsText);
        final ImageButton questionsButton = (ImageButton) findViewById(R.id.questionsButton);
        questionsButton.setOnTouchListener(new TooltipTouchListener(questionsText));

        final Button playButton = (Button) findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameConfig = GameConfig.getInstance();
                //TODO implement logic
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            databaseConnection.closeConnection();
        } catch (SQLException e) {
            Log.d(SQL_ERROR_TAG, Arrays.toString(e.getStackTrace()));
        }
    }
}

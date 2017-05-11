package swp_impl_acr.quizapy;

import android.content.Intent;
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
import java.util.List;
import java.util.Random;

import swp_impl_acr.quizapy.Database.DataSource.QuestionDataSource;
import swp_impl_acr.quizapy.Database.Entity.Answer;
import swp_impl_acr.quizapy.Database.Entity.Question;
import swp_impl_acr.quizapy.Database.QuizapyDataSource;
import swp_impl_acr.quizapy.EventListener.TooltipTouchListener;
import swp_impl_acr.quizapy.Helper.ImportParser;
import swp_impl_acr.quizapy.Helper.RespiratoryTrainerDetector;

public class StartActivity extends AppCompatActivity {

    private QuizapyDataSource databaseConnection = null;
    private GameConfig gameConfig = null;
    private AvailablePoints points = null;

    private QuestionDataSource questionDataSource = null;

    private TextView allQuestionsCount;
    private TextView answeredQuestionsCount;
    private TextView bonusPoints;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_import_questions:
                InputStream in = getResources().openRawResource(
                        getResources().getIdentifier("testdata",
                                "raw", getPackageName()));
                try {
                    ImportParser.parseQuestionJSON(in);
                    allQuestionsCount.setText(Integer.toString(questionDataSource.getAllQuestionsCount()));
                    answeredQuestionsCount.setText(Integer.toString(questionDataSource.getAllAnsweredQuestionsCount()));

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
            Log.d("SQL ERROR", Arrays.toString(e.getStackTrace()));
        }
        allQuestionsCount = (TextView) findViewById(R.id.allQuestionsText);
        answeredQuestionsCount = (TextView) findViewById(R.id.answeredQuestionsText);

        try {
            questionDataSource = new QuestionDataSource();
            allQuestionsCount.setText(Integer.toString(questionDataSource.getAllQuestionsCount()));
            answeredQuestionsCount.setText(Integer.toString(questionDataSource.getAllAnsweredQuestionsCount()));
        } catch (Exception e) {
            Log.d("SQL ERROR", Arrays.toString(e.getStackTrace()));
        }

        Log.d("Atem Trainer", RespiratoryTrainerDetector.isConnected() ? "is connected" : "is not connected");

        points = (AvailablePoints)getApplicationContext();
        points.setPoints(6);

        bonusPoints = (TextView) findViewById(R.id.currentBonusPointsText);
        bonusPoints.setText(Integer.toString(points.getPoints()));

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
                gameConfig.resetInstance();
                Random rand = new Random();
                try {
                    QuestionDataSource questionDataSource = new QuestionDataSource();
                    List<Question> list = questionDataSource.getAllUnansweredQuestions();
                    for(Question question:list){
                        List<Answer> answers = question.getAnswers();
                        gameConfig.addAnswer(answers.get(rand.nextInt(answers.size())));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                gameConfig.setDifficulty(1);
                Intent i = new Intent(getBaseContext(), QuestionListActivity.class);
                startActivity(i);
                //TODO implement logic
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        bonusPoints.setText(Integer.toString(points.getPoints()));

        try {
            answeredQuestionsCount.setText(Integer.toString(questionDataSource.getAllAnsweredQuestionsCount()));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            databaseConnection.closeConnection();
        } catch (SQLException e) {
            Log.d("SQL ERROR", Arrays.toString(e.getStackTrace()));
        }
    }
}

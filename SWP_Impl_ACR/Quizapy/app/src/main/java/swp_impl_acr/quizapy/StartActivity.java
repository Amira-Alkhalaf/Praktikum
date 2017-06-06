package swp_impl_acr.quizapy;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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
import android.widget.Toast;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import swp_impl_acr.quizapy.Database.DataSource.QuestionDataSource;
import swp_impl_acr.quizapy.Database.DataSource.StuffDataSource;
import swp_impl_acr.quizapy.Database.DataSource.TopicDataSource;
import swp_impl_acr.quizapy.Database.Entity.Topic;
import swp_impl_acr.quizapy.Database.QuizapyDataSource;
import swp_impl_acr.quizapy.EventListener.TooltipTouchListener;
import swp_impl_acr.quizapy.Helper.ImportParser;
import swp_impl_acr.quizapy.Helper.RespiratoryTrainer;

/**
 * start screen activity
 * option to import questions, shows current points and question amount
 */
public class StartActivity extends AppCompatActivity {

    private QuizapyDataSource databaseConnection = null;
    private SessionStorage sessionStorage = null;

    private QuestionDataSource questionDataSource = null;

    private TextView allQuestionsCount;
    private TextView answeredQuestionsCount;
    private TextView bonusPoints;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_import_questions:
                CharSequence jsonlist[] = new CharSequence[] {"testdata", "testdaten1", "testdaten2", "testdaten3"};
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Wähle Datei");
                builder.setItems(jsonlist, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        InputStream in;
                        int schema;
                        switch(which){
                            case 0:
                                in = getResources().openRawResource(
                                        getResources().getIdentifier("testdata",
                                                "raw", getPackageName()));
                                schema = 0;
                                break;
                            case 1:
                                in = getResources().openRawResource(
                                        getResources().getIdentifier("testdaten1",
                                                "raw", getPackageName()));
                                schema = 1;
                                break;
                            case 2:
                                in = getResources().openRawResource(
                                        getResources().getIdentifier("testdaten2",
                                                "raw", getPackageName()));
                                schema = 1;
                                break;
                            case 3:
                                in = getResources().openRawResource(
                                        getResources().getIdentifier("testdaten3",
                                                "raw", getPackageName()));
                                schema = 1;
                                break;
                            default:
                                schema = -1;
                                in =null;
                                break;
                        }
                        if(in != null || schema != -1){
                            try {
                                boolean valid = ImportParser.parseQuestionJSON(in, schema);
                                allQuestionsCount.setText(Integer.toString(questionDataSource.getAllQuestionsCount()));
                                answeredQuestionsCount.setText(Integer.toString(questionDataSource.getAllAnsweredQuestionsCount()));
                                if(valid){
                                    Toast.makeText(StartActivity.this, "Fragen (erfolgreich) importiert", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(StartActivity.this, "Es wurden nicht alle Fragen importiert", Toast.LENGTH_SHORT).show();
                                }
                                //todo: implement progress bar

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(StartActivity.this, "error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.show();
                return true;
            case R.id.action_options:
                Intent i = new Intent(getBaseContext(), OptionsActivity.class);
                startActivity(i);
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
            databaseConnection.openConnection();
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

        Log.i("Atem Trainer", RespiratoryTrainer.isConnected() ? "is connected" : "is not connected");

        sessionStorage = SessionStorage.getInstance();

        if(savedInstanceState == null){
            sessionStorage.setPoints(6);
        }

        bonusPoints = (TextView) findViewById(R.id.currentBonusPointsText);
        bonusPoints.setText(Integer.toString(sessionStorage.getPoints()));

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
                sessionStorage.resetInstance();
                try {
                    TopicDataSource topicDataSource = new TopicDataSource();
                    StuffDataSource stuffDataSource = new StuffDataSource();
                    List<Topic> topics = topicDataSource.getChoosableTopics(sessionStorage.getPoints(), Integer.parseInt(stuffDataSource.getValue("questions_in_sequence").toString()));
                    if(topics.size()==0 || sessionStorage.getPoints()==0){
                        Toast.makeText(StartActivity.this, "Nicht genügend Fragen oder Punkte zur Verfügung", Toast.LENGTH_SHORT).show();
                    } else {
//                        Intent test = new Intent(getBaseContext(), TypesOfQuestions.class);
//                        startActivity(test);
                        Intent in = new Intent(getBaseContext(), TopicSelectionActivity.class);
                        startActivity(in);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        bonusPoints.setText(Integer.toString(sessionStorage.getPoints()));

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

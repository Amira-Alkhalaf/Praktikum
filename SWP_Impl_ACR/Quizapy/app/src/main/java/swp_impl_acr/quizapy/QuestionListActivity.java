package swp_impl_acr.quizapy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import swp_impl_acr.quizapy.Adapter.CustomListAdapter;
import swp_impl_acr.quizapy.Database.DataSource.QuestionDataSource;
import swp_impl_acr.quizapy.Database.Entity.Answer;
import swp_impl_acr.quizapy.Database.Entity.Question;

public class QuestionListActivity extends AppCompatActivity {

    QuestionDataSource questionDataSource = null;
    GameConfig gameConfig = null;
    AvailablePoints points = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_question_list);

        gameConfig = GameConfig.getInstance();

        points = (AvailablePoints)getApplicationContext();

        try {
            questionDataSource = new QuestionDataSource();
        } catch (Exception e) {
            e.printStackTrace();
        }

        updateAvailablePoints();

        ListView listView = (ListView)findViewById(R.id.listView);

        CustomListAdapter adapter = new CustomListAdapter(this, gameConfig.getAnswers());
        listView.setAdapter(adapter);
    }

    private void updateAvailablePoints() {
        if(updateQuestions()){
            points.addPoints(gameConfig.getDifficulty());
        } else {
            points.subPoints(gameConfig.getDifficulty());
        }
    }

    private boolean updateQuestions() {
        ArrayList<Answer> answers = gameConfig.getAnswers();
        boolean allQuestionsCorrect=true;
        for(Answer answer:answers){
            Log.d("test", answer.toString());
            Question question = answer.getQuestion();
            if(!answer.isCorrectAnswer()){
                allQuestionsCorrect=false;
                question.setCorrectly(false);
            } else {
                question.setCorrectly(true);
            }
            question.setAnswered(true);
            saveQuestion(question);
        }
        return allQuestionsCorrect;
    }

    private void saveQuestion(Question question) {
        try {
            questionDataSource.saveQuestion(question);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

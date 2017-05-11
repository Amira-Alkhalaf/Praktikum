package swp_impl_acr.quizapy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import swp_impl_acr.quizapy.Adapter.LinkedHashMapAdapter;
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

        LinkedHashMapAdapter adapter = new LinkedHashMapAdapter(this, gameConfig.getQuestionAnswerMap());
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
        LinkedHashMap<Question, Answer> map = gameConfig.getQuestionAnswerMap();
        boolean allQuestionsCorrect=true;
        for(Map.Entry<Question, Answer> entry:map.entrySet()){
            if(!entry.getValue().isCorrectAnswer()){
                allQuestionsCorrect=false;
                entry.getKey().setCorrectly(false);
            } else {
                entry.getKey().setCorrectly(true);
            }
            entry.getKey().setAnswered(true);
            saveQuestion(entry.getKey());
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

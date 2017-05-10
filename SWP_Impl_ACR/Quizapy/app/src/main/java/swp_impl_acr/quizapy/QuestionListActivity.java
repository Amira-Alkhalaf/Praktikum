package swp_impl_acr.quizapy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.LinkedHashMap;

import swp_impl_acr.quizapy.Adapter.LinkedHashMapAdapter;

public class QuestionListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_question_list);

        GameConfig gameConfig = GameConfig.getInstance();

        ListView listView = (ListView)findViewById(R.id.listView);

        LinkedHashMapAdapter adapter = new LinkedHashMapAdapter(this, gameConfig.getQuestionAnswerMap());
        listView.setAdapter(adapter);
    }

}

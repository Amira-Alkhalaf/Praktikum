package swp_impl_acr.quizapy;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import swp_impl_acr.quizapy.Database.Entity.Answer;
import swp_impl_acr.quizapy.Database.Entity.Question;
import swp_impl_acr.quizapy.Database.Entity.Topic;

public class GameConfig {

    private static GameConfig instance;

    private int difficulty;
    private Topic topic;
    private ArrayList<Answer> answers;

    private GameConfig() {
        answers = new ArrayList<>();
    }

    public static GameConfig getInstance () {
        if (GameConfig.instance == null) {
            GameConfig.instance = new GameConfig();
        }
        return GameConfig.instance;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
    }

    public ArrayList<Answer> getAnswers() {
        return answers;
    }

    public void resetInstance(){
        this.difficulty=0;
        this.topic=null;
        this.answers=new ArrayList<>();
    }
}

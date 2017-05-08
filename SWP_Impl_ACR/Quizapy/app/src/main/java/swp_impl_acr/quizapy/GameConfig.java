package swp_impl_acr.quizapy;

import java.util.HashMap;
import java.util.Map;

import swp_impl_acr.quizapy.Database.Entity.Answer;
import swp_impl_acr.quizapy.Database.Entity.Question;
import swp_impl_acr.quizapy.Database.Entity.Topic;

public class GameConfig {

    private static GameConfig instance;

    private int difficulty;
    private Topic topic;
    private Map<Question, Answer> questionAnswerMap;

    private GameConfig() {
        questionAnswerMap = new HashMap<>();
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

    public void addQuestionAnswer(Question question, Answer answer) {
        questionAnswerMap.put(question, answer);
    }

    public Map getQuestionAnswerMap() {
        return questionAnswerMap;
    }
}
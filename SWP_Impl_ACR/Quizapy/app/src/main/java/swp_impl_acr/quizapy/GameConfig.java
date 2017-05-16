package swp_impl_acr.quizapy;

import java.util.ArrayList;

import swp_impl_acr.quizapy.Database.Entity.Answer;
import swp_impl_acr.quizapy.Database.Entity.Topic;

/**
 * holds information crucial to the current game run
 */
public class GameConfig {

    private static GameConfig instance;

    private int difficulty;
    private Topic topic;
    private ArrayList<Answer> answers;

    /**
     * private constructor
     */
    private GameConfig() {
        answers = new ArrayList<>();
    }

    /**
     * @return instance
     */
    public static GameConfig getInstance () {
        if (GameConfig.instance == null) {
            GameConfig.instance = new GameConfig();
        }
        return GameConfig.instance;
    }

    /**
     * returns the set difficulty for the questions
     * @return
     */
    public int getDifficulty() {
        return difficulty;
    }

    /**
     * sets the difficulty for the questions
     * @param difficulty
     */
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * returns the chosen topic
     * @return
     */
    public Topic getTopic() {
        return topic;
    }

    /**
     * sets the chosen topic
     * @param topic
     */
    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    /**
     * adds chosen answer
     * @param answer
     */
    public void addAnswer(Answer answer) {
        answers.add(answer);
    }

    /**
     * returns all chosen answers
     * @return
     */
    public ArrayList<Answer> getAnswers() {
        return answers;
    }

    /**
     * resets instance for a new game run
     */
    public void resetInstance(){
        this.difficulty=0;
        this.topic=null;
        this.answers=new ArrayList<>();
    }
}

package swp_impl_acr.quizapy.Database.Entity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import swp_impl_acr.quizapy.Database.DataSource.QuestionDataSource;
import swp_impl_acr.quizapy.Helper.CollectionUtils;

/**
 * question entity
 */
public class Question {

    private Integer id;
    private Topic topic;
    private String name;
    private int difficulty;
    private boolean answered;
    private boolean correctly;
    private List<Answer> answers;

    /**
     * constructor
     *
     * @param id
     * @param topic
     * @param name
     * @param difficulty
     * @param answered
     * @param correctly
     */
    public Question(int id, Topic topic, String name, int difficulty, boolean answered,
                    boolean correctly) {
        this.id = id;
        this.topic = topic;
        this.name = name;
        this.difficulty = difficulty;
        this.answered = answered;
        this.correctly = correctly;
        answers = new ArrayList<>();
    }

    /**
     * constructor
     */
    public Question() {
        answers = new ArrayList<>();
    }

    /**
     * returns the question id
     * @return
     */
    public Integer getId() {
        return id;
    }

    /**
     * sets the question id
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * returns the topic the question belongs to
     * @return
     */
    public Topic getTopic() {
        return topic;
    }

    /**
     * sets the topic the questions belongs to
     * @param topic
     */
    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    /**
     * returns the question text
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * sets the question text
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * returns the questions difficulty
     * @return
     */
    public int getDifficulty() {
        return difficulty;
    }

    /**
     * sets the questions difficulty
     * @param difficulty
     */
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * returns true if the question has already been answered
     * @return
     */
    public boolean isAnswered() {
        return answered;
    }

    /**
     * sets if the question has already been answered
     * @param answered
     */
    public void setAnswered(boolean answered) {
        this.answered = answered;
    }

    /**
     * returns true if the question was answered correctly
     * @return
     */
    public boolean isCorrectly() {
        return correctly;
    }

    /**
     * sets if the question was answered correctly
     * @param correctly
     */
    public void setCorrectly(boolean correctly) {
        this.correctly = correctly;
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
    }

    /**
     * returns a list of answers belonging to the question
     * @return
     */
    public List<Answer> getAnswers(int count) {
        if(answers.size()==0){
            try {
                QuestionDataSource questionDataSource = new QuestionDataSource();
                this.answers.add(questionDataSource.getCorrectAnswer(this.id));
                List<Answer> tempAnswers = questionDataSource.getWrongAnswers(this.id);
                if(count == -1){
                    this.answers.addAll(tempAnswers);
                } else {
                    tempAnswers = CollectionUtils.generateRandomList(tempAnswers, count-1);
                    this.answers.addAll(tempAnswers);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return answers;
    }
}

package swp_impl_acr.quizapy.Database.Entity;

import java.util.ArrayList;
import java.util.List;

import swp_impl_acr.quizapy.Database.DataSource.AnswerDataSource;
import swp_impl_acr.quizapy.Database.DataSource.QuestionDataSource;

public class Question {

    private int id;
    private Topic topic;
    private String name;
    private int difficulty;
    private boolean answered;
    private boolean correctly;
    private List<Answer> answers;

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

    public Question() {
        answers = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public boolean isAnswered() {
        return answered;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
    }

    public boolean isCorrectly() {
        return correctly;
    }

    public void setCorrectly(boolean correctly) {
        this.correctly = correctly;
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
    }

    public List<Answer> getAnswers() {
        if(answers.size()==0){
            try {
                QuestionDataSource questionDataSource = new QuestionDataSource();
                this.answers = questionDataSource.getAllAnswers(this.id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return answers;
    }
}

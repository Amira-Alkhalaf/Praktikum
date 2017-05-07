package swp_impl_acr.quizapy.Database.Entity;

import java.util.ArrayList;
import java.util.List;

public class Question {

    private int id;
    private int topic;
    private String name;
    private int difficulty;
    private boolean answered;
    private boolean correctly;
    private List<Answer> answers;

    public Question(int id, int topic, String name, int difficulty, boolean answered, boolean correctly) {
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
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTopic() {
        return topic;
    }

    public void setTopic(int topic) {
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

    public void addAnswer(Answer answer){
      answers.add(answer);
    }

    public List<Answer> getAnswers(){
        return answers;
    }
}

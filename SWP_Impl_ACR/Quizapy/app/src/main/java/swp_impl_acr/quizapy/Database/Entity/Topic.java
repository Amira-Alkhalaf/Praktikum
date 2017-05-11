package swp_impl_acr.quizapy.Database.Entity;

import java.util.ArrayList;
import java.util.List;

import swp_impl_acr.quizapy.Database.DataSource.TopicDataSource;

public class Topic {

    private int id;
    private String name;
    private List<Question> questions;

    public Topic(int id, String name) {
        this.id = id;
        this.name = name;
        questions = new ArrayList<>();
    }

    public Topic() {
        questions = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addQuestion(Question question) {
        questions.add(question);
    }

    public List<Question> getQuestions() {
        if(questions.size()==0){
            try {
                TopicDataSource topicDataSource = new TopicDataSource();
                this.questions = topicDataSource.getAllQuestions(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return questions;
    }
}

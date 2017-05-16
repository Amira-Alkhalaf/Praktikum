package swp_impl_acr.quizapy.Database.Entity;

import java.util.ArrayList;
import java.util.List;

import swp_impl_acr.quizapy.Database.DataSource.TopicDataSource;

/**
 * topic entity
 */
public class Topic {

    private int id;
    private String name;
    private List<Question> questions;

    /**
     * constructor
     * @param id
     * @param name
     */
    public Topic(int id, String name) {
        this.id = id;
        this.name = name;
        questions = new ArrayList<>();
    }

    /**
     * constructor
     */
    public Topic() {
        questions = new ArrayList<>();
    }

    /**
     * returns the topic id
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * sets the topic id
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * returns the topic text
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * sets the topic text
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    public void addQuestion(Question question) {
        questions.add(question);
    }

    /**
     * returns a list of questions belonging to the topic(not filtered)
     * @return
     */
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

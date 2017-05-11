package swp_impl_acr.quizapy.Database.Entity;

public class Answer {

    private int id;
    private Question question;
    private String name;
    private boolean correctAnswer;

    public Answer(int id, Question question, String name, boolean correctAnswer) {
        this.id = id;
        this.question = question;
        this.name = name;
        this.correctAnswer = correctAnswer;
    }

    public Answer() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(boolean correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}

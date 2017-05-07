package swp_impl_acr.quizapy.Database.Entity;

public class Answer {

    private int id;
    private int question;
    private String name;
    private boolean correctAnswer;

    public Answer(int id, int question, String name, boolean correctAnswer) {
        this.id = id;
        this.question = question;
        this.name = name;
        this.correctAnswer = correctAnswer;
    }

    public Answer() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuestion() {
        return question;
    }

    public void setQuestion(int question) {
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

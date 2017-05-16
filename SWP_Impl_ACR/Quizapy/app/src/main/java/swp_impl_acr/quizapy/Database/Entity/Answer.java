package swp_impl_acr.quizapy.Database.Entity;

/**
 * answer entity
 */
public class Answer {

    private int id;
    private Question question;
    private String name;
    private boolean correctAnswer;

    /**
     * constructor
     *
     * @param id
     * @param question
     * @param name
     * @param correctAnswer
     */
    public Answer(int id, Question question, String name, boolean correctAnswer) {
        this.id = id;
        this.question = question;
        this.name = name;
        this.correctAnswer = correctAnswer;
    }

    /**
     * constructor
     */
    public Answer() {
    }

    /**
     * returns the answer id
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * sets the answer id
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * returns the Question the answer belongs to
     * @return
     */
    public Question getQuestion() {
        return question;
    }

    /**
     * sets the question the answer belongs to
     * @param question
     */
    public void setQuestion(Question question) {
        this.question = question;
    }

    /**
     * returns the answer text
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * sets the answer text
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * returns true if it's the correct answer
     * @return
     */
    public boolean isCorrectAnswer() {
        return correctAnswer;
    }

    /**
     * sets if it's the correct answer
     * @param correctAnswer
     */
    public void setCorrectAnswer(boolean correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}

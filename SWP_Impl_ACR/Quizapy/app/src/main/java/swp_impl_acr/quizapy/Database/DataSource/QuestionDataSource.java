package swp_impl_acr.quizapy.Database.DataSource;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import swp_impl_acr.quizapy.Database.Entity.Answer;
import swp_impl_acr.quizapy.Database.Entity.Question;
import swp_impl_acr.quizapy.Database.Entity.Topic;
import swp_impl_acr.quizapy.Database.QuizapyContract;
import swp_impl_acr.quizapy.Database.QuizapyDataSource;

/**
 * class to handle question related queries
 */
public class QuestionDataSource {

    private SQLiteDatabase db;
    private QuizapyDataSource instance;

    private String[] columns = {
            QuizapyContract.QuestionTable._ID,
            QuizapyContract.QuestionTable.COLUMN_TOPIC,
            QuizapyContract.QuestionTable.COLUMN_NAME,
            QuizapyContract.QuestionTable.COLUMN_DIFFICULTY,
            QuizapyContract.QuestionTable.COLUMN_ANSWERED,
            QuizapyContract.QuestionTable.COLUMN_CORRECTLY,
    };

    /**
     * constructor
     * @throws Exception
     */
    public QuestionDataSource() throws Exception {
        this.instance = QuizapyDataSource.getInstance();
        this.db = instance.getConnection();
    }

    /**
     * adds question to the database if it doesn't already exist
     * otherwise updates it
     *
     * @param question
     * @throws SQLException
     */
    public void saveQuestion(Question question) throws SQLException {
        if (instance.checkIfDataExistsInDb(QuizapyContract.QuestionTable.TABLE_NAME,
                QuizapyContract.QuestionTable._ID,
                Integer.toString(question.getId()))) {
            updateQuestion(question);
        } else {
            addQuestion(question);
        }
    }

    /**
     * returns question with id
     * @param id
     * @return
     * @throws SQLException
     */
    public Question getQuestionById(final int id) throws SQLException {
        Cursor cursor = db.query(QuizapyContract.QuestionTable.TABLE_NAME,
                null,
                QuizapyContract.QuestionTable._ID + " = ?",
                new String[]{Integer.toString(id)}, null, null, null);

        cursor.moveToFirst();
        Question question = createQuestion(cursor);

        cursor.close();
        return question;
    }

    /**
     * adds question to database
     * @param question
     * @throws SQLException
     */
    private void addQuestion(Question question) throws SQLException {
        ContentValues contentValues = new ContentValues();
        contentValues.put(QuizapyContract.QuestionTable._ID, question.getId());
        contentValues.put(QuizapyContract.QuestionTable.COLUMN_TOPIC, question.getTopic().getId());
        contentValues.put(QuizapyContract.QuestionTable.COLUMN_NAME, question.getName());
        contentValues.put(QuizapyContract.QuestionTable.COLUMN_DIFFICULTY, question.getDifficulty());
        contentValues.put(QuizapyContract.QuestionTable.COLUMN_ANSWERED, 0);
        contentValues.put(QuizapyContract.QuestionTable.COLUMN_CORRECTLY, -1);
        db.insertOrThrow(QuizapyContract.QuestionTable.TABLE_NAME, null, contentValues);
    }

    /**
     * deletes question with id
     * @param id
     * @throws SQLException
     */
    public void deleteQuestion(final int id) throws SQLException {
        db.delete(QuizapyContract.QuestionTable.TABLE_NAME,
                QuizapyContract.QuestionTable._ID + " = ?",
                new String[]{Integer.toString(id)});
    }

    /**
     * returns a list with all questions in the database
     * @return
     * @throws SQLException
     */
    public List<Question> getAllQuestions() throws SQLException {
        List<Question> questions = new ArrayList<>();

        Cursor cursor = db.query(QuizapyContract.QuestionTable.TABLE_NAME, columns, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                questions.add(createQuestion(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return questions;
    }

    /**
     * returns the number of all questions in the database
     * @return
     * @throws SQLException
     */
    public int getAllQuestionsCount() throws SQLException {
        return db.query(QuizapyContract.QuestionTable.TABLE_NAME, columns, null, null, null, null, null).getCount();
    }

    /**
     * updates question
     * @param question
     * @throws SQLException
     */
    private void updateQuestion(Question question) throws SQLException {
        ContentValues contentValues = new ContentValues();

        int id = question.getId();
        Topic topic = question.getTopic();
        String name = question.getName();
        int difficulty = question.getDifficulty();
        int intValueAnswered = (question.isAnswered()) ? 1 : 0;
        int intValueCorrectly = (question.isCorrectly()) ? 1 : 0;

        contentValues.put(QuizapyContract.QuestionTable.COLUMN_TOPIC, topic.getId());
        contentValues.put(QuizapyContract.QuestionTable.COLUMN_NAME, name);
        contentValues.put(QuizapyContract.QuestionTable.COLUMN_DIFFICULTY, difficulty);
        contentValues.put(QuizapyContract.QuestionTable.COLUMN_ANSWERED, intValueAnswered);
        contentValues.put(QuizapyContract.QuestionTable.COLUMN_CORRECTLY, intValueCorrectly);

        db.update(QuizapyContract.QuestionTable.TABLE_NAME,
                contentValues,
                QuizapyContract.QuestionTable._ID + " = ?",
                new String[]{Integer.toString(id)});
    }

    /**
     * returns the correctAnswer for specific question(id)
     * @param id
     * @return
     * @throws SQLException
     */
    public Answer getCorrectAnswer(final int id) throws SQLException {
        Cursor cursor = db.query(QuizapyContract.AnswerTable.TABLE_NAME,
                null,
                QuizapyContract.AnswerTable.COLUMN_QUESTION + " = ? AND " + QuizapyContract.AnswerTable.COLUMN_CORRECT_ANSWER + " = 1",
                new String[]{Integer.toString(id)}, null, null, null);

        cursor.moveToFirst();
        Answer answer = createAnswer(cursor);

        cursor.close();
        return answer;
    }

    /**
     * returns all wrong answer for specific question(id)
     * @param id
     * @return
     * @throws SQLException
     */
    public List<Answer> getWrongAnswers(final int id) throws SQLException {
        List<Answer> answers = new ArrayList<>();

        Cursor cursor = db.query(QuizapyContract.AnswerTable.TABLE_NAME,
                null,
                QuizapyContract.AnswerTable.COLUMN_QUESTION + " = ? AND " + QuizapyContract.AnswerTable.COLUMN_CORRECT_ANSWER + " = 0",
                new String[]{Integer.toString(id)}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                answers.add(createAnswer(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return answers;
    }

    /**
     * returns all answers for specific question(id)
     * @param id
     * @return
     * @throws SQLException
     */
    public List<Answer> getAllAnswers(final int id) throws SQLException {
        List<Answer> answers = new ArrayList<>();

        Cursor cursor = db.query(QuizapyContract.AnswerTable.TABLE_NAME,
                null,
                QuizapyContract.AnswerTable.COLUMN_QUESTION + " = ?",
                new String[]{Integer.toString(id)}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                answers.add(createAnswer(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return answers;
    }

    /**
     * returns all unanswered questions
     * @return
     * @throws SQLException
     */
    public List<Question> getAllUnansweredQuestions() throws SQLException {
        List<Question> questions = new ArrayList<>();

        Cursor cursor = db.query(QuizapyContract.QuestionTable.TABLE_NAME,
                columns,
                QuizapyContract.QuestionTable.COLUMN_ANSWERED + " = 0",
                null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                questions.add(createQuestion(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return questions;
    }

    /**
     * returns all answered questions
     * @return
     * @throws SQLException
     */
    public List<Question> getAllAnsweredQuestions() throws SQLException {
        List<Question> questions = new ArrayList<>();

        Cursor cursor = db.query(QuizapyContract.QuestionTable.TABLE_NAME,
                columns,
                QuizapyContract.QuestionTable.COLUMN_ANSWERED + " = 1",
                null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                questions.add(createQuestion(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return questions;
    }

    /**
     * returns the count of all unanswered questions
     * @return
     * @throws SQLException
     */
    public int getAllUnansweredQuestionsCount() throws SQLException {
        return db.query(QuizapyContract.QuestionTable.TABLE_NAME,
                columns,
                QuizapyContract.QuestionTable.COLUMN_ANSWERED + " = 0",
                null, null, null, null).getCount();
    }

    /**
     * returns the count of all answered questions
     * @return
     * @throws SQLException
     */
    public int getAllAnsweredQuestionsCount() throws SQLException {
        return db.query(QuizapyContract.QuestionTable.TABLE_NAME,
                columns,
                QuizapyContract.QuestionTable.COLUMN_ANSWERED + " = 1",
                null, null, null, null).getCount();
    }

    /**
     * builds and returns a question object
     * @param cursor
     * @return
     */
    @NonNull
    private Question createQuestion(Cursor cursor) {
        Question question = new Question();
        question.setId(cursor.getInt(cursor.getColumnIndex(QuizapyContract.QuestionTable._ID)));
        try {
            TopicDataSource topicDataSource = new TopicDataSource();
            question.setTopic(topicDataSource.getTopicById(cursor.getInt(cursor.getColumnIndex(QuizapyContract.QuestionTable.COLUMN_TOPIC))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        question.setName(cursor.getString(cursor.getColumnIndex(QuizapyContract.QuestionTable.COLUMN_NAME)));
        question.setDifficulty(cursor.getInt(cursor.getColumnIndex(QuizapyContract.QuestionTable.COLUMN_DIFFICULTY)));
        int intValueAnswered = cursor.getInt(cursor.getColumnIndex(QuizapyContract.QuestionTable.COLUMN_ANSWERED));
        boolean isAnswered = (intValueAnswered == 1);
        question.setAnswered(isAnswered);
        int intValueCorrectly = cursor.getInt(cursor.getColumnIndex(QuizapyContract.QuestionTable.COLUMN_CORRECTLY));
        boolean isCorrectly = (intValueCorrectly == 1);
        question.setCorrectly(isAnswered);
        return question;
    }

    /**
     * build and returns an answer object
     * @param cursor
     * @return
     */
    @NonNull
    private Answer createAnswer(Cursor cursor) {
        Answer answer = new Answer();
        answer.setId(cursor.getInt(cursor.getColumnIndex(QuizapyContract.AnswerTable._ID)));
        try {
            QuestionDataSource questionDataSource = new QuestionDataSource();
            answer.setQuestion(questionDataSource.getQuestionById(cursor.getInt(cursor.getColumnIndex(QuizapyContract.AnswerTable.COLUMN_QUESTION))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        answer.setName(cursor.getString(cursor.getColumnIndex(QuizapyContract.AnswerTable.COLUMN_NAME)));
        int intValueCorrectAnswer = cursor.getInt(cursor.getColumnIndex(QuizapyContract.AnswerTable.COLUMN_CORRECT_ANSWER));
        boolean isCorrectAnswer = (intValueCorrectAnswer == 1);
        answer.setCorrectAnswer(isCorrectAnswer);
        return answer;
    }
}


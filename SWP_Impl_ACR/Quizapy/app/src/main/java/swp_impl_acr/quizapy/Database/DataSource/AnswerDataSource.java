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
import swp_impl_acr.quizapy.Database.QuizapyContract;
import swp_impl_acr.quizapy.Database.QuizapyDataSource;

/**
 * class to handle answer related queries
 */
public class AnswerDataSource {

    private SQLiteDatabase db;
    private QuizapyDataSource instance;

    private String[] columns = {
            QuizapyContract.AnswerTable._ID,
            QuizapyContract.AnswerTable.COLUMN_QUESTION,
            QuizapyContract.AnswerTable.COLUMN_NAME,
            QuizapyContract.AnswerTable.COLUMN_CORRECT_ANSWER,
    };

    /**
     * constructor
     * @throws Exception
     */
    public AnswerDataSource() throws Exception {
        this.instance = QuizapyDataSource.getInstance();
        this.db = instance.getConnection();
    }

    /**
     * adds answer to the database if it doesn't already exist
     * otherwise updates it
     *
     * @param answer
     * @throws SQLException
     */
    public void saveAnswer(Answer answer) throws SQLException {
        if (instance.checkIfDataExistsInDb(QuizapyContract.AnswerTable.TABLE_NAME,
                QuizapyContract.AnswerTable._ID,
                Integer.toString(answer.getId()))) {
            updateAnswer(answer);
        } else {
            addAnswer(answer);
        }
    }

    /**
     * adds an answer to the database
     * @param answer
     * @throws SQLException
     */
    private void addAnswer(Answer answer) throws SQLException {
        ContentValues contentValues = new ContentValues();
        contentValues.put(QuizapyContract.AnswerTable._ID, answer.getId());
        contentValues.put(QuizapyContract.AnswerTable.COLUMN_QUESTION, answer.getQuestion().getId());
        contentValues.put(QuizapyContract.AnswerTable.COLUMN_NAME, answer.getName());
        int intValueCorrectAnswer = (answer.isCorrectAnswer()) ? 1 : 0;
        contentValues.put(QuizapyContract.AnswerTable.COLUMN_CORRECT_ANSWER, intValueCorrectAnswer);
        db.insertOrThrow(QuizapyContract.AnswerTable.TABLE_NAME, null, contentValues);
    }

    /**
     * deleted answer with id from the database
     * @param id
     * @throws SQLException
     */
    public void deleteAnswer(final int id) throws SQLException {
        db.delete(QuizapyContract.AnswerTable.TABLE_NAME,
                QuizapyContract.AnswerTable._ID + " = ?",
                new String[]{Integer.toString(id)});
    }

    /**
     * returns answer with id
     * @param id
     * @return
     * @throws SQLException
     */
    public Answer getAnswerById(final int id) throws SQLException {
        Cursor cursor = db.query(QuizapyContract.AnswerTable.TABLE_NAME,
                null,
                QuizapyContract.AnswerTable._ID + " = ?",
                new String[]{Integer.toString(id)}, null, null, null);

        cursor.moveToFirst();
        Answer answer = createAnswer(cursor);

        cursor.close();
        return answer;
    }

    /**
     * returns a list of all answers in the database
     * @return
     * @throws SQLException
     */
    public List<Answer> getAllAnswers() throws SQLException {
        List<Answer> answers = new ArrayList<>();

        Cursor cursor = db.query(QuizapyContract.AnswerTable.TABLE_NAME, columns, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Answer answer = createAnswer(cursor);
                answers.add(answer);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return answers;
    }

    /**
     * returns the count of all answers in the database
     * @return
     * @throws SQLException
     */
    public int getAllAnswersCount() throws SQLException {
        return db.query(QuizapyContract.AnswerTable.TABLE_NAME, columns, null, null, null, null, null).getCount();
    }

    /**
     * updates an answer in the database
     * @param answer
     * @throws SQLException
     */
    private void updateAnswer(Answer answer) throws SQLException {
        ContentValues contentValues = new ContentValues();

        int id = answer.getId();
        Question question = answer.getQuestion();
        String name = answer.getName();
        int intValueCorrectAnswer = (answer.isCorrectAnswer()) ? 1 : 0;

        contentValues.put(QuizapyContract.AnswerTable.COLUMN_QUESTION, question.getId());
        contentValues.put(QuizapyContract.AnswerTable.COLUMN_NAME, name);
        contentValues.put(QuizapyContract.AnswerTable.COLUMN_CORRECT_ANSWER, intValueCorrectAnswer);

        db.update(QuizapyContract.AnswerTable.TABLE_NAME,
                contentValues,
                QuizapyContract.AnswerTable._ID + " = ?",
                new String[]{Integer.toString(id)});
    }

    /**
     * builds an Answer object and returns it
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
        boolean isCorrectAnswer = (intValueCorrectAnswer != 0);
        answer.setCorrectAnswer(isCorrectAnswer);
        return answer;
    }
}

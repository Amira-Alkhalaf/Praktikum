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

public class AnswerDataSource {

    private SQLiteDatabase db;
    private QuizapyDataSource instance;

    private String[] columns = {
            QuizapyContract.AnswerTable._ID,
            QuizapyContract.AnswerTable.COLUMN_QUESTION,
            QuizapyContract.AnswerTable.COLUMN_NAME,
            QuizapyContract.AnswerTable.COLUMN_CORRECT_ANSWER,
    };

    public AnswerDataSource() throws Exception {
        this.instance = QuizapyDataSource.getInstance();
        this.db = instance.getConnection();
    }

    public void saveAnswer(Answer answer) throws SQLException {
        if (instance.checkIfDataExistsInDb(QuizapyContract.AnswerTable.TABLE_NAME,
                QuizapyContract.AnswerTable._ID,
                Integer.toString(answer.getId()))) {
            updateAnswer(answer);
        } else {
            addAnswer(answer);
        }
    }

    private void addAnswer(Answer answer) throws SQLException {
        ContentValues contentValues = new ContentValues();
        contentValues.put(QuizapyContract.AnswerTable._ID, answer.getId());
        contentValues.put(QuizapyContract.AnswerTable.COLUMN_QUESTION, answer.getQuestion().getId());
        contentValues.put(QuizapyContract.AnswerTable.COLUMN_NAME, answer.getName());
        int intValueCorrectAnswer = (answer.isCorrectAnswer()) ? 1 : 0;
        contentValues.put(QuizapyContract.AnswerTable.COLUMN_CORRECT_ANSWER, intValueCorrectAnswer);
        db.insertOrThrow(QuizapyContract.AnswerTable.TABLE_NAME, null, contentValues);
    }

    public void deleteAnswer(final int id) throws SQLException {
        db.delete(QuizapyContract.AnswerTable.TABLE_NAME,
                QuizapyContract.AnswerTable._ID + " = ?",
                new String[]{Integer.toString(id)});
    }

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

    public int getAllAnswersCount() throws SQLException {
        return db.query(QuizapyContract.AnswerTable.TABLE_NAME, columns, null, null, null, null, null).getCount();
    }

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

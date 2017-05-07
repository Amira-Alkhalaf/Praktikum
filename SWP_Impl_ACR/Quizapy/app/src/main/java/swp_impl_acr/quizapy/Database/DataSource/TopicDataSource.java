package swp_impl_acr.quizapy.Database.DataSource;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import swp_impl_acr.quizapy.Database.Entity.Topic;
import swp_impl_acr.quizapy.Database.Entity.Question;
import swp_impl_acr.quizapy.Database.QuizapyContract;
import swp_impl_acr.quizapy.Database.QuizapyDataSource;

public class TopicDataSource {

    private SQLiteDatabase db;
    private QuizapyDataSource instance;

    private String[] columns = {
            QuizapyContract.TopicTable._ID,
            QuizapyContract.TopicTable.COLUMN_NAME,
    };

    public TopicDataSource() throws Exception {
        this.instance=QuizapyDataSource.getInstance();
        this.db= instance.getConnection();
    }

    public void saveTopic(Topic topic) throws SQLException {
        if(instance.checkIfDataExistsInDb(QuizapyContract.TopicTable.TABLE_NAME, QuizapyContract.TopicTable._ID, Integer.toString(topic.getId()))) {
            updateTopic(topic);
        } else {
            addTopic(topic);
        }
    }

    private void addTopic(Topic topic) throws SQLException {
        ContentValues contentValues = new ContentValues();
        contentValues.put(QuizapyContract.TopicTable._ID, topic.getId());
        contentValues.put(QuizapyContract.TopicTable.COLUMN_NAME, topic.getName());
        db.insertOrThrow(QuizapyContract.TopicTable.TABLE_NAME, null, contentValues);
    }

    public void deleteTopic(final int id) throws SQLException {
        db.delete(QuizapyContract.TopicTable.TABLE_NAME, QuizapyContract.TopicTable._ID +" = ?", new String[]{Integer.toString(id)});
    }

    public List<Topic> getAllTopics() throws SQLException {
        List<Topic> topics = new ArrayList<>();

        Cursor cursor = db.query(QuizapyContract.TopicTable.TABLE_NAME, columns, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                topics.add(createTopic(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return topics;
    }

    public int getAllTopicsCount() throws SQLException {
        return db.query(QuizapyContract.TopicTable.TABLE_NAME, columns, null, null, null, null, null).getCount();
    }

    private void updateTopic(Topic topic) throws SQLException {
        ContentValues contentValues = new ContentValues();

        int id = topic.getId();
        String name = topic.getName();

        contentValues.put(QuizapyContract.TopicTable.COLUMN_NAME, name);

        db.update(QuizapyContract.TopicTable.TABLE_NAME, contentValues, QuizapyContract.TopicTable._ID + " = ?", new String[]{Integer.toString(id)});
    }

    public List<Question> getAllUnansweredQuestionsByDifficulty(final int id, final int difficulty) throws SQLException {
        List<Question> questions = new ArrayList<>();

        Cursor cursor = db.query(QuizapyContract.QuestionTable.TABLE_NAME, null, QuizapyContract.QuestionTable.COLUMN_TOPIC + " = ? AND " + QuizapyContract.QuestionTable.COLUMN_DIFFICULTY + " = ? AND " + QuizapyContract.QuestionTable.COLUMN_ANSWERED + " = 0" , new String[]{Integer.toString(id), Integer.toString(difficulty)}, null, null, null);

        cursor.moveToFirst();
        Question question = createQuestion(cursor);

        cursor.close();
        return questions;
    }

    public int getAllUnansweredQuestionsByDifficultyCount(final int id, final int difficulty) throws SQLException {
        return db.query(QuizapyContract.QuestionTable.TABLE_NAME, null, QuizapyContract.QuestionTable.COLUMN_TOPIC + " = ? AND " + QuizapyContract.QuestionTable.COLUMN_DIFFICULTY + " = ? AND " + QuizapyContract.QuestionTable.COLUMN_ANSWERED + " = 0" , new String[]{Integer.toString(id), Integer.toString(difficulty)}, null, null, null).getCount();
    }

    public List<Topic> getChoosableTopics() throws SQLException {
        List<Topic> topics = getAllTopics();
        List<Topic> topicsFiltered = new ArrayList<>();
        for(Topic topic:topics){
            boolean hasEnoughQuestions = false;
            for(int i = 1; i<=3;i++){
                if(getAllUnansweredQuestionsByDifficultyCount(topic.getId(),i) >= 10){
                    hasEnoughQuestions = true;
                }
            }
            if(hasEnoughQuestions) {
                topicsFiltered.add(topic);
            }
        }
        return topicsFiltered;
    }

    @NonNull
    private Question createQuestion(Cursor cursor) {
        Question question = new Question();
        question.setId(cursor.getInt(cursor.getColumnIndex(QuizapyContract.QuestionTable._ID)));
        question.setTopic(cursor.getInt(cursor.getColumnIndex(QuizapyContract.QuestionTable.COLUMN_TOPIC)));
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

    @NonNull
    private Topic createTopic(Cursor cursor) {
        Topic topic = new Topic();
        topic.setId(cursor.getInt(cursor.getColumnIndex(QuizapyContract.TopicTable._ID)));
        topic.setName(cursor.getString(cursor.getColumnIndex(QuizapyContract.TopicTable.COLUMN_NAME)));
        return topic;
    }
}
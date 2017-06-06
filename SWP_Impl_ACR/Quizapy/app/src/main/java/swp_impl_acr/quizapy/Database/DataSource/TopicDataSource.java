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

/**
 * class to handle topic related queries
 */
public class TopicDataSource {

    private SQLiteDatabase db;
    private QuizapyDataSource instance;

    private String[] columns = {
            QuizapyContract.TopicTable._ID,
            QuizapyContract.TopicTable.COLUMN_NAME,
    };

    /**
     * constructor
     * @throws Exception
     */
    public TopicDataSource() throws Exception {
        this.instance = QuizapyDataSource.getInstance();
        this.db = instance.getConnection();
    }

    /**
     * adds topic to the database if it doesn't already exist
     * otherwise updates it
     * @param topic
     * @throws SQLException
     */
    public Topic saveTopic(Topic topic) throws SQLException {
        if (topic.getId() != null && instance.checkIfDataExistsInDb(QuizapyContract.TopicTable.TABLE_NAME,
                QuizapyContract.TopicTable._ID,
                Integer.toString(topic.getId()))) {
            updateTopic(topic);
            return topic;
        } else {
            return addTopic(topic);

        }
    }

    /**
     * adds topic to the database
     * @param topic
     * @throws SQLException
     */
    private Topic addTopic(Topic topic) throws SQLException {
        ContentValues contentValues = new ContentValues();
        if(topic.getId() != null){
            contentValues.put(QuizapyContract.TopicTable._ID, topic.getId());
        }
        contentValues.put(QuizapyContract.TopicTable.COLUMN_NAME, topic.getName());
        topic.setId((int)db.insertOrThrow(QuizapyContract.TopicTable.TABLE_NAME, null, contentValues));
        return topic;
    }

    /**
     * deletes topic from the database
     * @param id
     * @throws SQLException
     */
    public void deleteTopic(final int id) throws SQLException {
        db.delete(QuizapyContract.TopicTable.TABLE_NAME,
                QuizapyContract.TopicTable._ID + " = ?",
                new String[]{Integer.toString(id)});
    }

    /**
     * returns topic with id
     * @param id
     * @return
     * @throws SQLException
     */
    public Topic getTopicById(final int id) throws SQLException {
        Cursor cursor = db.query(QuizapyContract.TopicTable.TABLE_NAME,
                null,
                QuizapyContract.TopicTable._ID + " = ?",
                new String[]{Integer.toString(id)}, null, null, null);

        cursor.moveToFirst();
        Topic topic = createTopic(cursor);

        cursor.close();
        return topic;
    }

    /**
     * returns a list of all topics
     * @return
     * @throws SQLException
     */
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

    /**
     * returns the count of all topics
     * @return
     * @throws SQLException
     */
    public int getAllTopicsCount() throws SQLException {
        return db.query(QuizapyContract.TopicTable.TABLE_NAME, columns, null, null, null, null, null).getCount();
    }

    /**
     * updates topic in the database
     * @param topic
     * @throws SQLException
     */
    private void updateTopic(Topic topic) throws SQLException {
        ContentValues contentValues = new ContentValues();

        int id = topic.getId();
        String name = topic.getName();

        contentValues.put(QuizapyContract.TopicTable.COLUMN_NAME, name);

        db.update(QuizapyContract.TopicTable.TABLE_NAME,
                contentValues,
                QuizapyContract.TopicTable._ID + " = ?",
                new String[]{Integer.toString(id)});
    }

    /**
     * returns a list of all unanswered questions for a specific topic and difficulty
     * @param id
     * @param difficulty
     * @return
     * @throws SQLException
     */
    public List<Question> getAllUnansweredQuestionsByDifficulty(final int id, final int difficulty) throws SQLException {
        List<Question> questions = new ArrayList<>();

        Cursor cursor = db.query(QuizapyContract.QuestionTable.TABLE_NAME,
                null,
                QuizapyContract.QuestionTable.COLUMN_TOPIC + " = ? " +
                        "AND " + QuizapyContract.QuestionTable.COLUMN_DIFFICULTY + " = ? " +
                        "AND " + QuizapyContract.QuestionTable.COLUMN_ANSWERED + " = 0",
                new String[]{Integer.toString(id), Integer.toString(difficulty)}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                questions.add(createQuestion(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return questions;
    }

    /**
     * returns the number of all unanswered questions for a specific topic(id) and difficulty
     * @param id
     * @param difficulty
     * @return
     * @throws SQLException
     */
    public int getAllUnansweredQuestionsByDifficultyCount(final int id, final int difficulty) throws SQLException {
        return db.query(QuizapyContract.QuestionTable.TABLE_NAME,
                null, QuizapyContract.QuestionTable.COLUMN_TOPIC + " = ? " +
                        "AND " + QuizapyContract.QuestionTable.COLUMN_DIFFICULTY + " = ? " +
                        "AND " + QuizapyContract.QuestionTable.COLUMN_ANSWERED + " = 0",
                new String[]{Integer.toString(id), Integer.toString(difficulty)}, null, null, null).getCount();
    }

    /**
     * returns all topics which have more than amount unanswered questions in a difficulty
     * @return
     * @throws SQLException
     */
    public List<Topic> getChoosableTopics(int amount) throws SQLException {
        List<Topic> topics = getAllTopics();
        List<Topic> topicsFiltered = new ArrayList<>();
        for (Topic topic : topics) {
            boolean hasEnoughQuestions = false;
            for (int i = 1; i <= 3; i++) {
                if (getAllUnansweredQuestionsByDifficultyCount(topic.getId(), i) >= amount) {
                    hasEnoughQuestions = true;
                }
            }
            if (hasEnoughQuestions) {
                topicsFiltered.add(topic);
            }
        }
        return topicsFiltered;
    }
    /**
     * returns all topics which have more than amount unanswered questions in a difficulty not higher than available points
     * @return
     * @throws SQLException
     */
    public List<Topic> getChoosableTopics(int maxPoints, int amount) throws SQLException {
        int j=3;
        if(maxPoints<=j){
            j=maxPoints;
        }
        List<Topic> topics = getAllTopics();
        List<Topic> topicsFiltered = new ArrayList<>();
        for (Topic topic : topics) {
            boolean hasEnoughQuestions = false;
            for (int i = 1; i <= j; i++) {
                if (getAllUnansweredQuestionsByDifficultyCount(topic.getId(), i) >= amount) {
                    hasEnoughQuestions = true;
                }
            }
            if (hasEnoughQuestions) {
                topicsFiltered.add(topic);
            }
        }
        return topicsFiltered;
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
     * builds ans returns a topic object
     * @param cursor
     * @return
     */
    @NonNull
    private Topic createTopic(Cursor cursor) {
        Topic topic = new Topic();
        topic.setId(cursor.getInt(cursor.getColumnIndex(QuizapyContract.TopicTable._ID)));
        topic.setName(cursor.getString(cursor.getColumnIndex(QuizapyContract.TopicTable.COLUMN_NAME)));
        return topic;
    }

    /**
     * returns all questions from a specific topic
     * @param id
     * @return
     * @throws SQLException
     */
    public List<Question> getAllQuestions(final int id) throws SQLException  {
        List<Question> questions = new ArrayList<>();

        Cursor cursor = db.query(QuizapyContract.QuestionTable.TABLE_NAME,
                null,
                QuizapyContract.QuestionTable.COLUMN_TOPIC + " = ? ",
                new String[]{Integer.toString(id)}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                questions.add(createQuestion(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return questions;
    }
}
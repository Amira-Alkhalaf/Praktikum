package swp_impl_acr.quizapy.Database;

import android.provider.BaseColumns;

/**
 * class containing the database schema
 */
public class QuizapyContract {

    public static final int DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "quizapy.db";
    public static final String TEXT_TYPE = " TEXT ";
    public static final String INTEGER_TYPE = " INTEGER ";
    public static final String BOOLEAN_TYPE = " BOOLEAN ";
    public static final String COMMA_SEP = " , ";

    /**
     * private constructor
     */
    private QuizapyContract() {
    }

    /**
     * topic table schema
     */
    public static class TopicTable implements BaseColumns {
        public static final String TABLE_NAME = "topic";
        public static final String COLUMN_NAME = "name";

        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " ( " +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT  NOT NULL " + COMMA_SEP +
                COLUMN_NAME + TEXT_TYPE + " );";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    /**
     * question table schema
     */
    public static class QuestionTable implements BaseColumns {
        public static final String TABLE_NAME = "question";
        public static final String COLUMN_TOPIC = "topic";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DIFFICULTY = "difficulty";
        public static final String COLUMN_ANSWERED = "answered";
        public static final String COLUMN_CORRECTLY = "correctly";

        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " ( " +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL " + COMMA_SEP +
                COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
                COLUMN_DIFFICULTY + INTEGER_TYPE + COMMA_SEP +
                COLUMN_ANSWERED + BOOLEAN_TYPE + COMMA_SEP +
                COLUMN_CORRECTLY + BOOLEAN_TYPE + COMMA_SEP +
                COLUMN_TOPIC + " INTEGER,"
                + " FOREIGN KEY (" + COLUMN_TOPIC + ") REFERENCES " + TopicTable.TABLE_NAME + "(" + TopicTable._ID + "));";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    /**
     * answer table schema
     */
    public static class AnswerTable implements BaseColumns {
        public static final String TABLE_NAME = "answer";
        public static final String COLUMN_QUESTION = "question";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_CORRECT_ANSWER = "correctAnswer";

        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " ( " +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL " + COMMA_SEP +
                COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
                COLUMN_CORRECT_ANSWER + BOOLEAN_TYPE + COMMA_SEP +
                COLUMN_QUESTION + INTEGER_TYPE + COMMA_SEP +
                " FOREIGN KEY (" + COLUMN_QUESTION + ") REFERENCES " + QuestionTable.TABLE_NAME + "(" + QuestionTable._ID + "));";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    /**
     * options table
     */
    public static class OptionTable implements BaseColumns {
        public static final String TABLE_NAME = "option";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_VALUE = "value";

        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " ( " +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL " + COMMA_SEP +
                COLUMN_NAME + TEXT_TYPE + " UNIQUE " + COMMA_SEP +
                COLUMN_VALUE + TEXT_TYPE + ");";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}

package swp_impl_acr.quizapy.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * creates and updates Database
 */
public class QuizapyHelper extends SQLiteOpenHelper {

    private final String TAG = QuizapyHelper.class.getSimpleName();

    /**
     * constructor
     * @param context
     */
    public QuizapyHelper(Context context) {
        super(context, QuizapyContract.DATABASE_NAME, null, QuizapyContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(QuizapyContract.TopicTable.CREATE_TABLE);
        Log.d(TAG, QuizapyContract.TopicTable.CREATE_TABLE);

        db.execSQL(QuizapyContract.QuestionTable.CREATE_TABLE);
        Log.d(TAG, QuizapyContract.QuestionTable.CREATE_TABLE);

        db.execSQL(QuizapyContract.AnswerTable.CREATE_TABLE);
        Log.d(TAG, QuizapyContract.AnswerTable.CREATE_TABLE);

        db.execSQL(QuizapyContract.StuffTable.CREATE_TABLE);
        Log.d(TAG, QuizapyContract.StuffTable.CREATE_TABLE);
        ContentValues contentValues = new ContentValues();
        contentValues.put(QuizapyContract.StuffTable._ID, 1);
        contentValues.put(QuizapyContract.StuffTable.COLUMN_NAME, "points_in_save");
        contentValues.put(QuizapyContract.StuffTable.COLUMN_VALUE, "0");
        db.insert(QuizapyContract.StuffTable.TABLE_NAME, null, contentValues);
        addOptions(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        int version = oldVersion;
        switch(version){
            case 1:
                addOptions(db);
                version++;
            default: break;
        }
    }

    private void addOptions(SQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(QuizapyContract.StuffTable._ID, 2);
        contentValues.put(QuizapyContract.StuffTable.COLUMN_NAME, "questions_in_sequence");
        contentValues.put(QuizapyContract.StuffTable.COLUMN_VALUE, "10");
        db.insert(QuizapyContract.StuffTable.TABLE_NAME, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put(QuizapyContract.StuffTable._ID, 3);
        contentValues.put(QuizapyContract.StuffTable.COLUMN_NAME, "frequency_mode_1");
        contentValues.put(QuizapyContract.StuffTable.COLUMN_VALUE, "20");
        db.insert(QuizapyContract.StuffTable.TABLE_NAME, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put(QuizapyContract.StuffTable._ID, 4);
        contentValues.put(QuizapyContract.StuffTable.COLUMN_NAME, "frequency_mode_2");
        contentValues.put(QuizapyContract.StuffTable.COLUMN_VALUE, "20");
        db.insert(QuizapyContract.StuffTable.TABLE_NAME, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put(QuizapyContract.StuffTable._ID, 5);
        contentValues.put(QuizapyContract.StuffTable.COLUMN_NAME, "frequency_mode_3");
        contentValues.put(QuizapyContract.StuffTable.COLUMN_VALUE, "20");
        db.insert(QuizapyContract.StuffTable.TABLE_NAME, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put(QuizapyContract.StuffTable._ID, 6);
        contentValues.put(QuizapyContract.StuffTable.COLUMN_NAME, "frequency_mode_4");
        contentValues.put(QuizapyContract.StuffTable.COLUMN_VALUE, "20");
        db.insert(QuizapyContract.StuffTable.TABLE_NAME, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put(QuizapyContract.StuffTable._ID, 7);
        contentValues.put(QuizapyContract.StuffTable.COLUMN_NAME, "frequency_mode_5");
        contentValues.put(QuizapyContract.StuffTable.COLUMN_VALUE, "20");
        db.insert(QuizapyContract.StuffTable.TABLE_NAME, null, contentValues);
    }
}

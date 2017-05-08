package swp_impl_acr.quizapy.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class QuizapyHelper extends SQLiteOpenHelper {

    private final String TAG = QuizapyHelper.class.getSimpleName();

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
        contentValues.put(QuizapyContract.StuffTable.COLUMN_NAME, "points_in_save");
        contentValues.put(QuizapyContract.StuffTable.COLUMN_VALUE, "0");
        db.insert(QuizapyContract.StuffTable.TABLE_NAME, null, contentValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO implement logic
    }
}

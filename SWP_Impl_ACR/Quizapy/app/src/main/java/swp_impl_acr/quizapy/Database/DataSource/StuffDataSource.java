package swp_impl_acr.quizapy.Database.DataSource;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import swp_impl_acr.quizapy.Database.QuizapyContract;
import swp_impl_acr.quizapy.Database.QuizapyDataSource;

/**
 *  class to handle key / value table queries
 */
public class StuffDataSource {

    private SQLiteDatabase db;
    private QuizapyDataSource instance;

    /**
     * constructor
     * @throws Exception
     */
    public StuffDataSource() throws Exception {
        this.instance = QuizapyDataSource.getInstance();
        this.db = instance.getConnection();
    }

    /**
     * returns the value belonging to a given key
     * @param key
     * @return
     */
    public String getValue(String key) {
        Cursor cursor = db.query(QuizapyContract.StuffTable.TABLE_NAME,
                null, QuizapyContract.StuffTable.COLUMN_NAME + " = ?",
                new String[]{key}, null, null, null);
        cursor.moveToFirst();
        String rtn = cursor.getString(cursor.getColumnIndex(QuizapyContract.StuffTable.COLUMN_VALUE));
        return rtn;
    }

    /**
     * sets value belonging to a specific key
     * @param key
     * @param value
     */
    public void setValue(String key, String value) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(QuizapyContract.StuffTable.COLUMN_VALUE, value);

        db.update(QuizapyContract.StuffTable.TABLE_NAME,
                contentValues,
                QuizapyContract.StuffTable.COLUMN_NAME + " = ?",
                new String[]{key});
    }
}

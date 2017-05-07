package swp_impl_acr.quizapy.Database.DataSource;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import swp_impl_acr.quizapy.Database.QuizapyContract;
import swp_impl_acr.quizapy.Database.QuizapyDataSource;

public class StuffDataSource {

    private SQLiteDatabase db;
    private QuizapyDataSource instance;

    public StuffDataSource() throws Exception {
        this.instance=QuizapyDataSource.getInstance();
        this.db= instance.getConnection();
    }

    public String getValue(String key){
        Cursor cursor = db.query(QuizapyContract.StuffTable.TABLE_NAME, null, QuizapyContract.StuffTable.COLUMN_NAME + " = ?", new String[]{key}, null, null, null);
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex(QuizapyContract.StuffTable.COLUMN_VALUE));
    }

    public void setValue(String key, String value) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(QuizapyContract.StuffTable.COLUMN_VALUE, value);

        db.update(QuizapyContract.StuffTable.TABLE_NAME, contentValues, QuizapyContract.StuffTable.COLUMN_NAME + " = ?", new String[]{key});
    }
}

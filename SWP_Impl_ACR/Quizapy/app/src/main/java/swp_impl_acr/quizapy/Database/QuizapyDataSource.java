package swp_impl_acr.quizapy.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

public class QuizapyDataSource {

    private static QuizapyDataSource instance;
    private final String TAG = QuizapyDataSource.class.getSimpleName();

    private SQLiteDatabase db;
    private QuizapyHelper dbHelper;
    private boolean isOpen = false;

    private QuizapyDataSource(Context context) throws SQLException {
        dbHelper = new QuizapyHelper(context);
        openConnection();
    }

    public static QuizapyDataSource getInstance(Context context) throws SQLException {
        if (QuizapyDataSource.instance == null) {
            QuizapyDataSource.instance = new QuizapyDataSource(context);
        }
        return QuizapyDataSource.instance;
    }

    public static QuizapyDataSource getInstance() throws Exception {
        if (QuizapyDataSource.instance != null) {
            return QuizapyDataSource.instance;
        } else {
            throw new Exception("Instance not initialized; use getInstance(Context)");
        }
    }

    public void openConnection() throws SQLException {
        if (!isOpen) {
            db = dbHelper.getWritableDatabase();
            isOpen = true;
        }
    }

    public void closeConnection() throws SQLException {
        if (db.isOpen()) {
            db.close();
            isOpen = false;
        }
    }

    public SQLiteDatabase getConnection() {
        if (db.isOpen()) {
            return db;
        } else {
            return null;
        }
    }

    public boolean checkIfDataExistsInDb(String tableName, String dbField, String fieldValue) {
        Cursor cursor = db.query(tableName, null, dbField + " = ?", new String[]{fieldValue}, null, null, null);

        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
}

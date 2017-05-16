package swp_impl_acr.quizapy.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

/**
 * class to handle the connection to the database
 */
public class QuizapyDataSource {

    private static QuizapyDataSource instance;
    private final String TAG = QuizapyDataSource.class.getSimpleName();

    private SQLiteDatabase db;
    private QuizapyHelper dbHelper;
    private boolean isOpen = false;

    /**
     * private constructor
     *
     * @param context
     * @throws SQLException
     */
    private QuizapyDataSource(Context context) throws SQLException {
        dbHelper = new QuizapyHelper(context);
        openConnection();
    }

    /**
     * returns instance
     * @param context
     * @return
     * @throws SQLException
     */
    public static QuizapyDataSource getInstance(Context context) throws SQLException {
        if (QuizapyDataSource.instance == null) {
            QuizapyDataSource.instance = new QuizapyDataSource(context);
        }
        return QuizapyDataSource.instance;
    }

    /**
     * returns instance
     *
     * @return
     * @throws Exception
     */
    public static QuizapyDataSource getInstance() throws Exception {
        if (QuizapyDataSource.instance != null) {
            return QuizapyDataSource.instance;
        } else {
            throw new Exception("Instance not initialized; use getInstance(Context)");
        }
    }

    /**
     * opens connection to the database
     * @throws SQLException
     */
    public void openConnection() throws SQLException {
        if (!isOpen) {
            db = dbHelper.getWritableDatabase();
            isOpen = true;
        }
    }

    /**
     * closes connection to the database
     * @throws SQLException
     */
    public void closeConnection() throws SQLException {
        if (db.isOpen()) {
            db.close();
            isOpen = false;
        }
    }

    /**
     * returns the database connection
     * @return
     */
    public SQLiteDatabase getConnection() {
        if (db.isOpen()) {
            return db;
        } else {
            return null;
        }
    }

    /**
     * returns true if value already exists in the specified tablefield
     *
     * @param tableName
     * @param dbField
     * @param fieldValue
     * @return
     */
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

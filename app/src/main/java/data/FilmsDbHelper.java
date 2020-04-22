package data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class FilmsDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = FilmsDbHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "films.db";
    private static final int DATABASE_VERSION = 1;

    public FilmsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + FilmsContract.AddMovie.TABLE_NAME + "("
                + FilmsContract.AddMovie._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FilmsContract.AddMovie.COLUMN_NAME + " TEXT NOT NULL, "
                + FilmsContract.AddMovie.COLUMN_YEAR + " INTEGER NOT NULL, "
                + FilmsContract.AddMovie.COLUMN_COUNTRY + " TEXT NOT NULL, "
                + FilmsContract.AddMovie.COLUMN_GANRE + " TEXT NOT NULL, "
                + FilmsContract.AddMovie.COLUMN_ACTOR + " TEXT NOT NULL, "
                + FilmsContract.AddMovie.COLUMN_PRODUCER + " TEXT NOT NULL, "
                + FilmsContract.AddMovie.COLUMN_IMDB + " DOUBLE NOT NULL, "
                + FilmsContract.AddMovie.COLUMN_KINOPOISK + " DOUBLE NOT NULL, "
                + FilmsContract.AddMovie.COLUMN_WANT + " INT NOT NULL, "
                + FilmsContract.AddMovie.COLUMN_DESCRIPTION + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("SQLite", "Updating from version " + oldVersion + " to version " + newVersion);

        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        onCreate(db);
    }
}

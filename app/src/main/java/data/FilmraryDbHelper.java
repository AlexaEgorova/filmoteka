package data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FilmraryDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = FilmraryDbHelper.class.getSimpleName();
    public static final String DATABASE_NAME = "films.db";
    private static final int DATABASE_VERSION = 1;

    public FilmraryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + FilmsContract.Films.TABLE_NAME + "("
                + FilmsContract.Films._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FilmsContract.Films.COLUMN_NAME + " TEXT NOT NULL, "
                + FilmsContract.Films.COLUMN_YEAR + " INTEGER NOT NULL, "
                + FilmsContract.Films.COLUMN_COUNTRY + " TEXT NOT NULL, "
                + FilmsContract.Films.COLUMN_GANRE + " TEXT NOT NULL, "
                + FilmsContract.Films.COLUMN_ACTOR + " TEXT NOT NULL, "
                + FilmsContract.Films.COLUMN_PRODUCER + " TEXT NOT NULL, "
                + FilmsContract.Films.COLUMN_IMDB + " DOUBLE NOT NULL, "
                + FilmsContract.Films.COLUMN_KINOPOISK + " DOUBLE NOT NULL, "
                + FilmsContract.Films.COLUMN_WANT + " INT NOT NULL, "
                + FilmsContract.Films.COLUMN_DESCRIPTION + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("SQLite", "Updating from version " + oldVersion + " to version " + newVersion);

        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        onCreate(db);
    }
}

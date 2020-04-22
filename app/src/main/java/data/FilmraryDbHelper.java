package data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class FilmraryDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = FilmraryDbHelper.class.getSimpleName();
    public static final String DATABASE_NAME = "filmrary.db";
    private static final int DATABASE_VERSION = 1;

    public FilmraryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_FILMS_TABLE = "CREATE TABLE " + FilmsContract.Films.TABLE_NAME + "("
                + FilmsContract.Films._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FilmsContract.Films.COLUMN_NAME + " TEXT NOT NULL, "
                + FilmsContract.Films.COLUMN_YEAR + " INTEGER NOT NULL, "
                + FilmsContract.Films.COLUMN_COUNTRY + " TEXT, "
                + FilmsContract.Films.COLUMN_AGE + " INT, "
                + FilmsContract.Films.COLUMN_GANRE + " TEXT, "
                + FilmsContract.Films.COLUMN_ACTOR + " TEXT, "
                + FilmsContract.Films.COLUMN_PRODUCER + " TEXT, "
                + FilmsContract.Films.COLUMN_IMDB + " DOUBLE, "
                + FilmsContract.Films.COLUMN_KINOPOISK + " DOUBLE, "
                + FilmsContract.Films.COLUMN_WANT + " INT, "
                + FilmsContract.Films.COLUMN_LINK + " TEXT, "
                + FilmsContract.Films.COLUMN_DESCRIPTION + " TEXT)";

        String SQL_CREATE_ACTORS_TABLE = "CREATE TABLE " + ActorsContract.Actors.TABLE_NAME + "("
                + ActorsContract.Actors._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ActorsContract.Actors.COLUMN_NAME + " TEXT NOT NULL, "
                + ActorsContract.Actors.COLUMN_SURNAME + " TEXT NOT NULL, "
                + ActorsContract.Actors.COLUMN_NICKNAME + " TEXT, "
                + ActorsContract.Actors.COLUMN_BIRTHDAY + " DATETIME, "
                + ActorsContract.Actors.COLUMN_INFO + " TEXT" + ")";

        String SQL_CREATE_ACTOR_FILM_TABLE = "CREATE TABLE " + ActorFilmContract.ActorFilm.TABLE_NAME + "("
                + ActorFilmContract.ActorFilm._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ActorFilmContract.ActorFilm.COLUMN_ACTOR_ID + " INTEGER NOT NULL, "
                + ActorFilmContract.ActorFilm.COLUMN_FILM_ID + " INTEGER NOT NULL" + ")";

        String SQL_CREATE_COUNTRIES_TABLE = "CREATE TABLE " + CountriesContract.Countries.TABLE_NAME + "("
                + CountriesContract.Countries._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CountriesContract.Countries.COLUMN_NAME  + " TEXT NOT NULL" + ")";

        String SQL_CREATE_COUNTRY_FILM_TABLE = "CREATE TABLE " + CountryFilmContract.CountryFilm.TABLE_NAME + "("
                + CountryFilmContract.CountryFilm._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CountryFilmContract.CountryFilm.COLUMN_COUNTRY_ID + " INTEGER NOT NULL, "
                + CountryFilmContract.CountryFilm.COLUMN_FILM_ID + " INTEGER NOT NULL" + ")";

        String SQL_CREATE_GANRES_TABLE = "CREATE TABLE " + GanresContract.Ganres.TABLE_NAME + "("
                + GanresContract.Ganres._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + GanresContract.Ganres.COLUMN_NAME + " TEXT NOT NULL, "
                + GanresContract.Ganres.COLUMN_INFO + " TEXT" + ")";

        String SQL_CREATE_GANRE_FILM_TABLE = "CREATE TABLE " + GanreFilmContract.GanreFilm.TABLE_NAME + "("
                + GanreFilmContract.GanreFilm._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + GanreFilmContract.GanreFilm.COLUMN_GANRE_ID + " INTEGER NOT NULL, "
                + GanreFilmContract.GanreFilm.COLUMN_FILM_ID + " INTEGER NOT NULL" + ")";

        String SQL_CREATE_PRODUCERS_TABLE = "CREATE TABLE " + ProducersContract.Producers.TABLE_NAME + "("
                + ProducersContract.Producers._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ProducersContract.Producers.COLUMN_NAME + " TEXT NOT NULL, "
                + ProducersContract.Producers.COLUMN_SURNAME + " TEXT NOT NULL, "
                + ProducersContract.Producers.COLUMN_INFO + " TEXT" + ")";

        String SQL_CREATE_PRODUCER_FILM_TABLE = "CREATE TABLE " + ProducerFilmContract.ProducerFilm.TABLE_NAME + "("
                + ProducerFilmContract.ProducerFilm._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ProducerFilmContract.ProducerFilm.COLUMN_PRODUCER_ID + " INTEGER NOT NULL, "
                + ProducerFilmContract.ProducerFilm.COLUMN_FILM_ID + " INTEGER NOT NULL" + ")";

        String SQL_CREATE_WANT_TO_WATCH_TABLE = "CREATE TABLE " + WantToWatchContract.WantToWatch.TABLE_NAME + "("
                + WantToWatchContract.WantToWatch._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + WantToWatchContract.WantToWatch.COLUMN_FILM_ID + " INTEGER NOT NULL, "
                + WantToWatchContract.WantToWatch.COLUMN_ADD_DATE + " DATETIME" + ")";

        String SQL_CREATE_WATCHED_TABLE = "CREATE TABLE " + WatchedContract.Watched.TABLE_NAME + "("
                + WatchedContract.Watched._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + WatchedContract.Watched.COLUMN_FILM_ID + " INTEGER NOT NULL, "
                + WatchedContract.Watched.COLUMN_DATE + " DATETIME, "
                + WatchedContract.Watched.COLUMN_RATE + " INTEGER, "
                + WatchedContract.Watched.COLUMN_OPINION + " TEXT" + ")";

        db.execSQL(SQL_CREATE_ACTORS_TABLE);
        db.execSQL(SQL_CREATE_ACTOR_FILM_TABLE);
        db.execSQL(SQL_CREATE_COUNTRIES_TABLE);
        db.execSQL(SQL_CREATE_COUNTRY_FILM_TABLE);
        db.execSQL(SQL_CREATE_FILMS_TABLE);
        db.execSQL(SQL_CREATE_GANRES_TABLE);
        db.execSQL(SQL_CREATE_GANRE_FILM_TABLE);
        db.execSQL(SQL_CREATE_PRODUCERS_TABLE);
        db.execSQL(SQL_CREATE_PRODUCER_FILM_TABLE);
        db.execSQL(SQL_CREATE_WANT_TO_WATCH_TABLE);
        db.execSQL(SQL_CREATE_WATCHED_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("SQLite", "Updating from version " + oldVersion + " to version " + newVersion);

        db.execSQL("DROP TABLE IF EXISTS " + ActorsContract.Actors.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ActorFilmContract.ActorFilm.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CountriesContract.Countries.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CountryFilmContract.CountryFilm.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FilmsContract.Films.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + GanresContract.Ganres.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + GanreFilmContract.GanreFilm.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ProducersContract.Producers.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ProducerFilmContract.ProducerFilm.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + WantToWatchContract.WantToWatch.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + WatchedContract.Watched.TABLE_NAME);

        onCreate(db);
    }
}

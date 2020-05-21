package data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import androidx.annotation.NonNull
import data.ActorFilmContract.ActorFilm
import data.ActorSeriesContract.ActorSeries
import data.ActorsContract.Actors
import data.CountriesContract.Countries
import data.CountryFilmContract.CountryFilm
import data.CountrySeriesContract.CountrySeries
import data.FilmsContract.Films
import data.GenreFilmContract.GenreFilm
import data.GenreSeriesContract.GenreSeries
import data.ProducerFilmContract.ProducerFilm
import data.ProducerSeriesContract.ProducerSeries
import data.ProducersContract.Producers
import data.SeriesContract.Series
import data.WantToWatchContract.WantToWatch
import data.WantToWatchSeriesContract.WantToWatchSeries
import data.WatchedContract.Watched
import data.WatchedSeriesContract.WatchedSeries
import data.WatchingSeriesContract.WatchingSeries
import data.GenresContract.Genres

class FilmraryDbHelper private constructor(context: Context) :
        SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    fun fundamental(db: SQLiteDatabase) {
        val SQL_CREATE_FILMS_TABLE = ("CREATE TABLE ${Films.TABLE_NAME} ("
                + "${Films._ID} INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "${Films.COLUMN_NAME} TEXT NOT NULL, "
                + "${Films.COLUMN_YEAR} INT NOT NULL, "
                + "${Films.COLUMN_COUNTRY} TEXT, "
                + "${Films.COLUMN_AGE} INT, "
                + "${Films.COLUMN_GANRE} TEXT, "
                + "${Films.COLUMN_ACTOR} TEXT, "
                + "${Films.COLUMN_PRODUCER} TEXT, "
                + "${Films.COLUMN_IMDB} DOUBLE, "
                + "${Films.COLUMN_KINOPOISK} DOUBLE, "
                + "${Films.COLUMN_WANT} INT, "
                + "${Films.COLUMN_LINK} TEXT, "
                + "${Films.COLUMN_DESCRIPTION} TEXT)")

        val SQL_CREATE_ACTORS_TABLE = ("CREATE TABLE ${Actors.TABLE_NAME} ("
                + "${Actors._ID} INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "${Actors.COLUMN_NAME} TEXT NOT NULL, "
                + "${Actors.COLUMN_SURNAME} TEXT NOT NULL, "
                + "${Actors.COLUMN_NICKNAME} TEXT, "
                + "${Actors.COLUMN_BIRTHDAY} DATETIME, "
                + "${Actors.COLUMN_INFO} TEXT)")

        val SQL_CREATE_COUNTRIES_TABLE = ("CREATE TABLE ${Countries.TABLE_NAME} ("
                + "${Countries._ID} INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "${Countries.COLUMN_NAME} TEXT NOT NULL)")

        val SQL_CREATE_GENRES_TABLE = ("CREATE TABLE ${Genres.TABLE_NAME} ("
                + "${Genres._ID} INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "${Genres.COLUMN_NAME} TEXT NOT NULL, "
                + "${Genres.COLUMN_INFO} TEXT)")

        val SQL_CREATE_PRODUCERS_TABLE = ("CREATE TABLE ${Producers.TABLE_NAME} ("
                + "${Producers._ID} INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "${Producers.COLUMN_NAME} TEXT NOT NULL, "
                + "${Producers.COLUMN_SURNAME} TEXT NOT NULL, "
                + "${Producers.COLUMN_INFO} TEXT)")

        val SQL_CREATE_SERIES_TABLE = ("CREATE TABLE ${Series.TABLE_NAME} ("
                + "${Series._ID} INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "${Series.COLUMN_NAME} TEXT NOT NULL, "
                + "${Series.COLUMN_START_YEAR} INTEGER NOT NULL, "
                + "${Series.COLUMN_SEASONS_NUM} INTEGER, "
                + "${Series.COLUMN_EP_DURATION} INTEGER, "
                + "${Series.COLUMN_EP_IN_SEASON_NUM} INTEGER, "
                + "${Series.COLUMN_STATE} TEXT, "
                + "${Series.COLUMN_COUNTRY} TEXT, "
                + "${Series.COLUMN_AGE} INTEGER, "
                + "${Series.COLUMN_GENRE} TEXT, "
                + "${Series.COLUMN_ACTOR} TEXT, "
                + "${Series.COLUMN_PRODUCER} TEXT, "
                + "${Series.COLUMN_IMDB} DOUBLE, "
                + "${Series.COLUMN_KINOPOISK} DOUBLE, "
                + "${Series.COLUMN_WANT} INTEGER, "
                + "${Series.COLUMN_LINK} TEXT, "
                + "${Series.COLUMN_DESCRIPTION} TEXT)")

        db.execSQL(SQL_CREATE_ACTORS_TABLE)
        db.execSQL(SQL_CREATE_COUNTRIES_TABLE)
        db.execSQL(SQL_CREATE_FILMS_TABLE)
        db.execSQL(SQL_CREATE_GENRES_TABLE)
        db.execSQL(SQL_CREATE_PRODUCERS_TABLE)
        db.execSQL(SQL_CREATE_SERIES_TABLE)
    }

    fun connectedWithFilms(db: SQLiteDatabase) {
        val SQL_CREATE_ACTOR_FILM_TABLE = ("CREATE TABLE ${ActorFilm.TABLE_NAME} ("
                + "${ActorFilm._ID} INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "${ActorFilm.COLUMN_ACTOR_ID} INTEGER NOT NULL, "
                + "${ActorFilm.COLUMN_FILM_ID} INTEGER NOT NULL,"
                + "CONSTRAINT fk_actor_film "
                + "FOREIGN KEY (${ActorFilm.COLUMN_ACTOR_ID}) REFERENCES ${Actors.TABLE_NAME} (${Actors._ID}) "
                + "FOREIGN KEY (${ActorFilm.COLUMN_FILM_ID}) REFERENCES ${Films.TABLE_NAME} (${Films._ID}) "
                + "ON DELETE CASCADE)")

        val SQL_CREATE_COUNTRY_FILM_TABLE = ("CREATE TABLE ${CountryFilm.TABLE_NAME} ("
                + "${CountryFilm._ID} INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "${CountryFilm.COLUMN_COUNTRY_ID} INTEGER NOT NULL, "
                + "${CountryFilm.COLUMN_FILM_ID} INTEGER NOT NULL, "
                + "CONSTRAINT fk_country_film "
                + "FOREIGN KEY (${CountryFilm.COLUMN_COUNTRY_ID}) REFERENCES ${Countries.TABLE_NAME} (${Countries._ID}) "
                + "FOREIGN KEY (${CountryFilm.COLUMN_FILM_ID}) REFERENCES ${Films.TABLE_NAME} (${Films._ID}) "
                + "ON DELETE CASCADE)")

        val SQL_CREATE_GENRE_FILM_TABLE = ("CREATE TABLE ${GenreFilm.TABLE_NAME} ("
                + "${GenreFilm._ID} INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "${GenreFilm.COLUMN_GENRE_ID} INTEGER NOT NULL, "
                + "${GenreFilm.COLUMN_FILM_ID} INTEGER NOT NULL, "
                + "CONSTRAINT fk_genre_film "
                + "FOREIGN KEY (${GenreFilm.COLUMN_GENRE_ID}) REFERENCES ${Genres.TABLE_NAME} (${Genres._ID}) "
                + "FOREIGN KEY (${GenreFilm.COLUMN_FILM_ID}) REFERENCES ${Films.TABLE_NAME} (${Films._ID}) "
                + "ON DELETE CASCADE)")

        val SQL_CREATE_PRODUCER_FILM_TABLE = ("CREATE TABLE ${ProducerFilm.TABLE_NAME} ("
                + "${ProducerFilm._ID} INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "${ProducerFilm.COLUMN_PRODUCER_ID} INTEGER NOT NULL, "
                + "${ProducerFilm.COLUMN_FILM_ID} INTEGER NOT NULL, "
                + "CONSTRAINT fk_producer_film "
                + "FOREIGN KEY (${ProducerFilm.COLUMN_PRODUCER_ID}) REFERENCES ${Producers.TABLE_NAME} (${Producers._ID}) "
                + "FOREIGN KEY (${ProducerFilm.COLUMN_FILM_ID}) REFERENCES ${Films.TABLE_NAME} (${Films._ID}) "
                + "ON DELETE CASCADE)")

        val SQL_CREATE_WANT_TO_WATCH_TABLE = ("CREATE TABLE ${WantToWatch.TABLE_NAME} ("
                + "${WantToWatch._ID} INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "${WantToWatch.COLUMN_FILM_ID} INTEGER NOT NULL, "
                + "${WantToWatch.COLUMN_ADD_DATE} DATETIME, "
                + "CONSTRAINT fk_want_to_watch_film "
                + "FOREIGN KEY (${WantToWatch.COLUMN_FILM_ID}) REFERENCES ${Films.TABLE_NAME} (${Films._ID}) "
                + "ON DELETE CASCADE)")

        val SQL_CREATE_WATCHED_TABLE = ("CREATE TABLE ${Watched.TABLE_NAME} ("
                + "${Watched._ID} INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "${Watched.COLUMN_FILM_ID} INTEGER NOT NULL, "
                + "${Watched.COLUMN_DATE} DATETIME, "
                + "${Watched.COLUMN_RATE} INTEGER, "
                + "${Watched.COLUMN_OPINION} TEXT, "
                + "CONSTRAINT fk_watched_film "
                + "FOREIGN KEY (${Watched.COLUMN_FILM_ID}) REFERENCES ${Films.TABLE_NAME} (${Films._ID}) "
                + "ON DELETE CASCADE)")

        db.execSQL(SQL_CREATE_ACTOR_FILM_TABLE)
        db.execSQL(SQL_CREATE_COUNTRY_FILM_TABLE)
        db.execSQL(SQL_CREATE_GENRE_FILM_TABLE)
        db.execSQL(SQL_CREATE_PRODUCER_FILM_TABLE)
        db.execSQL(SQL_CREATE_WANT_TO_WATCH_TABLE)
        db.execSQL(SQL_CREATE_WATCHED_TABLE)
    }

    fun connectedWithSeries(db: SQLiteDatabase) {
        val SQL_CREATE_ACTOR_SERIES_TABLE = ("CREATE TABLE ${ActorSeries.TABLE_NAME} ("
                + "${ActorSeries._ID} INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "${ActorSeries.COLUMN_ACTOR_ID} INTEGER NOT NULL, "
                + "${ActorSeries.COLUMN_SERIES_ID} INTEGER NOT NULL, "
                + "CONSTRAINT fk_series_actor "
                + "FOREIGN KEY (${ActorSeries.COLUMN_ACTOR_ID}) REFERENCES ${Actors.TABLE_NAME} (${Actors._ID}) "
                + "FOREIGN KEY (${ActorSeries.COLUMN_SERIES_ID}) REFERENCES ${Series.TABLE_NAME} (${Series._ID}) "
                + "ON DELETE CASCADE)")

        val SQL_CREATE_COUNTRY_SERIES_TABLE = ("CREATE TABLE ${CountrySeries.TABLE_NAME} ("
                + "${CountrySeries._ID} INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "${CountrySeries.COLUMN_COUNTRY_ID} INTEGER NOT NULL, "
                + "${CountrySeries.COLUMN_SERIES_ID} INTEGER NOT NULL, "
                + "CONSTRAINT fk_country_series "
                + "FOREIGN KEY (${CountrySeries.COLUMN_COUNTRY_ID}) REFERENCES ${Countries.TABLE_NAME} (${Countries._ID}) "
                + "FOREIGN KEY (${CountrySeries.COLUMN_SERIES_ID}) REFERENCES ${Series.TABLE_NAME} (${Series._ID}) "
                + "ON DELETE CASCADE)")

        val SQL_CREATE_GENRE_SERIES_TABLE = ("CREATE TABLE ${GenreSeries.TABLE_NAME} ("
                + "${GenreSeries._ID} INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "${GenreSeries.COLUMN_GENRE_ID} INTEGER NOT NULL, "
                + "${GenreSeries.COLUMN_SERIES_ID} INTEGER NOT NULL, "
                + "CONSTRAINT fk_genre_series "
                + "FOREIGN KEY (${GenreSeries.COLUMN_GENRE_ID}) REFERENCES ${Genres.TABLE_NAME} (${Genres._ID}) "
                + "FOREIGN KEY (${GenreSeries.COLUMN_SERIES_ID}) REFERENCES ${Series.TABLE_NAME} (${Series._ID}) "
                + "ON DELETE CASCADE)")

        val SQL_CREATE_PRODUCER_SERIES_TABLE = ("CREATE TABLE ${ProducerSeries.TABLE_NAME} ("
                + "${ProducerSeries._ID} INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "${ProducerSeries.COLUMN_PRODUCER_ID} INTEGER NOT NULL, "
                + "${ProducerSeries.COLUMN_SERIES_ID} INTEGER NOT NULL, "
                + "CONSTRAINT fk_producer_series "
                + "FOREIGN KEY (${ProducerSeries.COLUMN_PRODUCER_ID}) REFERENCES ${Producers.TABLE_NAME} (${Producers._ID}) "
                + "FOREIGN KEY (${ProducerSeries.COLUMN_SERIES_ID}) REFERENCES ${Series.TABLE_NAME} (${Series._ID}) "
                + "ON DELETE CASCADE)")

        val SQL_CREATE_WANT_TO_WATCH_SERIES_TABLE = ("CREATE TABLE ${WantToWatchSeries.TABLE_NAME} ("
                + "${WantToWatchSeries._ID} INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "${WantToWatchSeries.COLUMN_SERIES_ID} INTEGER NOT NULL, "
                + "${WantToWatchSeries.COLUMN_ADD_DATE} DATE, "
                + "CONSTRAINT fk_want_to_watch_series "
                + "FOREIGN KEY (${WantToWatchSeries.COLUMN_SERIES_ID}) REFERENCES ${Series.TABLE_NAME} (${Series._ID}) "
                + "ON DELETE CASCADE)")

        val SQL_CREATE_WATCHED_SERIES_TABLE = ("CREATE TABLE ${WatchedSeries.TABLE_NAME} ("
                + "${WatchedSeries._ID} INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "${WatchedSeries.COLUMN_SERIES_ID} INTEGER NOT NULL, "
                + "${WatchedSeries.COLUMN_DATE} DATE, "
                + "${WatchedSeries.COLUMN_RATE} INTEGER, "
                + "${WatchedSeries.COLUMN_OPINION} TEXT, "
                + "CONSTRAINT fk_watched_series "
                + "FOREIGN KEY (${WatchedSeries.COLUMN_SERIES_ID}) REFERENCES ${Series.TABLE_NAME} (${Series._ID}) "
                + "ON DELETE CASCADE)")

        val SQL_CREATE_WATCHING_SERIES_TABLE = ("CREATE TABLE ${WatchingSeries.TABLE_NAME} ("
                + "${WatchingSeries._ID} INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "${WatchingSeries.COLUMN_SERIES_ID} INTEGER NOT NULL, "
                + "${WatchingSeries.COLUMN_SEASON_NUM} INTEGER, "
                + "${WatchingSeries.COLUMN_EPISODE_NUM} INTEGER, "
                + "CONSTRAINT fk_watching_series "
                + "FOREIGN KEY (${WatchingSeries.COLUMN_SERIES_ID}) REFERENCES ${Series.TABLE_NAME} (${Series._ID}) "
                + "ON DELETE CASCADE)")


        db.execSQL(SQL_CREATE_ACTOR_SERIES_TABLE)
        db.execSQL(SQL_CREATE_COUNTRY_SERIES_TABLE)
        db.execSQL(SQL_CREATE_GENRE_SERIES_TABLE)
        db.execSQL(SQL_CREATE_PRODUCER_SERIES_TABLE)
        db.execSQL(SQL_CREATE_WANT_TO_WATCH_SERIES_TABLE)
        db.execSQL(SQL_CREATE_WATCHED_SERIES_TABLE)
        db.execSQL(SQL_CREATE_WATCHING_SERIES_TABLE)
    }

    override fun onCreate(db: SQLiteDatabase) {
        fundamental(db)
        connectedWithFilms(db)
        connectedWithSeries(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.w("SQLite", "Updating from version $oldVersion to version $newVersion")
        val DROP = "DROP TABLE IF EXISTS "
        val tables = arrayOf(
                Actors.TABLE_NAME,
                ActorFilm.TABLE_NAME,
                Countries.TABLE_NAME,
                CountryFilm.TABLE_NAME,
                Films.TABLE_NAME,
                Genres.TABLE_NAME,
                GenreFilm.TABLE_NAME,
                Producers.TABLE_NAME,
                ProducerFilm.TABLE_NAME,
                WantToWatch.TABLE_NAME,
                Watched.TABLE_NAME,
                Series.TABLE_NAME,
                ActorSeries.TABLE_NAME,
                CountrySeries.TABLE_NAME,
                GenreSeries.TABLE_NAME,
                ProducerSeries.TABLE_NAME,
                WantToWatchSeries.TABLE_NAME,
                WatchedSeries.TABLE_NAME,
                WatchingSeries.TABLE_NAME
        )
        for (tableName in tables) {
            db.execSQL("$DROP $tableName")
        }
        onCreate(db)
    }

    companion object {
        private lateinit var instance: FilmraryDbHelper
        val LOG_TAG = FilmraryDbHelper::class.java.simpleName
        private const val DATABASE_NAME = "filmrary.db"
        private const val DATABASE_VERSION = 1
        private var inited = false

        @JvmStatic
        fun getInstance(context: Context): FilmraryDbHelper {
            if (!inited) {
                instance = FilmraryDbHelper(context.applicationContext)
                inited = true
            }
            return instance
        }
    }
}
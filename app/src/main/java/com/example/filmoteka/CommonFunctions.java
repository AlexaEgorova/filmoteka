package com.example.filmoteka;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import data.*;

import java.util.Calendar;


import data.ActorFilmContract.ActorFilm;
import data.ActorsContract.Actors;
import data.ActorSeriesContract.ActorSeries;
import data.CountriesContract.Countries;
import data.CountryFilmContract.CountryFilm;
import data.CountrySeriesContract.CountrySeries;
import data.FilmsContract.Films;
import data.GenreFilmContract.GenreFilm;
import data.GenresContract.Genres;
import data.GenreSeriesContract.GenreSeries;
import data.ProducerFilmContract.ProducerFilm;
import data.ProducersContract.Producers;
import data.ProducerSeriesContract.ProducerSeries;
import data.SeriesContract.Series;
import data.WantToWatchContract.WantToWatch;
import data.WantToWatchSeriesContract.WantToWatchSeries;
import data.WatchedContract.Watched;
import data.WatchedSeriesContract.WatchedSeries;

public class CommonFunctions {
    public static void addMovie(String vNameEditText,
                                String vYearSpinner, String vCountrySpinner, String vAgeEditText, String vGenreSpinner,
                                String vActorSpinner, String vProducerSpinner,
                                String vImdbEditText, String vKinopoiskEditText,
                                String vWantRadioGroup,
                                String vLinkEditText, String vDescriptionEditText,
                                FilmraryDbHelper vDbHelper) {
        SQLiteDatabase db = vDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Films.COLUMN_NAME, vNameEditText);
        values.put(Films.COLUMN_YEAR, Integer.parseInt(vYearSpinner));
        values.put(Films.COLUMN_COUNTRY, vCountrySpinner);
        values.put(Films.COLUMN_AGE, vAgeEditText);
        values.put(Films.COLUMN_GANRE, vGenreSpinner);
        values.put(Films.COLUMN_ACTOR, vActorSpinner);
        values.put(Films.COLUMN_PRODUCER, vProducerSpinner);
        values.put(Films.COLUMN_IMDB, Double.parseDouble(vImdbEditText));
        values.put(Films.COLUMN_KINOPOISK, Double.parseDouble(vKinopoiskEditText));
        values.put(Films.COLUMN_WANT, Integer.parseInt(vWantRadioGroup));
        values.put(Films.COLUMN_DESCRIPTION, vDescriptionEditText);
        values.put(Films.COLUMN_LINK, vLinkEditText);
        int newFilmsRowId = (int) db.insert(Films.TABLE_NAME, null, values);
        Log.d("addMovie", "Added film with Row ID" + newFilmsRowId);


        String query = selectAllFromTableWhere(Actors.TABLE_NAME, Actors.COLUMN_NAME, vActorSpinner.split(" ")[0])
                + " AND surname = '" + vActorSpinner.split(" ")[1] + "'";
        cascadeAdd(db, query, ActorFilm.COLUMN_ACTOR_ID, ActorFilm.COLUMN_FILM_ID, newFilmsRowId, ActorFilm.TABLE_NAME,
                   "ActorFilm");

        query = selectAllFromTableWhere(Countries.TABLE_NAME, Countries.COLUMN_NAME, vCountrySpinner);
        cascadeAdd(db, query, CountryFilm.COLUMN_COUNTRY_ID, CountryFilm.COLUMN_FILM_ID, newFilmsRowId,
                   CountryFilm.TABLE_NAME, "CountryFilm");

        query = selectAllFromTableWhere(Genres.TABLE_NAME, Genres.COLUMN_NAME, vGenreSpinner);
        cascadeAdd(db, query, GenreFilm.COLUMN_GENRE_ID, GenreFilm.COLUMN_FILM_ID, newFilmsRowId, GenreFilm.TABLE_NAME,
                   "GenreFilm");

        query = selectAllFromTableWhere(Producers.TABLE_NAME, Producers.COLUMN_NAME, vProducerSpinner.split(" ")[0])
                + " AND surname = '" + vProducerSpinner.split(" ")[1] + "'";
        cascadeAdd(db, query, ProducerFilm.COLUMN_PRODUCER_ID, ProducerFilm.COLUMN_FILM_ID, newFilmsRowId,
                   ProducerFilm.TABLE_NAME, "ProducerFilm");

        values.clear();
        if (Integer.parseInt(vWantRadioGroup) == 1) {
            values.put(WantToWatch.COLUMN_FILM_ID, newFilmsRowId);
            values.put(WantToWatch.COLUMN_ADD_DATE, Calendar.DATE);
            long newWantToWatchRowId = db.insert(WantToWatch.TABLE_NAME, null, values);
            Log.d("addMovie", "Added WantToWatch with Row ID" + newWantToWatchRowId);

        } else if (Integer.parseInt(vWantRadioGroup) == 2) {
            values.put(Watched.COLUMN_FILM_ID, newFilmsRowId);
            values.put(Watched.COLUMN_DATE, Calendar.DATE);
            long newWatchedRowId = db.insert(Watched.TABLE_NAME, null, values);
            Log.d("addMovie", "Added Watched with Row ID" + newWatchedRowId);
        }

    }

    public static void addSeries(String vNameEditText, String vStartYearSpinner,
                                 String vSeasonsNumEditText, String vEpDurationEditText,
                                 String vEpInSeasonNumEditText, String vStateSpinner,
                                 String vCountrySpinner, String vAgeEditText, String vGanreSpinner,
                                 String vActorSpinner, String vProducerSpinner,
                                 String vImdbEditText, String vKinopoiskEditText,
                                 String vWantRadioGroup, String vLinkEditText, String vDescriptionEditText,
                                 FilmraryDbHelper vDbHelper) {

        SQLiteDatabase db = vDbHelper.getWritableDatabase();
        int currentId;

        ContentValues values = new ContentValues();
        values.put(Series.COLUMN_NAME, vNameEditText);
        values.put(Series.COLUMN_START_YEAR, Integer.parseInt(vStartYearSpinner));
        values.put(Series.COLUMN_SEASONS_NUM, Integer.parseInt(vSeasonsNumEditText));
        values.put(Series.COLUMN_EP_DURATION, Integer.parseInt(vEpDurationEditText));
        values.put(Series.COLUMN_EP_IN_SEASON_NUM, Integer.parseInt(vEpInSeasonNumEditText));
        values.put(Series.COLUMN_STATE, vStateSpinner);
        values.put(Series.COLUMN_COUNTRY, vCountrySpinner);
        values.put(Series.COLUMN_AGE, Integer.parseInt(vAgeEditText));
        values.put(Series.COLUMN_GENRE, vGanreSpinner);
        values.put(Series.COLUMN_ACTOR, vActorSpinner);
        values.put(Series.COLUMN_PRODUCER, vProducerSpinner);
        values.put(Series.COLUMN_IMDB, Double.parseDouble(vImdbEditText));
        values.put(Series.COLUMN_KINOPOISK, Double.parseDouble(vKinopoiskEditText));
        values.put(Series.COLUMN_WANT, Integer.parseInt(vWantRadioGroup));
        values.put(Series.COLUMN_LINK, vLinkEditText);
        values.put(Series.COLUMN_DESCRIPTION, vDescriptionEditText);
        long newSeriasRowId = db.insert(Series.TABLE_NAME, null, values);
        Log.d("addSerias", "Added film with Row ID" + newSeriasRowId);

        values.clear();
        String query = "SELECT * FROM " + Actors.TABLE_NAME
                + " WHERE " + Actors.COLUMN_NAME + " = '" + vActorSpinner.split(" ")[0] + "'"
                + " AND " + Actors.COLUMN_SURNAME + " = '" + vActorSpinner.split(" ")[1] + "'";
        try (Cursor cursor = db.rawQuery(query, null)) {
            currentId = cursor.getPosition();
            values.put(ActorSeries.COLUMN_ACTOR_ID, currentId);
            values.put(ActorSeries.COLUMN_SERIES_ID, (int) newSeriasRowId);
            long newActorFilmRowId = db.insert(ActorSeries.TABLE_NAME, null, values);
            Log.d("addSerias", "Added ActorFilm with Row ID" + newActorFilmRowId);
        }

        values.clear();
        query = "SELECT * FROM " + Countries.TABLE_NAME
                + " WHERE " + Countries.COLUMN_NAME + " = '" + vCountrySpinner + "'";
        try (Cursor cursor = db.rawQuery(query, null)) {
            currentId = cursor.getPosition();
            values.put(CountrySeries.COLUMN_COUNTRY_ID, currentId);
            values.put(CountrySeries.COLUMN_SERIES_ID, (int) newSeriasRowId);
            long newCountryFilmRowId = db.insert(CountrySeries.TABLE_NAME, null, values);
            Log.d("addSerias", "Added CountryFilm with Row ID" + newCountryFilmRowId);
        }

        values.clear();
        query = "SELECT * FROM " + Genres.TABLE_NAME
                + " WHERE " + Genres.COLUMN_NAME + " = '" + vGanreSpinner + "'";
        try (Cursor cursor = db.rawQuery(query, null)) {
            currentId = cursor.getPosition();
            values.put(GenreSeries.COLUMN_GENRE_ID, currentId);
            values.put(GenreSeries.COLUMN_SERIES_ID, (int) newSeriasRowId);
            long newGanreFilmRowId = db.insert(GenreSeries.TABLE_NAME, null, values);
            Log.d("addSerias", "Added GanreFilm with Row ID" + newGanreFilmRowId);
        }

        values.clear();
        query = "SELECT * FROM " + Producers.TABLE_NAME
                + " WHERE " + Producers.COLUMN_NAME + " = '" + vProducerSpinner.split(" ")[0] + "'"
                + " AND surname = '" + vProducerSpinner.split(" ")[1] + "'";
        try (Cursor cursor = db.rawQuery(query, null)) {
            currentId = cursor.getPosition();
            values.put(ProducerSeries.COLUMN_PRODUCER_ID, currentId);
            values.put(ProducerSeries.COLUMN_SERIES_ID, (int) newSeriasRowId);
            long newProducerFilmRowId = db.insert(ProducerSeries.TABLE_NAME, null, values);
            Log.d("addSerias", "Added ProducerFilm with Row ID" + newProducerFilmRowId);
        }

        if (Integer.parseInt(vWantRadioGroup) == 1) {
            values.clear();
            values.put(WantToWatchSeries.COLUMN_SERIES_ID, (int) newSeriasRowId);
            values.put(WantToWatchSeries.COLUMN_ADD_DATE, Calendar.DATE);
            long newWantToWatchRowId = db.insert(WantToWatchSeries.TABLE_NAME, null, values);
            Log.d("addSerias", "Added WantToWatch with Row ID" + newWantToWatchRowId);

        } else if (Integer.parseInt(vWantRadioGroup) == 2) {
            values.clear();
            values.put(WatchedSeries.COLUMN_SERIES_ID, (int) newSeriasRowId);
            values.put(WatchedSeries.COLUMN_DATE, Calendar.DATE);
            long newWatchedRowId = db.insert(WatchedSeries.TABLE_NAME, null, values);
            Log.d("addSerias", "Added Watched with Row ID" + newWatchedRowId);
        }

    }

    private static String selectAllFromTableWhere(String TABLE_NAME, String COLUMN_NAME, String equalTo) {
        return String.format("SELECT * FROM %s WHERE %s = '%s'", TABLE_NAME, COLUMN_NAME, equalTo);
    }

    private static void cascadeAdd(SQLiteDatabase db, String query, String firstId, String secondId, int newFilmsRowId,
                                   String TABLE_NAME, String logMessageTableName) {
        try (Cursor cursor = db.rawQuery(query, null)) {
            if (cursor.getCount() != 0) {
                ContentValues values = new ContentValues();
                cursor.moveToNext();
                int currentId = cursor.getInt(0);
                values.put(firstId, currentId);
                values.put(secondId, newFilmsRowId);
                long newRowId = db.insert(TABLE_NAME, null, values);
                Log.d("addMovie", String.format("Added %s with Row ID%d", logMessageTableName, newRowId));
            }
        }
    }
}

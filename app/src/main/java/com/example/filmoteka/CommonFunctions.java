package com.example.filmoteka;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import data.*;

import java.util.Calendar;

import static data.FilmsContract.Films;
import static data.ActorsContract.Actors;
import static data.ActorFilmContract.ActorFilm;
import static data.CountriesContract.Countries;
import static data.GanreFilmContract.GanreFilm;
import static data.CountryFilmContract.CountryFilm;
import static data.GanresContract.Ganres;
import static data.ProducersContract.Producers;
import static data.ProducerFilmContract.ProducerFilm;

import static data.WatchedContract.Watched;
import static data.WantToWatchContract.WantToWatch;
import static data.SeriasContract.Serias;

public class CommonFunctions {
    public static void addMovie(String vNameEditText,
                                String vYearSpinner, String vCountrySpinner, String vAgeEditText, String vGanreSpinner,
                                String vActorSpinner, String vProducerSpinner,
                                String vImdbEditText, String vKinopoiskEditText,
                                String vWantRadioGroup,
                                String vDescriptionEditText, String vLinkEditText,
                                FilmraryDbHelper vDbHelper) {
        SQLiteDatabase db = vDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Films.COLUMN_NAME, vNameEditText);
        values.put(Films.COLUMN_YEAR, Integer.parseInt(vYearSpinner));
        values.put(Films.COLUMN_COUNTRY, vCountrySpinner);
        values.put(Films.COLUMN_AGE, vAgeEditText);
        values.put(Films.COLUMN_GANRE, vGanreSpinner);
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

        query = selectAllFromTableWhere(Ganres.TABLE_NAME, Ganres.COLUMN_NAME, vGanreSpinner);
        cascadeAdd(db, query, GanreFilm.COLUMN_GANRE_ID, GanreFilm.COLUMN_FILM_ID, newFilmsRowId, GanreFilm.TABLE_NAME,
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

    public static void addSerias(String vNameEditText, String vStartYearSpinner,
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
        values.put(Serias.COLUMN_NAME, vNameEditText);
        values.put(Serias.COLUMN_START_YEAR, Integer.parseInt(vStartYearSpinner));
        values.put(Serias.COLUMN_SEASONS_NUM, Integer.parseInt(vSeasonsNumEditText));
        values.put(Serias.COLUMN_EP_DURATION, Integer.parseInt(vEpDurationEditText));
        values.put(Serias.COLUMN_EP_IN_SEASON_NUM, Integer.parseInt(vEpInSeasonNumEditText));
        values.put(Serias.COLUMN_STATE, vStateSpinner);
        values.put(Serias.COLUMN_COUNTRY, vCountrySpinner);
        values.put(Serias.COLUMN_AGE, Integer.parseInt(vAgeEditText));
        values.put(Serias.COLUMN_GANRE, vGanreSpinner);
        values.put(Serias.COLUMN_ACTOR, vActorSpinner);
        values.put(Serias.COLUMN_PRODUCER, vProducerSpinner);
        values.put(Serias.COLUMN_IMDB, Double.parseDouble(vImdbEditText));
        values.put(Serias.COLUMN_KINOPOISK, Double.parseDouble(vKinopoiskEditText));
        values.put(Serias.COLUMN_WANT, Integer.parseInt(vWantRadioGroup));
        values.put(Serias.COLUMN_LINK, vLinkEditText);
        values.put(Serias.COLUMN_DESCRIPTION, vDescriptionEditText);
        long newSeriasRowId = db.insert(Serias.TABLE_NAME, null, values);
        Log.d("addSerias", "Added film with Row ID" + newSeriasRowId);

        values.clear();
        String query = "SELECT * FROM " + ActorsContract.Actors.TABLE_NAME
                + " WHERE " + ActorsContract.Actors.COLUMN_NAME + " = '" + vActorSpinner.split(" ")[0] + "'"
                + " AND " + ActorsContract.Actors.COLUMN_SURNAME + " = '" + vActorSpinner.split(" ")[1] + "'";
        try (Cursor cursor = db.rawQuery(query, null)) {
            currentId = cursor.getPosition();
            values.put(ActorSeriasContract.ActorSerias.COLUMN_ACTOR_ID, currentId);
            values.put(ActorSeriasContract.ActorSerias.COLUMN_SERIAS_ID, (int) newSeriasRowId);
            long newActorFilmRowId = db.insert(ActorSeriasContract.ActorSerias.TABLE_NAME, null, values);
            Log.d("addSerias", "Added ActorFilm with Row ID" + newActorFilmRowId);
        }

        values.clear();
        query = "SELECT * FROM " + CountriesContract.Countries.TABLE_NAME
                + " WHERE " + CountriesContract.Countries.COLUMN_NAME + " = '" + vCountrySpinner + "'";
        try (Cursor cursor = db.rawQuery(query, null)) {
            currentId = cursor.getPosition();
            values.put(CountrySeriasContract.CountrySerias.COLUMN_COUNTRY_ID, currentId);
            values.put(CountrySeriasContract.CountrySerias.COLUMN_SERIAS_ID, (int) newSeriasRowId);
            long newCountryFilmRowId = db.insert(CountrySeriasContract.CountrySerias.TABLE_NAME, null, values);
            Log.d("addSerias", "Added CountryFilm with Row ID" + newCountryFilmRowId);
        }

        values.clear();
        query = "SELECT * FROM " + GanresContract.Ganres.TABLE_NAME
                + " WHERE " + GanresContract.Ganres.COLUMN_NAME + " = '" + vGanreSpinner + "'";
        try (Cursor cursor = db.rawQuery(query, null)) {
            currentId = cursor.getPosition();
            values.put(GanreSeriasContract.GanreSerias.COLUMN_GANRE_ID, currentId);
            values.put(GanreSeriasContract.GanreSerias.COLUMN_SERIAS_ID, (int) newSeriasRowId);
            long newGanreFilmRowId = db.insert(GanreSeriasContract.GanreSerias.TABLE_NAME, null, values);
            Log.d("addSerias", "Added GanreFilm with Row ID" + newGanreFilmRowId);
        }

        values.clear();
        query = "SELECT * FROM " + ProducersContract.Producers.TABLE_NAME
                + " WHERE " + ProducersContract.Producers.COLUMN_NAME + " = '" + vProducerSpinner.split(" ")[0] + "'"
                + " AND surname = '" + vProducerSpinner.split(" ")[1] + "'";
        try (Cursor cursor = db.rawQuery(query, null)) {
            currentId = cursor.getPosition();
            values.put(ProducerSeriasContract.ProducerSerias.COLUMN_PRODUCER_ID, currentId);
            values.put(ProducerSeriasContract.ProducerSerias.COLUMN_SERIAS_ID, (int) newSeriasRowId);
            long newProducerFilmRowId = db.insert(ProducerSeriasContract.ProducerSerias.TABLE_NAME, null, values);
            Log.d("addSerias", "Added ProducerFilm with Row ID" + newProducerFilmRowId);
        }

        if (Integer.parseInt(vWantRadioGroup) == 1) {
            values.clear();
            values.put(WantToWatchSeriasContract.WantToWatchSerias.COLUMN_SERIAS_ID, (int) newSeriasRowId);
            values.put(WantToWatchSeriasContract.WantToWatchSerias.COLUMN_ADD_DATE, Calendar.DATE);
            long newWantToWatchRowId = db.insert(WantToWatchSeriasContract.WantToWatchSerias.TABLE_NAME, null, values);
            Log.d("addSerias", "Added WantToWatch with Row ID" + newWantToWatchRowId);

        } else if (Integer.parseInt(vWantRadioGroup) == 2) {
            values.clear();
            values.put(WatchedSeriasContract.WatchedSerias.COLUMN_SERIAS_ID, (int) newSeriasRowId);
            values.put(WatchedSeriasContract.WatchedSerias.COLUMN_DATE, Calendar.DATE);
            long newWatchedRowId = db.insert(WatchedSeriasContract.WatchedSerias.TABLE_NAME, null, values);
            Log.d("addSerias", "Added Watched with Row ID" + newWatchedRowId);
        }

    }

    private static String selectAllFromTableWhere(String TABLE_NAME, String COLUMN_NAME, String equalTo) {
        return String.format("SELECT * FROM %s WHERE %s = '%s'", TABLE_NAME, COLUMN_NAME, equalTo);
    }

    private static void cascadeAdd(SQLiteDatabase db, String query, String firstId, String secondId, int newFilmsRowId,
                                   String TABLE_NAME, String logMessageTableName) {
        try (Cursor cursor = db.rawQuery(query, null)) {
            ContentValues values = new ContentValues();
            int currentId = cursor.getPosition();
            values.put(firstId, currentId);
            values.put(secondId, newFilmsRowId);
            long newRowId = db.insert(TABLE_NAME, null, values);
            Log.d("addMovie", String.format("Added %s with Row ID%d", logMessageTableName, newRowId));
        }
    }
}

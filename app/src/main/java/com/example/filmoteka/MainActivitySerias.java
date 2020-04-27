package com.example.filmoteka;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.util.Log;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import data.ActorSeriasContract;
import data.ActorsContract;
import data.CountriesContract;
import data.CountrySeriasContract;
import data.GanreSeriasContract;
import data.ProducerSeriasContract;
import data.SeriasContract;
import data.SeriasContract.Serias;
import data.FilmraryDbHelper;
import data.GanresContract;
import data.ProducersContract;
import data.WantToWatchSeriasContract;
import data.WatchedSeriasContract;


public class MainActivitySerias extends AppCompatActivity {

    final int COLUMN_NAME = 1;
    public FilmraryDbHelper vDbHelper;

    String name;
    String start_year;
    String seasons_num;
    String ep_duration;
    String ep_in_season_num;
    String state;
    String country;
    String age;
    String ganre;
    String actor;
    String producer;
    String imdb;
    String kinopoisk;
    String want;
    String link;
    String description;

    boolean fromEditor = false;

    ArrayList<String> listItem;
    ArrayAdapter<String> adapter;
    ListView moviesListView;

    ArrayList<String> countries;
    ArrayList<String> ganres;
    ArrayList<String> actors;
    ArrayList<String> producers;

    // main serias window
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_serias);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // all movies are showed in tablesListView as a list with clickable elements
        moviesListView = findViewById(R.id.tables_list_view);

        // for work with db
        vDbHelper = FilmraryDbHelper.getInstance(this);

        // Вроде сделал todo_: fill setupSpinner() voids
        //todo: each Array List should be filled with info from table with same name (actors from Actors)
        listItem = new ArrayList<>();
        countries = new ArrayList<>();
        ganres = new ArrayList<>();
        actors = new ArrayList<>();
        producers = new ArrayList<>();

        // to show the list of movies
        viewMovies();

        // waits for click on list element
        // now if it is clicked - it deletes  (no eto ne tochno)
        moviesListView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(MainActivitySerias.this, SeriasInfo.class);
            String serias = (String) moviesListView.getItemAtPosition(position);
            String[] seriasString = searchSeriasByName(serias);
            for (int i = 0; i < Serias.COLUMNS.length; ++i) {
                intent.putExtra(Serias.COLUMNS[i], seriasString[i]);
            }
            startActivity(intent);
        });

        // add button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            // if clicked - new intent(window) opens
            Intent intent = new Intent(MainActivitySerias.this, EditorActivitySerias.class);
            startActivity(intent);
        });

        Intent thisIntent = getIntent();
        parseIntentWithFilm(thisIntent);

        if (fromEditor) {
            addSerias(name, start_year, seasons_num, ep_duration, ep_in_season_num, state,
                      country, age, ganre, actor, producer,
                      imdb, kinopoisk,
                      want,
                      description, link);
            listItem.clear();
            viewMovies();
        }
        fromEditor = false;
    }

    private void parseIntentWithFilm(Intent intent) {
        Bundle filmInfo = intent.getExtras();
        if (filmInfo == null || filmInfo.isEmpty()) {
            return;
        }
        name = intent.getStringExtra(Serias.COLUMN_NAME);
        start_year = intent.getStringExtra(Serias.COLUMN_START_YEAR);
        seasons_num = intent.getStringExtra(Serias.COLUMN_SEASONS_NUM);
        ep_duration = intent.getStringExtra(Serias.COLUMN_EP_DURATION);
        ep_in_season_num = intent.getStringExtra(Serias.COLUMN_EP_IN_SEASON_NUM);
        state = intent.getStringExtra(Serias.COLUMN_STATE);
        country = intent.getStringExtra(Serias.COLUMN_COUNTRY);
        age = intent.getStringExtra(Serias.COLUMN_AGE);
        ganre = intent.getStringExtra(Serias.COLUMN_GANRE);
        actor = intent.getStringExtra(Serias.COLUMN_ACTOR);
        producer = intent.getStringExtra(Serias.COLUMN_PRODUCER);
        imdb = intent.getStringExtra(Serias.COLUMN_IMDB);
        kinopoisk = intent.getStringExtra(Serias.COLUMN_KINOPOISK);
        want = intent.getStringExtra(Serias.COLUMN_WANT);
        link = intent.getStringExtra(Serias.COLUMN_LINK);
        description = intent.getStringExtra(Serias.COLUMN_DESCRIPTION);
        fromEditor = intent.getBooleanExtra("fromEditor", fromEditor);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //displayDatabaseInfo();
    }


    // search
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.item_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<String> seriasList = new ArrayList<>();

                for (String serias : listItem) {
                    if (serias.toLowerCase().contains(newText.toLowerCase())) {
                        seriasList.add(serias);
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivitySerias.this,
                                                                  android.R.layout.simple_list_item_1,
                                                                  seriasList);
                moviesListView.setAdapter(adapter);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivitySerias.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    // todo: заполнить спиннеры
    // todo: сделать опрятную кнопку добавить (мб всплывающее окно, мб всплывающие кнопки)
    // todo: сделать заполнение таблиц актеры, жанры и пр по кнопкам
    // todo: сделать что-то (чекбоксы, скроллвью) для просмотра всех таблиц для данной коллекции
    // todo: поработать над ограничениями
    // todo: добавить многострочность для больших полей (описание)
    // todo: в Инфо сделать что-то типа "Посмотреть каст" и туда жахнуть запросом таблицу атеров
    // todo: сделать нечувствительными к регистру все поля
    // todo: расшарить все функции для фильмов на сериалы
    // todo: заменить редактируемый возраст на стандартный в spinner'e
    // todo: сделать сортировку таблиц

    // add data
    private void addSerias(String vNameEditText, String vStartYearSpinner,
                           String vSeasonsNumEditText, String vEpDurationEditText,
                           String vEpInSeasonNumEditText, String vStateSpinner,
                           String vCountrySpinner, String vAgeEditText, String vGanreSpinner,
                           String vActorSpinner, String vProducerSpinner,
                           String vImdbEditText, String vKinopoiskEditText,
                           String vWantRadioGroup, String vLinkEditText,
                           String vDescriptionEditText) {

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

    // show data in ListView
    public void viewMovies() {
        SQLiteDatabase db = vDbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + SeriasContract.Serias.TABLE_NAME;
        try (Cursor cursor = db.rawQuery(query, null)) {

            if (cursor.getCount() == 0) {
                Toast.makeText(this, "No data in serias db", Toast.LENGTH_SHORT).show();
            } else {
                while (cursor.moveToNext()) {
                    listItem.add(cursor.getString(COLUMN_NAME)); // 1 for name, 0 for index
                }
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItem);
                moviesListView.setAdapter(adapter);
            }
        }
    }

    // todo: variety of indexes
    // find movie by some id
    public void searchSerias(String text) {
        SQLiteDatabase db = vDbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + SeriasContract.Serias.TABLE_NAME
                + " WHERE " + SeriasContract.Serias.COLUMN_NAME
                + " LIKE  '%" + text + "%'";
        try (Cursor cursor = db.rawQuery(query, null)) {

            if (cursor.getCount() == 0) {
                Toast.makeText(this, "No such data in db", Toast.LENGTH_SHORT).show();
            } else {
                while (cursor.moveToNext()) {
                    listItem.add(cursor.getString(COLUMN_NAME)); // 1 for name, 0 for index
                }
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItem);
                moviesListView.setAdapter(adapter);
            }
        }
    }

    public String[] searchSeriasByName(String name) {
        SQLiteDatabase db = vDbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + SeriasContract.Serias.TABLE_NAME
                + " WHERE (" + SeriasContract.Serias.COLUMN_NAME + ") = '" + name + "'";
        String[] result = new String[Serias.COLUMNS.length];
        try (Cursor cursor = db.rawQuery(query, null)) {

            if (cursor.getCount() == 0) {
                Toast.makeText(this, "No such data in db", Toast.LENGTH_SHORT).show();
            } else {
                while (cursor.moveToNext()) {
                    for (int i = 0; i < Serias.COLUMNS.length; ++i) {
                        result[i] = cursor.getString(i);
                    }
                }
            }
        }
        return result;
    }

}

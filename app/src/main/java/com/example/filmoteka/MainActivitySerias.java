package com.example.filmoteka;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import data.SeriesContract.Series;
import data.FilmraryDbHelper;


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

        // to show the list of movies
        viewMovies();

        // waits for click on list element
        moviesListView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(MainActivitySerias.this, SeriasInfo.class);
            String serias = (String) moviesListView.getItemAtPosition(position);
            String[] seriasString = searchSeriasByName(serias);
            for (int i = 0; i < Series.COLUMNS.length; ++i) {
                intent.putExtra(Series.COLUMNS[i], seriasString[i]);
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
            CommonFunctions.addSerias(name, start_year,
                                      seasons_num,
                                      ep_duration, ep_in_season_num,
                                      state, country, age, ganre, actor, producer,
                                      imdb, kinopoisk,
                                      want, link, description, vDbHelper);
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
        name = intent.getStringExtra(Series.COLUMN_NAME);
        start_year = intent.getStringExtra(Series.COLUMN_START_YEAR);
        seasons_num = intent.getStringExtra(Series.COLUMN_SEASONS_NUM);
        ep_duration = intent.getStringExtra(Series.COLUMN_EP_DURATION);
        ep_in_season_num = intent.getStringExtra(Series.COLUMN_EP_IN_SEASON_NUM);
        state = intent.getStringExtra(Series.COLUMN_STATE);
        country = intent.getStringExtra(Series.COLUMN_COUNTRY);
        age = intent.getStringExtra(Series.COLUMN_AGE);
        ganre = intent.getStringExtra(Series.COLUMN_GENRE);
        actor = intent.getStringExtra(Series.COLUMN_ACTOR);
        producer = intent.getStringExtra(Series.COLUMN_PRODUCER);
        imdb = intent.getStringExtra(Series.COLUMN_IMDB);
        kinopoisk = intent.getStringExtra(Series.COLUMN_KINOPOISK);
        want = intent.getStringExtra(Series.COLUMN_WANT);
        link = intent.getStringExtra(Series.COLUMN_LINK);
        description = intent.getStringExtra(Series.COLUMN_DESCRIPTION);
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

    // todo: поработать над ограничениями
    // todo: добавить многострочность для больших полей (описание)
    // todo: в Инфо сделать что-то типа "Посмотреть каст" и туда жахнуть запросом таблицу атеров
    // todo: сделать нечувствительными к регистру все поля
    // todo: расшарить все функции для фильмов на сериалы
    // todo: заменить редактируемый возраст на стандартный в spinner'e
    // todo: сделать сортировку таблиц


    // show data in ListView
    public void viewMovies() {
        SQLiteDatabase db = vDbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + Series.TABLE_NAME;
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
        String query = "SELECT * FROM " + Series.TABLE_NAME
                       + " WHERE " + Series.COLUMN_NAME
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
        String query = "SELECT * FROM " + Series.TABLE_NAME
                       + " WHERE (" + Series.COLUMN_NAME + ") = '" + name + "'";
        String[] result = new String[Series.COLUMNS.length];
        try (Cursor cursor = db.rawQuery(query, null)) {

            if (cursor.getCount() == 0) {
                Toast.makeText(this, "No such data in db", Toast.LENGTH_SHORT).show();
            } else {
                while (cursor.moveToNext()) {
                    for (int i = 0; i < Series.COLUMNS.length; ++i) {
                        result[i] = cursor.getString(i);
                    }
                }
            }
        }
        return result;
    }

}

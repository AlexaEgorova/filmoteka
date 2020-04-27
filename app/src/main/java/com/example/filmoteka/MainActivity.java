package com.example.filmoteka;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import data.*;
import data.FilmsContract.Films;


public class MainActivity extends AppCompatActivity {

    final int COLUMN_NAME = 1;
    public FilmraryDbHelper vDbHelper;

    final int FILMS = 0;
    final int COUNTRIES = 1;
    final int GENRES = 2;
    final int ACTORS = 3;
    final int PRODUCERS = 4;
    final int WANT = 5;
    final int WATCHED = 6;

    int tableSpinnerMode = FILMS;

    String name;
    String year;
    String country;
    String age;
    String ganre;
    String actor;
    String producer;
    String imdb;
    String kinopoisk;
    String want;
    String description;
    String link;
    boolean fromEditor = false;

    ArrayList<String> listItem;
    ArrayAdapter<String> adapter;

    ListView tablesListView;
    Spinner fTablesSpinner;

    // main window
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // all movies are showed in tablesListView as a list with clickable elements
        tablesListView = findViewById(R.id.tables_list_view);
        fTablesSpinner = findViewById(R.id.tables_spinner);
        setupTablesSpinner();

        // for work with db
        vDbHelper = FilmraryDbHelper.getInstance(this);

        listItem = new ArrayList<>();

        fTablesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selection = (String) parentView.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals("Фильмы")) {
                        listItem.clear();
                        viewTableData(Films.TABLE_NAME);
                        tableSpinnerMode = FILMS;
                    } else if (selection.equalsIgnoreCase("актёры")) {
                        listItem.clear();
                        viewTableData(ActorsContract.Actors.TABLE_NAME, 2);
                        tableSpinnerMode = ACTORS;
                    } else if (selection.equalsIgnoreCase("продюсеры")) {
                        listItem.clear();
                        viewTableData(ProducersContract.Producers.TABLE_NAME, 2);
                        tableSpinnerMode = PRODUCERS;
                    }  else if (selection.equalsIgnoreCase("жанры")) {
                        listItem.clear();
                        viewTableData(GanresContract.Ganres.TABLE_NAME);
                        tableSpinnerMode = GENRES;
                    }  else if (selection.equalsIgnoreCase("хочу посмотреть")) {
                        listItem.clear();
                        viewTableData(WantToWatchContract.WantToWatch.TABLE_NAME);
                        tableSpinnerMode = WANT;
                    }  else if (selection.equalsIgnoreCase("посмотрел")) {
                        listItem.clear();
                        viewTableData(WatchedContract.Watched.TABLE_NAME);
                        tableSpinnerMode = WATCHED;
                    } else { //if (selection.equals("Страны"))
                        listItem.clear();
                        viewTableData(CountriesContract.Countries.TABLE_NAME);
                        tableSpinnerMode = COUNTRIES;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                viewTableData(Films.TABLE_NAME);
            }

        });
        // to show the list of movies
        //viewTableData();

        tablesListView.setOnItemClickListener((parent, view, position, id) -> {
            String name = (String) tablesListView.getItemAtPosition(position);
            switch(tableSpinnerMode) {
                case FILMS: {
                    Intent intent = new Intent(MainActivity.this, FilmInfo.class);
                    String film = (String) tablesListView.getItemAtPosition(position);
                    String[] filmString = searchMovieByName(film);
                    for (int i = 0; i < Films.COLUMNS.length; ++i) {
                        intent.putExtra(Films.COLUMNS[i], filmString[i]);
                    }
                    startActivity(intent);
                    break;
                }
                case COUNTRIES: {
                    boolean done = deleteCountry(name);
                    listItem.clear();
                    viewTableData(CountriesContract.Countries.TABLE_NAME);
                    break;
                }
                default:
                    Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
//                case ACTORS: {
//                    deleteByName(ActorsContract.Actors.TABLE_NAME, name);
//                }
            }
        });

        Intent thisIntent = getIntent();
        parseIntentWithFilm(thisIntent);

        if (fromEditor) {
            CommonFunctions.addMovie(name,
                                     year, country, age, ganre, actor, producer,
                                     imdb, kinopoisk,
                                     want, link,
                                     description, vDbHelper);
            listItem.clear();
            fTablesSpinner.setSelection(0);
            viewTableData(Films.TABLE_NAME);
        }
        fromEditor = false;
    }

    private void setupTablesSpinner() {
        //todo: add genres
        //todo: add producers
        //todo: add want_to_watch
        //todo: add watched
        String[] tables = {"Фильмы", "Страны", "Актёры", "Продюсеры", "Жанры", "Хочу посмотреть", "Посмотрел"};
        fTablesSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tables));
        fTablesSpinner.setSelection(0);
    }

    private void parseIntentWithFilm(Intent intent) {
        Bundle filmInfo = intent.getExtras();

        if (filmInfo == null || filmInfo.isEmpty()) {
            return;
        }

        name = intent.getStringExtra(Films.COLUMN_NAME);
        year = intent.getStringExtra(Films.COLUMN_YEAR);
        country = intent.getStringExtra(Films.COLUMN_COUNTRY);
        age = intent.getStringExtra(Films.COLUMN_AGE);
        ganre = intent.getStringExtra(Films.COLUMN_GANRE);
        actor = intent.getStringExtra(Films.COLUMN_ACTOR);
        producer = intent.getStringExtra(Films.COLUMN_PRODUCER);
        imdb = intent.getStringExtra(Films.COLUMN_IMDB);
        kinopoisk = intent.getStringExtra(Films.COLUMN_KINOPOISK);
        want = intent.getStringExtra(Films.COLUMN_WANT);
        description = intent.getStringExtra(Films.COLUMN_DESCRIPTION);
        link = intent.getStringExtra(Films.COLUMN_LINK);
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
                ArrayList<String> moviesList = new ArrayList<>();

                for (String movie : listItem) {
                    if (movie.toLowerCase().contains(newText.toLowerCase())) {
                        moviesList.add(movie);
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this,
                                                                  android.R.layout.simple_list_item_1,
                                                                  moviesList);
                tablesListView.setAdapter(adapter);
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
            Intent intent = new Intent(MainActivity.this, MainActivitySerias.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    // add data
    public void viewTableData(String tableName, int numberOfColumns) {
        SQLiteDatabase db = vDbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + tableName;
        try (Cursor cursor = db.rawQuery(query, null)) {

            if (cursor.getCount() == 0) {
                Toast.makeText(this, "No data in db", Toast.LENGTH_SHORT).show();
            } else {
                while (cursor.moveToNext()) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 1; i <= numberOfColumns; ++i) {
                        sb.append(cursor.getString(i));
                        if (i != numberOfColumns) {
                            sb.append(" ");
                        }
                    }
                    listItem.add(sb.toString());
                }
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItem);
                tablesListView.setAdapter(adapter);
            }
        }
    }

    // show data in ListView
    public void viewTableData(String tableName) {
        SQLiteDatabase db = vDbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + tableName;
        try (Cursor cursor = db.rawQuery(query, null)) {

            if (cursor.getCount() == 0) {
                Toast.makeText(this, "No data in db", Toast.LENGTH_SHORT).show();
            } else {
                while (cursor.moveToNext()) {
                    listItem.add(cursor.getString(COLUMN_NAME)); // 1 for name, 0 for index
                }
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItem);
                tablesListView.setAdapter(adapter);
            }
        }
    }

    public void viewTableDataWatch(String tableName) {
        SQLiteDatabase db = vDbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + tableName;
        ArrayList<String> ids = new ArrayList<>();
        try (Cursor cursor = db.rawQuery(query, null)) {

            if (cursor.getCount() == 0) {
                Toast.makeText(this, "No data in db", Toast.LENGTH_SHORT).show();
            } else {
                while (cursor.moveToNext()) {
                    ids.add(cursor.getString(COLUMN_NAME)); // 1 for name, 0 for index
                }
            }
        }
        for (String id : ids) {
            ArrayList<String> movies = searchMovie(Integer.parseInt(id));
            if (!movies.isEmpty()) {
                listItem.add(movies.get(0));
            }
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItem);
        tablesListView.setAdapter(adapter);
    }

    public ArrayList<String> searchMovie(int id) {
        SQLiteDatabase db = vDbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + FilmsContract.Films.TABLE_NAME
                + " WHERE " + Films._ID + " = " + id;
        ArrayList<String> list = new ArrayList<>();
        try (Cursor cursor = db.rawQuery(query, null)) {

            if (cursor.getCount() == 0) {
                Toast.makeText(this, "No such data in db", Toast.LENGTH_SHORT).show();
            } else {
                while (cursor.moveToNext()) {
                    list.add(cursor.getString(1)); // 1 for name, 0 for index
                }
            }
        }
        return list;
    }

    // todo: variety of indexes
    // find movie by some id
    public void searchMovies(String text) {
        SQLiteDatabase db = vDbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + FilmsContract.Films.TABLE_NAME
                + " WHERE " + FilmsContract.Films.COLUMN_NAME
                + " LIKE  '%" + text + "%'";
        try (Cursor cursor = db.rawQuery(query, null)) {

            if (cursor.getCount() == 0) {
                Toast.makeText(this, "No such data in db", Toast.LENGTH_SHORT).show();
            } else {
                while (cursor.moveToNext()) {
                    listItem.add(cursor.getString(COLUMN_NAME)); // 1 for name, 0 for index
                }
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItem);
                tablesListView.setAdapter(adapter);
            }
        }
    }

    public String[] searchMovieByName(String name) {
        SQLiteDatabase db = vDbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + FilmsContract.Films.TABLE_NAME
                + " WHERE (" + FilmsContract.Films.COLUMN_NAME + ") = '" + name + "'";
        String[] result = new String[Films.COLUMNS.length];
        try (Cursor cursor = db.rawQuery(query, null)) {

            if (cursor.getCount() == 0) {
                Toast.makeText(this, "No such data in db", Toast.LENGTH_SHORT).show();
            } else {
                while (cursor.moveToNext()) {
                    for (int i = 0; i < Films.COLUMNS.length; ++i) {
                        result[i] = cursor.getString(i);
                    }
                }
            }
        }
        return result;
    }

    public void addMovie(View view) {
        Intent intent = new Intent(MainActivity.this, EditorActivity.class);
        startActivity(intent);
    }

//    public boolean deleteActor(String name) {
//        SQLiteDatabase db = vDbHelper.getWritableDatabase();
//        String queryActors = "SELECT * FROM " + ActorsContract.Actors.TABLE_NAME + "WHERE";
//        String queryActorFilm = "SELECT * FROM " + ActorFilmContract.ActorFilm.TABLE_NAME;
//        String queryActorSeries = "SELECT * FROM " + ActorSeriasContract.ActorSerias.TABLE_NAME;
//        int currentid;
//        // get id
//        try (Cursor cursor = db.rawQuery(queryActors, null)) {
//            if (cursor.getCount() != 0) {
//                while (cursor.moveToNext()) {
//                    countries.add(cursor.getString(1));
//                }
//            }
//        }
//    }

    public boolean deleteCountry(String name) {
        SQLiteDatabase db = vDbHelper.getWritableDatabase();
        int found_id = 0;

        String idQuery = "SELECT * FROM " + CountriesContract.Countries.TABLE_NAME
                + " WHERE " + CountriesContract.Countries.COLUMN_NAME + " = '"
                + name + "'";
        try (Cursor cursor = db.rawQuery(idQuery, null)) {
            int idColumnIndex = cursor.getColumnIndex(CountriesContract.Countries._ID);
            if (cursor.getCount() != 0) {
                while (cursor.moveToNext())
                    found_id = cursor.getInt(idColumnIndex);
            }
            else
                Toast.makeText(this, "No such!", Toast.LENGTH_SHORT).show();
        }

        String queryCountryFilm = "SELECT * FROM " + CountryFilmContract.CountryFilm.TABLE_NAME
                + " WHERE " + CountryFilmContract.CountryFilm.COLUMN_COUNTRY_ID
                + " = '" + Integer.toString(found_id) + "'";
        try (Cursor cursor = db.rawQuery(queryCountryFilm, null)) {
            if (cursor.getCount() != 0) {
                Toast.makeText(this, "This country is needed for films!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        String queryCountrySeries = "SELECT * FROM " + CountrySeriasContract.CountrySerias.TABLE_NAME
                + " WHERE " + CountrySeriasContract.CountrySerias.COLUMN_COUNTRY_ID
                + " = '" + Integer.toString(found_id) + "'";
        try (Cursor cursor = db.rawQuery(queryCountrySeries, null)) {
            if (cursor.getCount() != 0) {
                Toast.makeText(this, "This country is needed for series!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        int deleted = db.delete(CountriesContract.Countries.TABLE_NAME,
                CountriesContract.Countries._ID + " = '" + Integer.toString(found_id) + "'",
                null);
        if (deleted == 0)
            return false;
        Toast.makeText(this, "Deleted row " + Integer.toString(deleted) + "!", Toast.LENGTH_SHORT).show();
        return true;
    }
}

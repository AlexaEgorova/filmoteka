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

import data.ActorsContract;
import data.CountriesContract;
import data.FilmsContract;
import data.FilmsContract.Films;
import data.FilmraryDbHelper;


public class MainActivity extends AppCompatActivity {

    final int COLUMN_NAME = 1;
    public FilmraryDbHelper vDbHelper;

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
                    } else if (selection.equalsIgnoreCase("актёры")) {
                        listItem.clear();
                        viewTableData(ActorsContract.Actors.TABLE_NAME, 2);
                    } else { //if (selection.equals("Страны"))
                        listItem.clear();
                        viewTableData(CountriesContract.Countries.TABLE_NAME);
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
            Intent intent = new Intent(MainActivity.this, FilmInfo.class);
            String film = (String) tablesListView.getItemAtPosition(position);
            String[] filmString = searchMovieByName(film);
            for (int i = 0; i < Films.COLUMNS.length; ++i) {
                intent.putExtra(Films.COLUMNS[i], filmString[i]);
            }
            startActivity(intent);
        });

        Intent thisIntent = getIntent();
        parseIntentWithFilm(thisIntent);

        if (fromEditor) {
            CommonFunctions.addMovie(name,
                                     year, country, age, ganre, actor, producer,
                                     imdb, kinopoisk,
                                     want,
                                     description, link, vDbHelper);
            listItem.clear();
            fTablesSpinner.setSelection(0);
            viewTableData(Films.TABLE_NAME);
        }
        fromEditor = false;
    }

    private void setupTablesSpinner() {
        //todo: add genres
        //todo: add producers
        //todo: add actors
        //todo: add want_to_watch
        //todo: add watched
        String[] tables = {"Фильмы", "Страны", "Актёры"};
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
}

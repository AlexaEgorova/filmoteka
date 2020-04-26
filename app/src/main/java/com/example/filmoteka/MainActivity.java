package com.example.filmoteka;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

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
    ListView moviesListView;

    ArrayList<String> countries;
    ArrayList<String> ganres;
    ArrayList<String> actors;
    ArrayList<String> producers;

    // main window
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // all movies are showed in moviesListView as a list with clickable elements
        moviesListView = findViewById(R.id.movies_list_view);


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

        moviesListView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(MainActivity.this, FilmInfo.class);
            String film = (String) moviesListView.getItemAtPosition(position);
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
            viewMovies();
        }
        fromEditor = false;
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

//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    private void displayDatabaseInfo() {
//        SQLiteDatabase db = vDbHelper.getReadableDatabase();
//
//        String[] projection = {
//                Films._ID,
//                Films.COLUMN_NAME,
//                Films.COLUMN_YEAR,
//                Films.COLUMN_COUNTRY,
//                Films.COLUMN_GANRE,
//                Films.COLUMN_ACTOR,
//                Films.COLUMN_PRODUCER,
//                Films.COLUMN_IMDB,
//                Films.COLUMN_KINOPOISK,
//                Films.COLUMN_WANT,
//                Films.COLUMN_DESCRIPTION};
//
//        // query
//        try (Cursor cursor = db.query(
//                Films.TABLE_NAME,
//                projection,
//                null, null, null, null, null)) {
//            TextView displayTextView = findViewById(R.id.text_view_info);
//            displayTextView.setText("Количество фильмов в приложении: " + cursor.getCount() + " \n\n");
//            displayTextView.append(Films._ID + " - " +
//                    Films.COLUMN_NAME + " - " +
//                    Films.COLUMN_YEAR + " - " +
//                    Films.COLUMN_COUNTRY + " - " +
//                    Films.COLUMN_GANRE + " - " +
//                    Films.COLUMN_ACTOR + " - " +
//                    Films.COLUMN_PRODUCER + " - " +
//                    Films.COLUMN_IMDB + " - " +
//                    Films.COLUMN_KINOPOISK + " - " +
//                    Films.COLUMN_WANT + " - " +
//                    Films.COLUMN_DESCRIPTION + "\n");
//
//            int idColumnIndex = cursor.getColumnIndex(Films._ID);
//            int nameColumnIndex = cursor.getColumnIndex(Films.COLUMN_NAME);
//            int yearColumnIndex = cursor.getColumnIndex(Films.COLUMN_YEAR);
//            int countryColumnIndex = cursor.getColumnIndex(Films.COLUMN_COUNTRY);
//            int ganreColumnIndex = cursor.getColumnIndex(Films.COLUMN_GANRE);
//            int actorColumnIndex = cursor.getColumnIndex(Films.COLUMN_ACTOR);
//            int producerColumnIndex = cursor.getColumnIndex(Films.COLUMN_PRODUCER);
//            int imdbColumnIndex = cursor.getColumnIndex(Films.COLUMN_IMDB);
//            int kinopoiskColumnIndex = cursor.getColumnIndex(Films.COLUMN_KINOPOISK);
//            int wantColumnIndex = cursor.getColumnIndex(Films.COLUMN_WANT);
//            int descriptionColumnIndex = cursor.getColumnIndex(Films.COLUMN_DESCRIPTION);
//
//            while (cursor.moveToNext()) {
//                int currentId = (idColumnIndex >= 0) ? cursor.getInt(idColumnIndex) : -1;
//                String currentName = (idColumnIndex >= 0) ? cursor.getString(nameColumnIndex) : "Null";
//                int currentYear = (idColumnIndex >= 0) ? cursor.getInt(yearColumnIndex) : -1;
//                String currentCountry = (idColumnIndex >= 0) ? cursor.getString(countryColumnIndex) : "Null";
//                String currentGanre = (idColumnIndex >= 0) ? cursor.getString(ganreColumnIndex) : "Null";
//                String currentActor = (idColumnIndex >= 0) ? cursor.getString(actorColumnIndex) : "Null";
//                String currentProducer = (idColumnIndex >= 0) ? cursor.getString(producerColumnIndex) : "Null";
//                double currentImdb = (idColumnIndex >= 0) ? cursor.getDouble(imdbColumnIndex) : -1;
//                double currentKinopoisk = (idColumnIndex >= 0) ? cursor.getDouble(kinopoiskColumnIndex) : -1;
//                int currentWant = (idColumnIndex >= 0) ? cursor.getInt(wantColumnIndex) : -1;
//                String currentDescription = (idColumnIndex >= 0) ? cursor.getString(descriptionColumnIndex) : "Null";
//
//                displayTextView.append(("\n" + currentId + " - " +
//                        currentName + " - " +
//                        currentYear + " - " +
//                        currentCountry + " - " +
//                        currentGanre + " - " +
//                        currentActor + " - " +
//                        currentProducer + " - " +
//                        currentImdb + " - " +
//                        currentKinopoisk + " - " +
//                        currentWant + " - " +
//                        currentDescription));
//            }
//        }
//    }

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
            Intent intent = new Intent(MainActivity.this, MainActivitySerias.class);
            startActivity(intent);
//            AlertDialog dialog = DialogScreen.getDialog(this);
//            dialog.show();
//            initSettings(dialog);

        }

        return super.onOptionsItemSelected(item);
    }

//    private void initSettings(AlertDialog dialog) {
//        // Определяем SeekBar и привязываем к нему дельты настроек
//        SeekBar sb_sense = (SeekBar) dialog.findViewById(R.id.seekSense);
//        SeekBar sb_vol = (SeekBar) dialog.findViewById(R.id.seekVol);
//        // Задаем этим SeekBar текущие значения настроек
//        sb_sense.setProgress(76);
//        sb_vol.setProgress(11);
//    }

    // add data

    // show data in ListView
    public void viewMovies() {
        SQLiteDatabase db = vDbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + FilmsContract.Films.TABLE_NAME;
        try (Cursor cursor = db.rawQuery(query, null)) {

            if (cursor.getCount() == 0) {
                Toast.makeText(this, "No data in db", Toast.LENGTH_SHORT).show();
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
                moviesListView.setAdapter(adapter);
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

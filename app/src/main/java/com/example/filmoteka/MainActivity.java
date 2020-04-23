package com.example.filmoteka;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;

import data.ActorFilmContract;
import data.ActorsContract;
import data.CountriesContract;
import data.CountryFilmContract;
import data.FilmsContract;
import data.FilmsContract.Films;
import data.FilmraryDbHelper;
import data.GanreFilmContract;
import data.GanresContract;
import data.ProducerFilmContract;
import data.ProducersContract;
import data.WantToWatchContract;
import data.WatchedContract;

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
        vDbHelper = new FilmraryDbHelper(this);

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
        // todo: on click open new intent(window) with all info about chosen movie
        // todo: new intent means new .xml file
        moviesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                SQLiteDatabase db = vDbHelper.getWritableDatabase();
                //String whereclause = "_id = '" + id + "' ";
                //String whereclause = "_id = '" + Long.toString(id) + "' ";
                //int delCnt = db.delete(Films.TABLE_NAME, Films._ID + " = '" + Long.toString(id) + "' ", null);
                //int delCnt = db.delete(Films.TABLE_NAME, "_ID = " + Long.toString(id), null);
                //int delCnt = db.delete(Films.TABLE_NAME, "_ID" + " = ?", new String[] { Long.toString(id) });
//                int delCnt = db.delete(FilmsContract.Films.TABLE_NAME, FilmsContract.Films.COLUMN_NAME + " = '" + moviesListView.getItemAtPosition(position).toString() + "' ", null);
                //String text = moviesListView.getItemAtPosition(position).toString();
//                String text = delCnt + " deleted " + moviesListView.getItemAtPosition(position).toString();
//                Toast.makeText(MainActivity.this, "" + text, Toast.LENGTH_SHORT).show();
//                // todo: check why doesnt't clear up immediately
//                listItem.clear();
//                viewMovies();
                Intent intent = new Intent(MainActivity.this, FilmInfo.class);
                startActivity(intent);
            }
        });

        // add button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // if clicked - new intent(window) opens
                // todo: add Age and Link field on intent
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        Intent thisIntent = getIntent();
        parseIntentWithFilm(thisIntent);

        if (fromEditor) {
            addMovie(name,
                    year, country, age, ganre, actor, producer,
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

        name = intent.getStringExtra("name");
        year = intent.getStringExtra("year");
        country = intent.getStringExtra("country");
        age = intent.getStringExtra("age");
        ganre = intent.getStringExtra("genre");
        actor = intent.getStringExtra("actor");
        producer = intent.getStringExtra("producer");
        imdb = intent.getStringExtra("imdb");
        kinopoisk = intent.getStringExtra("kinopoisk");
        want = intent.getStringExtra("want");
        description = intent.getStringExtra("description");
        link = intent.getStringExtra("link");
        fromEditor = intent.getBooleanExtra("fromEditor", fromEditor);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //displayDatabaseInfo();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void displayDatabaseInfo() {
        SQLiteDatabase db = vDbHelper.getReadableDatabase();

        String[] projection = {
                Films._ID,
                Films.COLUMN_NAME,
                Films.COLUMN_YEAR,
                Films.COLUMN_COUNTRY,
                Films.COLUMN_GANRE,
                Films.COLUMN_ACTOR,
                Films.COLUMN_PRODUCER,
                Films.COLUMN_IMDB,
                Films.COLUMN_KINOPOISK,
                Films.COLUMN_WANT,
                Films.COLUMN_DESCRIPTION};

        // query
        try (Cursor cursor = db.query(
                Films.TABLE_NAME,
                projection,
                null, null, null, null, null)) {
            TextView displayTextView = findViewById(R.id.text_view_info);
            displayTextView.setText("Количество фильмов в приложении: " + cursor.getCount() + " \n\n");
            displayTextView.append(Films._ID + " - " +
                    Films.COLUMN_NAME + " - " +
                    Films.COLUMN_YEAR + " - " +
                    Films.COLUMN_COUNTRY + " - " +
                    Films.COLUMN_GANRE + " - " +
                    Films.COLUMN_ACTOR + " - " +
                    Films.COLUMN_PRODUCER + " - " +
                    Films.COLUMN_IMDB + " - " +
                    Films.COLUMN_KINOPOISK + " - " +
                    Films.COLUMN_WANT + " - " +
                    Films.COLUMN_DESCRIPTION + "\n");

            int idColumnIndex = cursor.getColumnIndex(Films._ID);
            int nameColumnIndex = cursor.getColumnIndex(Films.COLUMN_NAME);
            int yearColumnIndex = cursor.getColumnIndex(Films.COLUMN_YEAR);
            int countryColumnIndex = cursor.getColumnIndex(Films.COLUMN_COUNTRY);
            int ganreColumnIndex = cursor.getColumnIndex(Films.COLUMN_GANRE);
            int actorColumnIndex = cursor.getColumnIndex(Films.COLUMN_ACTOR);
            int producerColumnIndex = cursor.getColumnIndex(Films.COLUMN_PRODUCER);
            int imdbColumnIndex = cursor.getColumnIndex(Films.COLUMN_IMDB);
            int kinopoiskColumnIndex = cursor.getColumnIndex(Films.COLUMN_KINOPOISK);
            int wantColumnIndex = cursor.getColumnIndex(Films.COLUMN_WANT);
            int descriptionColumnIndex = cursor.getColumnIndex(Films.COLUMN_DESCRIPTION);

            while (cursor.moveToNext()) {
                int currentId = (idColumnIndex >= 0) ? cursor.getInt(idColumnIndex) : -1;
                String currentName = (idColumnIndex >= 0) ? cursor.getString(nameColumnIndex) : "Null";
                int currentYear = (idColumnIndex >= 0) ? cursor.getInt(yearColumnIndex) : -1;
                String currentCountry = (idColumnIndex >= 0) ? cursor.getString(countryColumnIndex) : "Null";
                String currentGanre = (idColumnIndex >= 0) ? cursor.getString(ganreColumnIndex) : "Null";
                String currentActor = (idColumnIndex >= 0) ? cursor.getString(actorColumnIndex) : "Null";
                String currentProducer = (idColumnIndex >= 0) ? cursor.getString(producerColumnIndex) : "Null";
                double currentImdb = (idColumnIndex >= 0) ? cursor.getDouble(imdbColumnIndex) : -1;
                double currentKinopoisk = (idColumnIndex >= 0) ? cursor.getDouble(kinopoiskColumnIndex) : -1;
                int currentWant = (idColumnIndex >= 0) ? cursor.getInt(wantColumnIndex) : -1;
                String currentDescription = (idColumnIndex >= 0) ? cursor.getString(descriptionColumnIndex) : "Null";

                displayTextView.append(("\n" + currentId + " - " +
                        currentName + " - " +
                        currentYear + " - " +
                        currentCountry + " - " +
                        currentGanre + " - " +
                        currentActor + " - " +
                        currentProducer + " - " +
                        currentImdb + " - " +
                        currentKinopoisk + " - " +
                        currentWant + " - " +
                        currentDescription));
            }
        }
    }

    // search
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.item_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // add data
    private void addMovie(String vNameEditText,
                          String vYearSpinner, String vCountrySpinner, String vAgeEditText, String vGanreSpinner,
                          String vActorSpinner, String vProducerSpinner,
                          String vImdbEditText, String vKinopoiskEditText,
                          String vWantRadioGroup,
                          String vDescriptionEditText, String vLinkEditText) {
        //todo: check maybe Age and link are needed somewhere else
        SQLiteDatabase db = vDbHelper.getWritableDatabase();
        int currentId;

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
        long newFilmsRowId = db.insert(Films.TABLE_NAME, null, values);
        Log.d("addMovie", "Added film with Row ID" + newFilmsRowId);

        values.clear();
        String query = "SELECT * FROM " + ActorsContract.Actors.TABLE_NAME
                + " WHERE " + ActorsContract.Actors.COLUMN_NAME + " = '" + vActorSpinner.split(" ")[0] + "'"
                + " AND surname = '" + vActorSpinner.split(" ")[1] + "'";
        try (Cursor cursor = db.rawQuery(query, null)) {
            currentId = cursor.getPosition();
            values.put(ActorFilmContract.ActorFilm.COLUMN_ACTOR_ID, (int) currentId);
            values.put(ActorFilmContract.ActorFilm.COLUMN_FILM_ID, (int) newFilmsRowId);
            long newActorFilmRowId = db.insert(ActorFilmContract.ActorFilm.TABLE_NAME, null, values);
            Log.d("addMovie", "Added ActorFilm with Row ID" + newActorFilmRowId);
        }

        values.clear();
        query = "SELECT * FROM " + CountriesContract.Countries.TABLE_NAME
                + " WHERE " + CountriesContract.Countries.COLUMN_NAME + " = '" + vCountrySpinner + "'";
        try (Cursor cursor = db.rawQuery(query, null)) {
            currentId = cursor.getPosition();
            values.put(CountryFilmContract.CountryFilm.COLUMN_COUNTRY_ID, (int) currentId);
            values.put(CountryFilmContract.CountryFilm.COLUMN_FILM_ID, (int) newFilmsRowId);
            long newCountryFilmRowId = db.insert(ActorFilmContract.ActorFilm.TABLE_NAME, null, values);
            Log.d("addMovie", "Added CountryFilm with Row ID" + newCountryFilmRowId);
        }

        values.clear();
        query = "SELECT * FROM " + GanresContract.Ganres.TABLE_NAME
                + " WHERE " + GanresContract.Ganres.COLUMN_NAME + " = '" + vGanreSpinner + "'";
        try (Cursor cursor = db.rawQuery(query, null)) {
            currentId = cursor.getPosition();
            values.put(GanreFilmContract.GanreFilm.COLUMN_GANRE_ID, (int) currentId);
            values.put(GanreFilmContract.GanreFilm.COLUMN_FILM_ID, (int) newFilmsRowId);
            long newGanreFilmRowId = db.insert(GanreFilmContract.GanreFilm.TABLE_NAME, null, values);
            Log.d("addMovie", "Added GanreFilm with Row ID" + newGanreFilmRowId);
        }

        values.clear();
        query = "SELECT * FROM " + ProducersContract.Producers.TABLE_NAME
                + " WHERE " + ProducersContract.Producers.COLUMN_NAME + " = '" + vProducerSpinner.split(" ")[0] + "'"
                + " AND surname = '" + vProducerSpinner.split(" ")[1] + "'";
        try (Cursor cursor = db.rawQuery(query, null)) {
            currentId = cursor.getPosition();
            values.put(ProducerFilmContract.ProducerFilm.COLUMN_PRODUCER_ID, (int) currentId);
            values.put(ProducerFilmContract.ProducerFilm.COLUMN_FILM_ID, (int) newFilmsRowId);
            long newProducerFilmRowId = db.insert(ProducerFilmContract.ProducerFilm.TABLE_NAME, null, values);
            Log.d("addMovie", "Added ProducerFilm with Row ID" + newProducerFilmRowId);
        }

        if (Integer.parseInt(vWantRadioGroup) == 1) {
            values.clear();
            values.put(WantToWatchContract.WantToWatch.COLUMN_FILM_ID, (int)newFilmsRowId);
            values.put(WantToWatchContract.WantToWatch.COLUMN_ADD_DATE, Calendar.DATE);
            long newWantToWatchRowId = db.insert(WantToWatchContract.WantToWatch.TABLE_NAME, null, values);
            Log.d("addMovie", "Added WantToWatch with Row ID" + newWantToWatchRowId);

        } else if (Integer.parseInt(vWantRadioGroup) == 2){
            values.clear();
            values.put(WatchedContract.Watched.COLUMN_FILM_ID, (int)newFilmsRowId);
            values.put(WatchedContract.Watched.COLUMN_DATE, Calendar.DATE);
            long newWatchedRowId = db.insert(WatchedContract.Watched.TABLE_NAME, null, values);
            Log.d("addMovie", "Added Watched with Row ID" + newWatchedRowId);
        }

    }

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


}

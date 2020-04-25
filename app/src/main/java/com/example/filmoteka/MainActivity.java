package com.example.filmoteka;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.util.Log;

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
import java.util.Calendar;

import data.FilmsContract;
import data.FilmsContract.Films;
import data.FilmraryDbHelper;
import data.WantToWatchContract;
import data.WatchedContract;

import static data.ActorsContract.Actors;
import static data.ActorFilmContract.ActorFilm;
import static data.CountriesContract.Countries;
import static data.GanreFilmContract.GanreFilm;
import static data.CountryFilmContract.CountryFilm;
import static data.GanresContract.Ganres;
import static data.ProducersContract.Producers;
import static data.ProducerFilmContract.ProducerFilm;

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

        if (getIntent().getBooleanExtra("delete", false)) {
            deleteMovie(getIntent().getStringExtra(FilmsContract.Films._ID));
        }

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
    private void addMovie(String vNameEditText,
                          String vYearSpinner, String vCountrySpinner, String vAgeEditText, String vGanreSpinner,
                          String vActorSpinner, String vProducerSpinner,
                          String vImdbEditText, String vKinopoiskEditText,
                          String vWantRadioGroup,
                          String vDescriptionEditText, String vLinkEditText) {
        //todo: check maybe Age and link are needed somewhere else
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

        query = selectAllFromTableWhere(Producers.TABLE_NAME, Producers.COLUMN_NAME,
                                        vProducerSpinner.split(" ")[0])
                + " AND surname = '" + vProducerSpinner.split(" ")[1] + "'";
        cascadeAdd(db, query, ProducerFilm.COLUMN_PRODUCER_ID, ProducerFilm.COLUMN_FILM_ID, newFilmsRowId,
                   ProducerFilm.TABLE_NAME, "ProducerFilm");

        values.clear();
        if (Integer.parseInt(vWantRadioGroup) == 1) {
            values.put(WantToWatchContract.WantToWatch.COLUMN_FILM_ID, newFilmsRowId);
            values.put(WantToWatchContract.WantToWatch.COLUMN_ADD_DATE, Calendar.DATE);
            long newWantToWatchRowId = db.insert(WantToWatchContract.WantToWatch.TABLE_NAME, null, values);
            Log.d("addMovie", "Added WantToWatch with Row ID" + newWantToWatchRowId);

        } else if (Integer.parseInt(vWantRadioGroup) == 2) {
            values.put(WatchedContract.Watched.COLUMN_FILM_ID, newFilmsRowId);
            values.put(WatchedContract.Watched.COLUMN_DATE, Calendar.DATE);
            long newWatchedRowId = db.insert(WatchedContract.Watched.TABLE_NAME, null, values);
            Log.d("addMovie", "Added Watched with Row ID" + newWatchedRowId);
        }

    }

    private String selectAllFromTableWhere(String TABLE_NAME, String COLUMN_NAME, String equalTo) {
        return String.format("SELECT * FROM %s WHERE %s = '%s'", TABLE_NAME, COLUMN_NAME, equalTo);
    }

    private void cascadeAdd(SQLiteDatabase db, String query, String firstId, String secondId, int newFilmsRowId,
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

    public void deleteMovie(String id) {
        SQLiteDatabase db = vDbHelper.getWritableDatabase();
        int deleted = db.delete(Films.TABLE_NAME, Films._ID + " = " + id, null);
        listItem.clear();
        moviesListView.setAdapter(null);
        viewMovies();
        Log.d("deleteMovies", String.format("Deleted %d rows", deleted), null);
    }

    public void addMovie(View view) {
        Intent intent = new Intent(MainActivity.this, EditorActivity.class);
        startActivity(intent);
    }
}

package com.example.filmoteka;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;

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

import java.util.ArrayList;

import data.FilmsContract;
import data.FilmsContract.Films;
import data.FilmraryDbHelper;

public class MainActivity extends AppCompatActivity {

    public FilmraryDbHelper vDbHelper;

    String name;
    String year;
    String country;
    String ganre;
    String actor;
    String producer;
    String imdb;
    String kinopoisk;
    String want;
    String description;
    boolean fromEditor = false;

    ArrayList<String> listItem;
    ArrayAdapter adapter;
    ListView moviesListView;

    ArrayList<String> countries;
    ArrayList<String> ganres;
    ArrayList<String> actors;
    ArrayList<String> producers;
//    ArrayList<String> wants;
//    ArrayList<String> watcheds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        moviesListView = findViewById(R.id.movies_list_view);

        vDbHelper = new FilmraryDbHelper(this);

        listItem = new ArrayList<>();
        countries = new ArrayList<>();
        ganres = new ArrayList<>();
        actors = new ArrayList<>();
        producers = new ArrayList<>();

        viewMovies();

        moviesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SQLiteDatabase db = vDbHelper.getWritableDatabase();
                //String whereclause = "_id = '" + id + "' ";
                String whereclause = "_id = '" + Long.toString(id) + "' ";
                //int delCnt = db.delete(Films.TABLE_NAME, Films._ID + " = '" + Long.toString(id) + "' ", null);
                //int delCnt = db.delete(Films.TABLE_NAME, "_ID = " + Long.toString(id), null);
                //int delCnt = db.delete(Films.TABLE_NAME, "_ID" + " = ?", new String[] { Long.toString(id) });
                int delCnt = db.delete(FilmsContract.Films.TABLE_NAME, FilmsContract.Films.COLUMN_NAME + " = '" + moviesListView.getItemAtPosition(position).toString() + "' ", null);
                //String text = moviesListView.getItemAtPosition(position).toString();
                String text = Integer.toString(delCnt) + " deleted " + moviesListView.getItemAtPosition(position).toString();
                Toast.makeText(MainActivity.this, ""+text, Toast.LENGTH_SHORT).show();
                listItem.clear();
                viewMovies();
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
        Intent thisIntent = getIntent();
        name = thisIntent.getStringExtra("name");
        year = thisIntent.getStringExtra("year");
        country = thisIntent.getStringExtra("country");
        ganre = thisIntent.getStringExtra("ganre");
        actor = thisIntent.getStringExtra("actor");
        producer = thisIntent.getStringExtra("producer");
        imdb = thisIntent.getStringExtra("imdb");
        kinopoisk = thisIntent.getStringExtra("kinopoisk");
        want = thisIntent.getStringExtra("want");
        description = thisIntent.getStringExtra("description");
        fromEditor = thisIntent.getBooleanExtra("fromEditor", fromEditor);
        if (fromEditor) {
            addMovie(name,
                    year, country, ganre, actor, producer,
                    imdb, kinopoisk,
                    want,
                    description);
            listItem.clear();
            viewMovies();
        }
        fromEditor = false;
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
                Films.COLUMN_DESCRIPTION };

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
                ArrayList<String> movieslist = new ArrayList<>();

                for (String movie : listItem) {
                    if (movie.toLowerCase().contains(newText.toLowerCase())){
                        movieslist.add(movie);
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                        android.R.layout.simple_list_item_1,
                        movieslist);
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
                          String vYearSpinner, String vCountrySpinner, String vGanreSpinner,
                          String vActorSpinner, String vProducerSpinner,
                          String vImdbEditText, String vKinopoiskEditText,
                          String vWantRadioGroup,
                          String vDescriptionEditText) {
        SQLiteDatabase db = vDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Films.COLUMN_NAME, vNameEditText);
        values.put(Films.COLUMN_YEAR, Integer.parseInt(vYearSpinner));
        values.put(Films.COLUMN_COUNTRY, vCountrySpinner);
        values.put(Films.COLUMN_GANRE, vGanreSpinner);
        values.put(Films.COLUMN_ACTOR, vActorSpinner);
        values.put(Films.COLUMN_PRODUCER, vProducerSpinner);
        values.put(Films.COLUMN_IMDB, Double.parseDouble(vImdbEditText));
        values.put(Films.COLUMN_KINOPOISK, Double.parseDouble(vKinopoiskEditText));
        values.put(Films.COLUMN_WANT, Integer.parseInt(vWantRadioGroup));
        values.put(Films.COLUMN_DESCRIPTION, vDescriptionEditText);

        long newRowId = db.insert(Films.TABLE_NAME, null, values);
    }

    // show data in ListView
    public void viewMovies() {
        SQLiteDatabase db = vDbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + FilmsContract.Films.TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data in db", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                listItem.add(cursor.getString(1)); // 1 for name, 0 for index
            }
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItem);
            moviesListView.setAdapter(adapter);
        }
        //cursor.close();
    }

    // todo: variaty of indexes
    // find movie by some id
    public void searchMovies(String text) {
        SQLiteDatabase db = vDbHelper.getReadableDatabase();
        String query = "SELECT * FROM "+ FilmsContract.Films.TABLE_NAME
                + " WHERE "+ FilmsContract.Films.COLUMN_NAME
                + " LIKE  '%" + text + "%'";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No such data in db", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                listItem.add(cursor.getString(1)); // 1 for name, 0 for index
            }
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItem);
            moviesListView.setAdapter(adapter);
        }
    }


}

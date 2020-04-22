package com.example.filmoteka;

import android.annotation.TargetApi;
import android.app.LauncherActivity;
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
import data.FilmsDbHelper;

public class MainActivity extends AppCompatActivity {

    public FilmsDbHelper vDbHelper;

    String name;
    String year;
    String country;
    String description;
    boolean fromEditor = false;

    ArrayList<String> listItem;
    ArrayAdapter adapter;
    ListView moviesListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        moviesListView = findViewById(R.id.movies_list_view);

        vDbHelper = new FilmsDbHelper(this);

        listItem = new ArrayList<>();
        viewMovies();

        moviesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text = moviesListView.getItemAtPosition(position).toString();
                Toast.makeText(MainActivity.this, ""+text, Toast.LENGTH_SHORT).show();
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
        description = thisIntent.getStringExtra("description");
        fromEditor = thisIntent.getBooleanExtra("fromEditor", fromEditor);
        if (fromEditor) {
            addMovie(name, year, country, description);
            //listItem.clear();
            //viewMovies();
        }
        fromEditor = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void displayDatabaseInfo() {
        SQLiteDatabase db = vDbHelper.getReadableDatabase();

        String[] projection = {
                FilmsContract.AddMovie._ID,
                FilmsContract.AddMovie.COLUMN_NAME,
                FilmsContract.AddMovie.COLUMN_YEAR,
                FilmsContract.AddMovie.COLUMN_COUNTRY,
                FilmsContract.AddMovie.COLUMN_DESCRIPTION };

        // query
        try (Cursor cursor = db.query(
                FilmsContract.AddMovie.TABLE_NAME,
                projection,
                null, null, null, null, null)) {
            TextView displayTextView = findViewById(R.id.text_view_info);
            displayTextView.setText("Количество фильмов в приложении: " + cursor.getCount() + " \n\n");
            displayTextView.append(FilmsContract.AddMovie._ID + " - " +
                    FilmsContract.AddMovie.COLUMN_NAME + " - " +
                    FilmsContract.AddMovie.COLUMN_YEAR + " - " +
                    FilmsContract.AddMovie.COLUMN_COUNTRY + " - " +
                    FilmsContract.AddMovie.COLUMN_DESCRIPTION + "\n");

            int idColumnIndex = cursor.getColumnIndex(FilmsContract.AddMovie._ID);
            int nameColumnIndex = cursor.getColumnIndex(FilmsContract.AddMovie.COLUMN_NAME);
            int yearColumnIndex = cursor.getColumnIndex(FilmsContract.AddMovie.COLUMN_YEAR);
            int countryColumnIndex = cursor.getColumnIndex(FilmsContract.AddMovie.COLUMN_COUNTRY);
            int descriptionColumnIndex = cursor.getColumnIndex(FilmsContract.AddMovie.COLUMN_DESCRIPTION);

            while (cursor.moveToNext()) {
                int currentId = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                int currentYear = cursor.getInt(yearColumnIndex);
                String currentCountry = cursor.getString(countryColumnIndex);
                String currentDescription = cursor.getString(descriptionColumnIndex);

                displayTextView.append(("\n" + currentId + " - " +
                        currentName + " - " +
                        currentYear + " - " +
                        currentCountry + " - " +
                        currentDescription));
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem searhItem = menu.findItem(R.id.item_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searhItem);

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
    private void addMovie(String vNameEditText, String vYearSpinner, String vCountrySpinner, String vDescriptionEditText) {
        SQLiteDatabase db = vDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FilmsContract.AddMovie.COLUMN_NAME, vNameEditText);
        values.put(FilmsContract.AddMovie.COLUMN_YEAR, Integer.parseInt(vYearSpinner));
        values.put(FilmsContract.AddMovie.COLUMN_COUNTRY, vCountrySpinner);
        values.put(FilmsContract.AddMovie.COLUMN_DESCRIPTION, vDescriptionEditText);

        long newRowId = db.insert(FilmsContract.AddMovie.TABLE_NAME, null, values);
    }

    // show data in ListView
    public void viewMovies() {
        SQLiteDatabase db = vDbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + FilmsContract.AddMovie.TABLE_NAME;
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

    // find movie by some id
    public void searchMovies(String text) {
        SQLiteDatabase db = vDbHelper.getReadableDatabase();
        String query = "SELECT * FROM "+ FilmsContract.AddMovie.TABLE_NAME
                + " WHERE "+ FilmsContract.AddMovie.COLUMN_NAME
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

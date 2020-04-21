package com.example.filmoteka;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import data.FilmsContract;
import data.FilmsDbHelper;

public class MainActivity extends AppCompatActivity {

    public FilmsDbHelper vDbHelper;

    String name;
    String year;
    String country;
    String description;
    boolean fromEditor = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        vDbHelper = new FilmsDbHelper(this);

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
        if (fromEditor)
            addMovie(name, year, country, description);
        fromEditor = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

    private void addMovie(String vNameEditText, String vYearSpinner, String vCountrySpinner, String vDescriptionEditText) {
        SQLiteDatabase db = vDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FilmsContract.AddMovie.COLUMN_NAME, vNameEditText);
        values.put(FilmsContract.AddMovie.COLUMN_YEAR, Integer.parseInt(vYearSpinner));
        values.put(FilmsContract.AddMovie.COLUMN_COUNTRY, vCountrySpinner);
        values.put(FilmsContract.AddMovie.COLUMN_DESCRIPTION, vDescriptionEditText);

        long newRowId = db.insert(FilmsContract.AddMovie.TABLE_NAME, null, values);
    }

}

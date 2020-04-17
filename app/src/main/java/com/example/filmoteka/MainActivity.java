package com.example.filmoteka;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.w3c.dom.Text;

import data.FilmsContract;
import data.FilmsDbHelper;

public class MainActivity extends AppCompatActivity {

    private FilmsDbHelper vDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        vDbHelper = new FilmsDbHelper(this);
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
                FilmsContract.AddMovie.COLUMN_DESCRITION };

        // query
        Cursor cursor = db.query(
                FilmsContract.AddMovie.TABLE_NAME,
                projection,
                null, null, null, null, null);
        TextView displayTextView = (TextView)findViewById(R.id.text_view_info);

        try {
            displayTextView.setText("Количество фильмов в приложении: " + cursor.getCount() + " \n\n");
            displayTextView.append(FilmsContract.AddMovie._ID + " - " +
                    FilmsContract.AddMovie.COLUMN_NAME + " - " +
                    FilmsContract.AddMovie.COLUMN_YEAR + " - " +
                    FilmsContract.AddMovie.COLUMN_COUNTRY + " - " +
                    FilmsContract.AddMovie.COLUMN_DESCRITION + "\n");

            int idColumnIndex = cursor.getColumnIndex(FilmsContract.AddMovie._ID);
            int nameColumnIndex = cursor.getColumnIndex(FilmsContract.AddMovie.COLUMN_NAME);
            int yearColumnIndex = cursor.getColumnIndex(FilmsContract.AddMovie.COLUMN_YEAR);
            int countryColumnIndex = cursor.getColumnIndex(FilmsContract.AddMovie.COLUMN_COUNTRY);
            int descriptionColumnIndex = cursor.getColumnIndex(FilmsContract.AddMovie.COLUMN_DESCRITION);

            while(cursor.moveToNext()) {
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
        } finally {
            cursor.close();
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
}

package com.example.filmoteka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import data.CountriesContract;
import data.FilmraryDbHelper;

public class AddCountry extends AppCompatActivity{

    public FilmraryDbHelper vDbHelper;
    private EditText fAddCountryEditText;
    CustomStringList3 countries;

    protected void onCreate(Bundle savedInstanceState) {

        Intent fromIntent = getIntent();
        String fromActivityName = fromIntent.getStringExtra("name");

        vDbHelper = FilmraryDbHelper.getInstance(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_country);

        Button addMovieButton = findViewById(R.id.add_country_button);
        fAddCountryEditText = findViewById(R.id.add_country_edit_text);
        addMovieButton.setOnClickListener(view -> {
            String vAddCountryEditText = fAddCountryEditText.getText().toString();

            if (vAddCountryEditText.isEmpty()) {
                Toast.makeText(AddCountry.this, "Country field is empty!", Toast.LENGTH_LONG).show();
                return;
            }

            // а чё делать, если фильм не добавлен? пока давай оставаться на странице..
            boolean added = addCountry(vAddCountryEditText);
            if (added) {
                Intent intent;
                if (fromActivityName.equalsIgnoreCase("editor"))
                    intent = new Intent(AddCountry.this, EditorActivity.class);
                else //if (fromActivityName.equalsIgnoreCase("editorSerias"))
                    intent = new Intent(AddCountry.this, EditorActivitySerias.class);
                startActivity(intent);
            }
        });
    }

    public class CustomStringList3 extends ArrayList<String> {
        @Override
        public boolean contains(Object o) {
            String paramStr = (String)o;
            for (String s : this) {
                if (paramStr.equalsIgnoreCase(s)) return true;
            }
            return false;
        }
    }

    private boolean addCountry(String country) {
        SQLiteDatabase db = vDbHelper.getWritableDatabase();
        String text = "SELECT * FROM " + CountriesContract.Countries.TABLE_NAME;
        countries = new CustomStringList3();
        countries.add(" - ");
        try (Cursor cursor = db.rawQuery(text, null)) {
            if (cursor.getCount() != 0) {
                while (cursor.moveToNext()) {
                    countries.add(cursor.getString(1));
                }
            }
        }
        if (countries.contains(country)) {
            Toast.makeText(this, "This country already exists!", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            ContentValues values = new ContentValues();
            values.put(CountriesContract.Countries.COLUMN_NAME, country);
            long newRowId = db.insert(CountriesContract.Countries.TABLE_NAME, null, values);
            Log.d("Added country ", String.format("Added %s with Row ID%d", country, newRowId));
            return true;
        }
    }
}

package com.example.filmoteka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import data.CountriesContract;
import data.FilmraryDbHelper;
import data.FilmsContract;

import java.util.ArrayList;
import java.util.Calendar;

public class EditorActivity extends AppCompatActivity {

    private EditText fNameEditText;
    private EditText fAgeEditText;
    private Spinner fYearSpinner;
    private Spinner fCountrySpinner;
    private Spinner fGanreSpinner;
    private Spinner fActorSpinner;
    private Spinner fProducerSpinner;
    private EditText fImbdEditText;
    private EditText fKinopoiskEditText;
    private RadioButton fDontRadioButton;
    private RadioButton fWantRadioButton;
    private RadioButton fWatchedRadioButton;
    private EditText fLinkEditText;
    private EditText fDescriptionEditText;

    String vCountry;
    int vWant = 0;

    public FilmraryDbHelper vDbHelper;
    ArrayList<String> countries;

    /**
     * Год премьеры, минимальное значение 1895, максимальное - текущий год.
     */

    private void findViews() {
        fNameEditText = findViewById(R.id.name_edit_text);
        fYearSpinner = findViewById(R.id.year_spinner);
        fCountrySpinner = findViewById(R.id.country_spinner);
        fGanreSpinner = findViewById(R.id.ganre_spinner);
        fAgeEditText = findViewById(R.id.age_edit_text);
        fActorSpinner = findViewById(R.id.actor_spinner);
        fProducerSpinner = findViewById(R.id.producer_spinner);
        fImbdEditText = findViewById(R.id.imdb_edit_text);
        fKinopoiskEditText = findViewById(R.id.kinopoisk_edit_text);
        fDontRadioButton = findViewById(R.id.radio_do_not_add);
        fWantRadioButton = findViewById(R.id.radio_want_to_watch);
        fWatchedRadioButton = findViewById(R.id.radio_watched);
        fLinkEditText = findViewById(R.id.link_edit_text);
        fDescriptionEditText = findViewById(R.id.description_edit_text);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        vDbHelper = FilmraryDbHelper.getInstance(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        findViews();

        vWant = checkRadioButtons();

        setupYearSpinner();
        setupCountrySpinner();
        setupGanreSpinner();
        setupActorSpinner();
        setupProducerSpinner();

        Button addMovieButton = findViewById(R.id.add_button);
        addMovieButton.setOnClickListener(view -> {
            String vNameEditText = fNameEditText.getText().toString();
            String vYearSpinner = fYearSpinner.getSelectedItem().toString();
            String vCountrySpinner = fCountrySpinner.getSelectedItem().toString();
            String vGenreSpinner = fGanreSpinner.getSelectedItem().toString();
            String vAgeText = fAgeEditText.getText().toString();
            String vActorSpinner = fActorSpinner.getSelectedItem().toString();
            String vProducerSpinner = fProducerSpinner.getSelectedItem().toString();
            String vImbdEditText = fImbdEditText.getText().toString();
            String vKinopoiskEditText = fKinopoiskEditText.getText().toString();
            String vWantRadioGroup = Integer.toString(vWant);
            String vLinkText = fLinkEditText.getText().toString();
            String vDescriptionEditText = fDescriptionEditText.getText().toString();

            if (vNameEditText.isEmpty() || vImbdEditText.isEmpty() || vKinopoiskEditText.isEmpty()) {
                Toast.makeText(EditorActivity.this, "Some fields are empty!", Toast.LENGTH_LONG).show();
                return;
            }

            Intent intent = new Intent(EditorActivity.this, MainActivity.class);
            intent.putExtra(FilmsContract.Films.COLUMN_NAME, vNameEditText);
            intent.putExtra(FilmsContract.Films.COLUMN_YEAR, vYearSpinner);
            intent.putExtra(FilmsContract.Films.COLUMN_COUNTRY, vCountrySpinner);
            intent.putExtra(FilmsContract.Films.COLUMN_GANRE, vGenreSpinner);
            intent.putExtra(FilmsContract.Films.COLUMN_ACTOR, vActorSpinner);
            intent.putExtra(FilmsContract.Films.COLUMN_AGE, vAgeText);
            intent.putExtra(FilmsContract.Films.COLUMN_PRODUCER, vProducerSpinner);
            intent.putExtra(FilmsContract.Films.COLUMN_IMDB, vImbdEditText);
            intent.putExtra(FilmsContract.Films.COLUMN_KINOPOISK, vKinopoiskEditText);
            intent.putExtra(FilmsContract.Films.COLUMN_WANT, vWantRadioGroup);
            intent.putExtra(FilmsContract.Films.COLUMN_LINK, vLinkText);
            intent.putExtra(FilmsContract.Films.COLUMN_DESCRIPTION, vDescriptionEditText);
            intent.putExtra("fromEditor", true);
            startActivity(intent);
        });

        Button addCountryButton = findViewById(R.id.add_country_button);
        addCountryButton.setOnClickListener(view -> {
            Intent intent = new Intent(EditorActivity.this, AddCountry.class);
            intent.putExtra("name", "editor");
            startActivity(intent);
        });
    }

    public void onClick(View view) {
        String text = "This button will add producer in future";
        Toast.makeText(EditorActivity.this, "" + text, Toast.LENGTH_LONG).show();
    }

    private int checkRadioButtons() {
        if (fDontRadioButton.isChecked())
            return 0;
        else if (fWantRadioButton.isChecked())
            return 1;
        else //if (fWatchedRadioButton.isChecked())
            return 2;
    }

    private void setupYearSpinner() {
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        int size = thisYear - 1895 + 1;
        ArrayList<String> years = new ArrayList<>(size);
        for (int i = thisYear; i >= 1895; i--) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);
        fYearSpinner.setAdapter(adapter);
        fYearSpinner.setSelection(0);
    }


    private void setupCountrySpinner() {
        SQLiteDatabase db = vDbHelper.getReadableDatabase();
        String text = "SELECT * FROM " + CountriesContract.Countries.TABLE_NAME;
        countries = new ArrayList<>();
        countries.add(" - ");
        try (Cursor cursor = db.rawQuery(text, null)) {
            if (cursor.getCount() != 0) {
                while (cursor.moveToNext()) {
                    countries.add(cursor.getString(1));
                }
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, countries);
            fCountrySpinner.setAdapter(adapter);
        }
//        ArrayAdapter<CharSequence> countrySpinnerAdapter = ArrayAdapter.createFromResource(this,
//                R.array.array_country_options, android.R.layout.simple_dropdown_item_1line);
//
//        countrySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
//
//        fCountrySpinner.setAdapter(countrySpinnerAdapter);
//        fCountrySpinner.setSelection(1);
//
//        fCountrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String selection = (String) parent.getItemAtPosition(position);
//                if (!TextUtils.isEmpty(selection)) {
//                    if (selection.equals(getString(R.string.russia_country))) {
//                        vCountry = "Россия";
//                    } else if (selection.equals(getString(R.string.usa_country))) {
//                        vCountry = "США";
//                    } else if (selection.equals(getString(R.string.france_country))) {
//                        vCountry = "Франция";
//                    } else {
//                        vCountry = "Неизвестно";
//                    }
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                vCountry = "Неизвестно";
//            }
//        });
    }

    private void setupGanreSpinner() {
        String[] genres = {"Боевик", "Вестерн", "Гангстерский фильм", "Детектив", "Драма", "Исторический фильм",
                "Комедия", "Мелодрама", "Музыкальный фильм", "Нуар", "Политический фильм", "Приключенческий фильм",
                "Сказка", "Трагедия", "Трагикомедия",};
        fGanreSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genres));
        fGanreSpinner.setSelection(0);
    }

    private void setupActorSpinner() {
        String[] actors = {"Брэд Питт", "Алексей Панин", "Хайден Кристенсен", "Анджелина Джоли",
                "Джет Ли", "Александр Ревва"};
        fActorSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, actors));
        fActorSpinner.setSelection(0);
    }

    private void setupProducerSpinner() {
        String[] producer = {"Джеймс Кэмерон", "Джордж Лукас", "Тим Бёртон", "Акира Куросава", "Тимур Бекмамбетов",
                "Сарик Андреасян", "Люк Бессон"};
        fProducerSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, producer));
        fProducerSpinner.setSelection(0);
    }

    private String selectAllFromTableWhere(String TABLE_NAME, String COLUMN_NAME, String equalTo) {
        return String.format("SELECT * FROM %s WHERE %s = '%s'", TABLE_NAME, COLUMN_NAME, equalTo);
    }

}

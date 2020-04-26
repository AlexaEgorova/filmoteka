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

import java.util.ArrayList;
import java.util.Calendar;

import data.CountriesContract;
import data.FilmraryDbHelper;

public class EditorActivitySerias extends AppCompatActivity {

    private EditText fNameEditText;
    private Spinner fStartYearSpinner;
    private EditText fSeasonsNumEditText;
    private EditText fEpDurationEditText;
    private EditText fEpInSeasonNumEditText;
    private Spinner fStateSpinner;
    private Spinner fCountrySpinner;
    private EditText fAgeEditText;
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

    ArrayList<String> countries;

    String vCountry;
    int vWant = 0;
    int vState  = 0;
    FilmraryDbHelper vDbHelper;

    /**
     * Год премьеры, минимальное значение 1895, максимальное - текущий год.
     */

    private void findViews() {
        fNameEditText = findViewById(R.id.name_edit_text);
        fStartYearSpinner = findViewById(R.id.year_spinner);
        fSeasonsNumEditText = findViewById(R.id.seasons_num_edit_text);
        fEpDurationEditText = findViewById(R.id.ep_duration_edit_text);
        fEpInSeasonNumEditText = findViewById(R.id.ep_in_season_edit_text);
        fStateSpinner = findViewById(R.id.state_spinner);
        fCountrySpinner = findViewById(R.id.country_spinner);
        fAgeEditText = findViewById(R.id.age_edit_text);
        fGanreSpinner = findViewById(R.id.ganre_spinner);
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

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_serias);

        findViews();
        vDbHelper = FilmraryDbHelper.getInstance(this);

        vWant = checkRadioButtons();

        setupYearSpinner();
        setupCountrySpinner();
        setupStateSpinner();
        setupGanreSpinner();
        setupActorSpinner();
        setupProducerSpinner();

        Button addCountryButton = findViewById(R.id.add_country_button);
        addCountryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditorActivitySerias.this, AddCountry.class);
                startActivity(intent);
            }
        });

        Button addGanreButton = findViewById(R.id.add_ganre_button);
        addGanreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = "This button will add genre in future";
                Toast.makeText(EditorActivitySerias.this, "" + text, Toast.LENGTH_LONG).show();
            }
        });

        Button addActorButton = findViewById(R.id.add_actor_button);
        addActorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = "This button will add actor in future";
                Toast.makeText(EditorActivitySerias.this, "" + text, Toast.LENGTH_LONG).show();
            }
        });

        Button addProducerButton = findViewById(R.id.add_producer_button);
        addProducerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = "This button will add producer in future";
                Toast.makeText(EditorActivitySerias.this, "" + text, Toast.LENGTH_LONG).show();
            }
        });

        Button addMovieButton = findViewById(R.id.add_button);
        addMovieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String vNameEditText = fNameEditText.getText().toString();
                String vStartYearSpinner = fStartYearSpinner.getSelectedItem().toString();
                String vSeasonsNumEditText = fSeasonsNumEditText.getText().toString();
                String vEpDurationEditText = fEpDurationEditText.getText().toString();
                String vEpInSeasonNumEditText = fEpInSeasonNumEditText.getText().toString();
                String vStateSpinner = fStateSpinner.getSelectedItem().toString();
                String vCountrySpinner = fCountrySpinner.getSelectedItem().toString();
                String vAgeEditText = fAgeEditText.getText().toString();
                String vGenreSpinner = fGanreSpinner.getSelectedItem().toString();
                String vActorSpinner = fActorSpinner.getSelectedItem().toString();
                String vProducerSpinner = fProducerSpinner.getSelectedItem().toString();
                String vImbdEditText = fImbdEditText.getText().toString();
                String vKinopoiskEditText = fKinopoiskEditText.getText().toString();
                String vWantRadioGroup = Integer.toString(vWant);
                String vLinkEditText = fLinkEditText.getText().toString();
                String vDescriptionEditText = fDescriptionEditText.getText().toString();

                if (vNameEditText.isEmpty() || vImbdEditText.isEmpty() || vKinopoiskEditText.isEmpty()) {
                    Toast.makeText(EditorActivitySerias.this, "Some fields are empty!", Toast.LENGTH_LONG).show();
                    return;
                }

    //    public final static String[] COLUMNS = {_ID, COLUMN_NAME, COLUMN_START_YEAR,
//            COLUMN_SEASONS_NUM, COLUMN_EP_DURATION, COLUMN_EP_IN_SEASON_NUM, COLUMN_STATE,
//            COLUMN_COUNTRY, COLUMN_AGE, COLUMN_GANRE, COLUMN_ACTOR, COLUMN_PRODUCER,
//            COLUMN_IMDB, COLUMN_KINOPOISK, COLUMN_WANT, COLUMN_LINK, COLUMN_DESCRIPTION};

                Intent intent = new Intent(EditorActivitySerias.this, MainActivitySerias.class);
                intent.putExtra("name", vNameEditText);
                intent.putExtra("start_year", vStartYearSpinner);
                intent.putExtra("seasons_num", vSeasonsNumEditText);
                intent.putExtra("ep_duration", vEpDurationEditText);
                intent.putExtra("ep_in_season_num", vEpInSeasonNumEditText);
                intent.putExtra("state", vStateSpinner);
                intent.putExtra("country", vCountrySpinner);
                intent.putExtra("age",  vAgeEditText);
                intent.putExtra("genre", vGenreSpinner);
                intent.putExtra("actor", vActorSpinner);
                intent.putExtra("producer", vProducerSpinner);
                intent.putExtra("imdb", vImbdEditText);
                intent.putExtra("kinopoisk", vKinopoiskEditText);
                intent.putExtra("want", vWantRadioGroup);
                intent.putExtra("link", vLinkEditText);
                intent.putExtra("description", vDescriptionEditText);
                intent.putExtra("fromEditor", true);
                startActivity(intent);
            }
        });
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
        fStartYearSpinner.setAdapter(adapter);
        fStartYearSpinner.setSelection(1);
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
    }

    private void setupGanreSpinner() {
        String[] genres = {" - ", "Боевик", "Вестерн", "Гангстерский фильм", "Детектив", "Драма", "Исторический фильм",
                "Комедия", "Мелодрама", "Музыкальный фильм", "Нуар", "Политический фильм", "Приключенческий фильм",
                "Сказка", "Трагедия", "Трагикомедия",};
        fGanreSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genres));
        fGanreSpinner.setSelection(0);
    }

    private void setupActorSpinner() {
        String[] actors = {" - ", "Брэд Питт", "Алексей Панин", "Хайден Кристенсен", "Анджелина Джоли",
                "Джет Ли", "Александр Ревва"};
        fActorSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, actors));
        fActorSpinner.setSelection(0);
    }

    private void setupProducerSpinner() {
        String[] producer = {" - ", "Джеймс Кэмерон", "Джордж Лукас", "Тим Бёртон", "Акира Куросава", "Тимур Бекмамбетов",
                "Сарик Андреасян", "Люк Бессон"};
        fProducerSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, producer));
        fProducerSpinner.setSelection(0);
    }

    private void setupStateSpinner() {
        String[] state = {" - ", "В процессе", "Завершен", "Заморожен"};
        fStateSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, state));
        fStateSpinner.setSelection(0);
    }
}

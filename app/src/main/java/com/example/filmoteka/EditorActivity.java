package com.example.filmoteka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class EditorActivity extends AppCompatActivity {

    private EditText fNameEditText;
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
    private EditText fDescriptionEditText;

    String vCountry;
    int vWant = 0;

    /**
     * Год премьеры, минимальное значение 1895, максимальное - текущий год.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        fNameEditText = findViewById(R.id.name_edit_text);
        fYearSpinner = findViewById(R.id.year_spinner);
        fCountrySpinner = findViewById(R.id.country_spinner);
        fGanreSpinner = findViewById(R.id.ganre_spinner);
        fActorSpinner = findViewById(R.id.actor_spinner);
        fProducerSpinner = findViewById(R.id.producer_spinner);
        fImbdEditText = findViewById(R.id.imdb_edit_text);
        fKinopoiskEditText = findViewById(R.id.kinopoisk_edit_text);
        fDontRadioButton = findViewById(R.id.radio_do_not_add);
        fWantRadioButton = findViewById(R.id.radio_want_to_watch);
        fWatchedRadioButton = findViewById(R.id.radio_watched);
        fDescriptionEditText = findViewById(R.id.description_edit_text);

        vWant = checkRadioButtons();

        setupYearSpinner();
        setupCountrySpinner();
        setupGanreSpinner();
        setupActorSpinner();
        setupProducerSpinner();

        Button addCountryButton = findViewById(R.id.add_country_button);
        addCountryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = "This button will add country in future";
                Toast.makeText(EditorActivity.this, "" + text, Toast.LENGTH_LONG).show();
            }
        });

        Button addGanreButton = findViewById(R.id.add_ganre_button);
        addGanreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = "This button will add ganre in future";
                Toast.makeText(EditorActivity.this, "" + text, Toast.LENGTH_LONG).show();
            }
        });

        Button addActorButton = findViewById(R.id.add_actor_button);
        addActorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = "This button will add actor in future";
                Toast.makeText(EditorActivity.this, "" + text, Toast.LENGTH_LONG).show();
            }
        });

        Button addProducerButton = findViewById(R.id.add_producer_button);
        addProducerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = "This button will add producer in future";
                Toast.makeText(EditorActivity.this, "" + text, Toast.LENGTH_LONG).show();
            }
        });

        //todo: add validity check
        Button addMovieButton = findViewById(R.id.add_button);
        addMovieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String vNameEditText = fNameEditText.getText().toString();
                String vYearSpinner = fYearSpinner.getSelectedItem().toString();
                String vCountrySpinner = fCountrySpinner.getSelectedItem().toString();
                String vGanreSpinner = fGanreSpinner.getSelectedItem().toString();
                String vActorSpinner = fActorSpinner.getSelectedItem().toString();
                String vProducerSpinner = fProducerSpinner.getSelectedItem().toString();
                String vImbdEditText = fImbdEditText.getText().toString();
                String vKinopoiskEditText = fKinopoiskEditText.getText().toString();
                String vWantRadioGroup = Integer.toString(vWant);
                String vDescriptionEditText = fDescriptionEditText.getText().toString();

                Intent intent = new Intent(EditorActivity.this, MainActivity.class);
                intent.putExtra("name", vNameEditText);
                intent.putExtra("year", vYearSpinner);
                intent.putExtra("country", vCountrySpinner);
                intent.putExtra("ganre", vGanreSpinner);
                intent.putExtra("actor", vActorSpinner);
                intent.putExtra("producer", vProducerSpinner);
                intent.putExtra("imdb", vImbdEditText);
                intent.putExtra("kinopoisk", vKinopoiskEditText);
                intent.putExtra("want", vWantRadioGroup);
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
        fYearSpinner.setAdapter(adapter);
        fYearSpinner.setSelection(0);
    }

    private void setupCountrySpinner() {
        ArrayAdapter<CharSequence> countrySpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_country_options, android.R.layout.simple_dropdown_item_1line);

        countrySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        fCountrySpinner.setAdapter(countrySpinnerAdapter);
        fCountrySpinner.setSelection(1);

        fCountrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.russia_country))) {
                        vCountry = "Россия";
                    } else if (selection.equals(getString(R.string.usa_country))) {
                        vCountry = "США";
                    } else if (selection.equals(getString(R.string.france_country))) {
                        vCountry = "Франция";
                    } else {
                        vCountry = "Неизвестно";
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                vCountry = "Неизвестно";
            }
        });
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
}

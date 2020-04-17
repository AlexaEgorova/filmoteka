package com.example.filmoteka;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class EditorActivity extends AppCompatActivity {

    private EditText vNameEditText;
    private EditText vDescriptionEditText;

    private Spinner vCountrySpinner;
    private Spinner vYearSpinner;

    /**
     Год премьеры, минимальное значение 1895, максимальное - текущий год.
     */
    private String vCountry;
    private int mYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        vNameEditText = (EditText)findViewById(R.id.edit_film_name);
        vDescriptionEditText = (EditText)findViewById(R.id.edit_film_description);
        vCountrySpinner = (Spinner)findViewById(R.id.spinner_country);
        vYearSpinner = (Spinner)findViewById(R.id.spinner_year);

        setupCountrySpinner();
        setupYeatSpinner();
    }

    private void setupCountrySpinner() {
        ArrayAdapter countrySpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_country_options, android.R.layout.simple_dropdown_item_1line);

        countrySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        vCountrySpinner.setAdapter(countrySpinnerAdapter);
        vCountrySpinner.setSelection(1);

        vCountrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String)parent.getItemAtPosition(position);
                if(!TextUtils.isEmpty(selection)) {
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

    private  void setupYeatSpinner() {

    }
}

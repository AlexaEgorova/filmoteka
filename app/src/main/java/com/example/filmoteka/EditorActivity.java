package com.example.filmoteka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;

import data.FilmsContract;
import data.FilmsDbHelper;

public class EditorActivity extends AppCompatActivity {

    private EditText fNameEditText;
    private EditText fDescriptionEditText;

    private Spinner fCountrySpinner;
    private Spinner fYearSpinner;

    private FilmsDbHelper vDbHelper;

    /**
     Год премьеры, минимальное значение 1895, максимальное - текущий год.
     */
    private String vCountry;
    private int mYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intent = getIntent();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        vDbHelper = new FilmsDbHelper(this);

        fNameEditText = (EditText)findViewById(R.id.name_edit_text);
        fDescriptionEditText = (EditText)findViewById(R.id.description_edit_text);
        fCountrySpinner = (Spinner)findViewById(R.id.country_spinner);
        fYearSpinner = (Spinner)findViewById(R.id.year_spinner);

        setupCountrySpinner();
        setupYearSpinner();

        Button btn = findViewById(R.id.add_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String vNameEditText = fNameEditText.getText().toString();
                String vYearSpinner = fYearSpinner.getSelectedItem().toString();
                String vCountrySpinner = fCountrySpinner.getSelectedItem().toString();
                String vDescriptionEditText = fDescriptionEditText.getText().toString();

                Intent intent = new Intent(EditorActivity.this, MainActivity.class);
                intent.putExtra("name", vNameEditText);
                intent.putExtra("year", vYearSpinner);
                intent.putExtra("country", vCountrySpinner);
                intent.putExtra("description", vDescriptionEditText);
                intent.putExtra("fromEditor", true);
                startActivity(intent);
            }
        });
    }

    private void setupCountrySpinner() {
        ArrayAdapter countrySpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_country_options, android.R.layout.simple_dropdown_item_1line);

        countrySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        fCountrySpinner.setAdapter(countrySpinnerAdapter);
        fCountrySpinner.setSelection(1);

        fCountrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    private  void setupYearSpinner() {
        ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = thisYear; i >= 1895; i--) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);
        fYearSpinner.setAdapter(adapter);
        fYearSpinner.setSelection(0);
    }
}

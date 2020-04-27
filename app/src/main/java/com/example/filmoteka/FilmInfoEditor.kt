package com.example.filmoteka

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import data.CountriesContract.Countries
import data.FilmraryDbHelper
import data.FilmsContract.Films
import kotlinx.android.synthetic.main.activity_film_info_editor.*
import java.util.*


class FilmInfoEditor : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film_info_editor)

        initFields()
    }

    private fun initFields() {
        name_edit_text.setText(intent.getStringExtra(Films.COLUMN_NAME))

        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        year_spinner.setSelection(intent.getIntExtra(Films.COLUMN_YEAR, currentYear) - currentYear)

        setupGenreSpinner()
        setupActorSpinner()
        setupProducerSpinner()
        setupCountrySpinner()
        setupYearSpinner()

        imdb_edit_text.setText(intent.getStringExtra(Films.COLUMN_IMDB))
        kinopoisk_edit_text.setText(intent.getStringExtra(Films.COLUMN_KINOPOISK))
        age_edit_text.setText(intent.getStringExtra(Films.COLUMN_AGE))

        radio_do_not_add.isChecked = false
        radio_want_to_watch.isChecked = false
        radio_watched.isChecked = false
        when (intent.getStringExtra(Films.COLUMN_WANT)) {
            "0" -> radio_do_not_add.isChecked = true
            "1" -> radio_want_to_watch.isChecked = true
            "2" -> radio_watched.isChecked = true
        }

        link_edit_text.setText(intent.getStringExtra(Films.COLUMN_LINK))
        description_edit_text.setText(intent.getStringExtra(Films.COLUMN_DESCRIPTION))
    }

    private fun setupCountrySpinner() {
        val db: SQLiteDatabase = FilmraryDbHelper.getInstance(this).readableDatabase
        val text = "SELECT * FROM ${Countries.TABLE_NAME}"
        val countries = ArrayList<String>()
        countries.add(" - ")
        db.rawQuery(text, null).use { cursor ->
            if (cursor.count != 0) {
                while (cursor.moveToNext()) {
                    countries.add(cursor.getString(1))
                }
            }
            val adapter: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, countries)
            country_spinner.adapter = adapter
        }
    }

    private fun setupGenreSpinner() {
        val genres = arrayOf("Боевик", "Вестерн", "Гангстерский фильм", "Детектив", "Драма", "Исторический фильм",
                "Комедия", "Мелодрама", "Музыкальный фильм", "Нуар", "Политический фильм", "Приключенческий фильм",
                "Сказка", "Трагедия", "Трагикомедия")
        ganre_spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genres)
        ganre_spinner.setSelection(0)
    }

    private fun setupActorSpinner() {
        val db: SQLiteDatabase = FilmraryDbHelper.getInstance(this).readableDatabase
        val text = "SELECT * FROM " + Countries.TABLE_NAME
        val actors = ArrayList<String>()
        actors.add(" - ")
        db.rawQuery(text, null).use { cursor ->
            if (cursor.count != 0) {
                while (cursor.moveToNext()) {
                    actors.add(cursor.getString(1))
                }
            }
            val adapter: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, actors)
            actor_spinner.adapter = adapter
        }
    }

    private fun setupProducerSpinner() {
        val producer = arrayOf("Джеймс Кэмерон", "Джордж Лукас", "Тим Бёртон", "Акира Куросава", "Тимур Бекмамбетов",
                "Сарик Андреасян", "Люк Бессон")
        producer_spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, producer)
        producer_spinner.setSelection(0)
    }

    private fun setupYearSpinner() {
        val thisYear = Calendar.getInstance()[Calendar.YEAR]
        val size = thisYear - 1895 + 1
        val years = ArrayList<String>(size)
        for (i in thisYear downTo 1895) {
            years.add(i.toString())
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, years)
        year_spinner.adapter = adapter
        year_spinner.setSelection(0)
    }

    fun onClick(view: View) {
        val vNameEditText = name_edit_text.text.toString()
        val vYearSpinner = year_spinner.selectedItem.toString()
        val vCountrySpinner = country_spinner.selectedItem.toString()
        val vAgeText = age_edit_text.text.toString()
        val vGenreSpinner = ganre_spinner.selectedItem.toString()
        val vActorSpinner = actor_spinner.selectedItem.toString()
        val vProducerSpinner = producer_spinner.selectedItem.toString()
        val vImbdEditText = imdb_edit_text.text.toString()
        val vKinopoiskEditText = kinopoisk_edit_text.text.toString()
        val vWantRadioGroup = checkRadioButtons().toString()
        val vDescriptionEditText = description_edit_text.text.toString()
        val vLinkText = link_edit_text.text.toString()

        if (vNameEditText.isEmpty() || vImbdEditText.isEmpty() || vKinopoiskEditText.isEmpty()) {
            Toast.makeText(this, "Some fields are empty!", Toast.LENGTH_LONG).show()
            return
        }

        val filmId = intent.getStringExtra(Films._ID)
        val db = FilmraryDbHelper.getInstance(this).writableDatabase
        val deleted = db.delete(Films.TABLE_NAME, "${Films._ID} = $filmId", null)
        Log.d("filmEditDeleteMovies", "Deleted $deleted rows", null)

        CommonFunctions.addMovie(vNameEditText, vYearSpinner, vCountrySpinner, vAgeText, vGenreSpinner, vActorSpinner,
                vProducerSpinner, vImbdEditText, vKinopoiskEditText, vWantRadioGroup, vDescriptionEditText, vLinkText,
                FilmraryDbHelper.getInstance(this))
        intent.setClass(this, MainActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    private fun checkRadioButtons(): Int {
        return when {
            radio_do_not_add.isChecked -> 0
            radio_want_to_watch.isChecked -> 1
            else -> 2 //if (fWatchedRadioButton.isChecked())
        }
    }
}

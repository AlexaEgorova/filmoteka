package com.example.filmoteka

import android.app.Activity
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import data.ActorsContract
import data.CountriesContract.Countries
import data.FilmraryDbHelper
import data.FilmsContract.Films
import data.GanresContract
import data.ProducersContract
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

    fun addCountry(view: View?) {
        val intent = Intent(this, AddCountry::class.java)
        startActivityForResult(intent, CODE_RETURN.COUNTRY.value)
    }

    fun addActor(view: View?) {
        val intent = Intent(this, AddActor::class.java)
        startActivityForResult(intent, CODE_RETURN.ACTOR.value)
    }

    fun addProducer(view: View?) {
        val intent = Intent(this, AddProducer::class.java)
        startActivityForResult(intent, CODE_RETURN.PRODUCER.value)
    }

    fun addGenre(view: View?) {
        val intent = Intent(this, AddGenre::class.java)
        startActivityForResult(intent, CODE_RETURN.GENRE.value)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (CODE_RETURN.values()[requestCode - 1]) {
                CODE_RETURN.COUNTRY -> setupCountrySpinner()
                CODE_RETURN.GENRE -> setupGenreSpinner()
                CODE_RETURN.PRODUCER -> setupProducerSpinner()
                CODE_RETURN.ACTOR -> setupActorSpinner()
            }
        }
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
        val db: SQLiteDatabase = FilmraryDbHelper.getInstance(this).readableDatabase
        val text = "SELECT * FROM ${GanresContract.Ganres.TABLE_NAME}"
        val genres = ArrayList<String>()
        genres.add(" - ")
        db.rawQuery(text, null).use { cursor ->
            if (cursor.count != 0) {
                while (cursor.moveToNext()) {
                    genres.add(cursor.getString(1))
                }
            }
            val adapter: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                    genres)
            ganre_spinner.adapter = adapter
        }
    }

    private fun setupActorSpinner() {
        val db: SQLiteDatabase = FilmraryDbHelper.getInstance(this).readableDatabase
        val text = "SELECT * FROM ${ActorsContract.Actors.TABLE_NAME}"
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
        val db: SQLiteDatabase = FilmraryDbHelper.getInstance(this).readableDatabase
        val text = "SELECT * FROM ${ProducersContract.Producers.TABLE_NAME}"
        val producers = ArrayList<String>()
        producers.add(" - ")
        db.rawQuery(text, null).use { cursor ->
            if (cursor.count != 0) {
                while (cursor.moveToNext()) {
                    producers.add(cursor.getString(1) + " " + cursor.getString(2))
                }
            }
            val adapter: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, producers)
            producer_spinner.setAdapter(adapter)
        }
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
                vProducerSpinner, vImbdEditText, vKinopoiskEditText, vWantRadioGroup, vLinkText, vDescriptionEditText,
                FilmraryDbHelper.getInstance(this))
        intent.setClass(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun checkRadioButtons(): Int {
        return when {
            radio_do_not_add.isChecked -> 0
            radio_want_to_watch.isChecked -> 1
            else -> 2 //if (fWatchedRadioButton.isChecked())
        }
    }
}

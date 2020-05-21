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
import data.GenresContract
import data.ProducersContract
import data.SeriesContract.Series
import kotlinx.android.synthetic.main.activity_serias_info_editor.*
import java.util.*


class SeriasInfoEditor : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_serias_info_editor)

        initFields()
    }

    private fun initFields() {
        name_edit_text.setText(intent.getStringExtra(Series.COLUMN_NAME))

        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        year_spinner.setSelection(intent.getIntExtra(Series.COLUMN_START_YEAR, currentYear) - currentYear)

        seasons_num_edit_text.setText(intent.getStringExtra(Series.COLUMN_SEASONS_NUM))
        ep_in_season_edit_text.setText(intent.getStringExtra(Series.COLUMN_EP_IN_SEASON_NUM))
        ep_duration_edit_text.setText(intent.getStringExtra(Series.COLUMN_EP_DURATION))

        setupStateSpinner()
        setupGenreSpinner()
        setupActorSpinner()
        setupProducerSpinner()
        setupCountrySpinner()
        setupYearSpinner()

        imdb_edit_text.setText(intent.getStringExtra(Series.COLUMN_IMDB))
        kinopoisk_edit_text.setText(intent.getStringExtra(Series.COLUMN_KINOPOISK))
        age_edit_text.setText(intent.getStringExtra(Series.COLUMN_AGE))

        radio_do_not_add.isChecked = false
        radio_want_to_watch.isChecked = false
        radio_watched.isChecked = false
        when (intent.getStringExtra(Series.COLUMN_WANT)) {
            "0" -> radio_do_not_add.isChecked = true
            "1" -> radio_want_to_watch.isChecked = true
            "2" -> radio_watched.isChecked = true
        }

        link_edit_text.setText(intent.getStringExtra(Series.COLUMN_LINK))
        description_edit_text.setText(intent.getStringExtra(Series.COLUMN_DESCRIPTION))
    }

    fun addCountrySeriesInfo(view: View?) {
        val intent = Intent(this, AddCountry::class.java)
        startActivityForResult(intent, CODE_RETURN.COUNTRY.value)
    }

    fun addActorSeriesInfo(view: View?) {
        val intent = Intent(this, AddActor::class.java)
        startActivityForResult(intent, CODE_RETURN.ACTOR.value)
    }

    fun addProducerSeriesInfo(view: View?) {
        val intent = Intent(this, AddProducer::class.java)
        startActivityForResult(intent, CODE_RETURN.PRODUCER.value)
    }

    fun addGenreSeriesInfo(view: View?) {
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
        val text = "SELECT * FROM ${GenresContract.Genres.TABLE_NAME}"
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
                    actors.add(cursor.getString(1) + " " + cursor.getString(2))
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

    private fun setupStateSpinner() {
        val state = arrayOf(" - ", "В процессе", "Завершен", "Заморожен")
        state_spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, state)
        state_spinner.setSelection(0)
    }

    fun onClick(view: View) {
        val vNameEditText: String = name_edit_text.text.toString()
        val vStartYearSpinner: String = year_spinner.selectedItem.toString()
        val vSeasonsNumEditText: String = seasons_num_edit_text.text.toString()
        val vEpDurationEditText: String = ep_duration_edit_text.text.toString()
        val vEpInSeasonNumEditText: String = ep_in_season_edit_text.text.toString()
        val vStateSpinner: String = state_spinner.selectedItem.toString()
        val vCountrySpinner: String = country_spinner.selectedItem.toString()
        val vAgeEditText: String = age_edit_text.text.toString()
        val vGenreSpinner: String = ganre_spinner.selectedItem.toString()
        val vActorSpinner: String = actor_spinner.selectedItem.toString()
        val vProducerSpinner: String = producer_spinner.selectedItem.toString()
        val vImdbEditText: String = imdb_edit_text.text.toString()
        val vKinopoiskEditText: String = kinopoisk_edit_text.text.toString()
        val vWant = checkRadioButtons()
        val vWantRadioGroup = vWant.toString()
        val vLinkEditText: String = link_edit_text.text.toString()
        val vDescriptionEditText: String = description_edit_text.text.toString()

        if (vNameEditText.isEmpty() || vImdbEditText.isEmpty() || vKinopoiskEditText.isEmpty()
                || vAgeEditText.isEmpty() || vStartYearSpinner.isEmpty() || vSeasonsNumEditText.isEmpty()
                || vEpDurationEditText.isEmpty() || vEpInSeasonNumEditText.isEmpty()) {
            Toast.makeText(this, "Some fields are empty!", Toast.LENGTH_LONG).show()
            return
        }

        val seriesId = intent.getStringExtra(Series._ID)
        val db = FilmraryDbHelper.getInstance(this).writableDatabase
        val deleted = db.delete(Series.TABLE_NAME, "${Series._ID} = $seriesId", null)
        Log.d("seriesEditDeleteMovies", "Deleted $deleted rows", null)

        CommonFunctions.addSeries(vNameEditText, vStartYearSpinner, vSeasonsNumEditText, vEpDurationEditText,
                vEpInSeasonNumEditText, vStateSpinner, vCountrySpinner, vAgeEditText, vGenreSpinner, vActorSpinner,
                vProducerSpinner, vImdbEditText, vKinopoiskEditText, vWantRadioGroup, vLinkEditText,
                vDescriptionEditText, FilmraryDbHelper.getInstance(this))
        intent.setClass(this, MainActivitySerias::class.java)
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

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
import data.CountriesContract.Countries
import data.FilmraryDbHelper
import data.GanresContract
import data.ProducersContract
import data.SeriasContract.Serias
import kotlinx.android.synthetic.main.activity_info_serias.*
import kotlinx.android.synthetic.main.activity_serias_info_editor.*
import java.util.*


class SeriasInfoEditor : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_serias_info_editor)

        initFields()
    }

    private fun initFields() {
        name_edit_text.setText(intent.getStringExtra(Serias.COLUMN_NAME))

        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        year_spinner.setSelection(intent.getIntExtra(Serias.COLUMN_START_YEAR, currentYear) - currentYear)

        seasons_num_edit_text.setText(intent.getStringExtra(Serias.COLUMN_SEASONS_NUM))
        series_info_episodes_a_season.text = intent.getStringExtra(Serias.COLUMN_EP_IN_SEASON_NUM)
        series_info_episode_length.text = intent.getStringExtra(Serias.COLUMN_EP_DURATION)

        setupStateSpinner()
        setupGenreSpinner()
        setupActorSpinner()
        setupProducerSpinner()
        setupCountrySpinner()
        setupYearSpinner()

        imdb_edit_text.setText(intent.getStringExtra(Serias.COLUMN_IMDB))
        kinopoisk_edit_text.setText(intent.getStringExtra(Serias.COLUMN_KINOPOISK))
        age_edit_text.setText(intent.getStringExtra(Serias.COLUMN_AGE))

        radio_do_not_add.isChecked = false
        radio_want_to_watch.isChecked = false
        radio_watched.isChecked = false
        when (intent.getStringExtra(Serias.COLUMN_WANT)) {
            "0" -> radio_do_not_add.isChecked = true
            "1" -> radio_want_to_watch.isChecked = true
            "2" -> radio_watched.isChecked = true
        }

        link_edit_text.setText(intent.getStringExtra(Serias.COLUMN_LINK))
        description_edit_text.setText(intent.getStringExtra(Serias.COLUMN_DESCRIPTION))
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
        val text = "SELECT * FROM ${Countries.TABLE_NAME}"
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

        val intent = Intent(this, MainActivitySerias::class.java)
        intent.putExtra(Serias.COLUMN_NAME, vNameEditText)
        intent.putExtra(Serias.COLUMN_START_YEAR, vStartYearSpinner)
        intent.putExtra(Serias.COLUMN_SEASONS_NUM, vSeasonsNumEditText)
        intent.putExtra(Serias.COLUMN_EP_DURATION, vEpDurationEditText)
        intent.putExtra(Serias.COLUMN_EP_IN_SEASON_NUM, vEpInSeasonNumEditText)
        intent.putExtra(Serias.COLUMN_STATE, vStateSpinner)
        intent.putExtra(Serias.COLUMN_COUNTRY, vCountrySpinner)
        intent.putExtra(Serias.COLUMN_AGE, vAgeEditText)
        intent.putExtra(Serias.COLUMN_GANRE, vGenreSpinner)
        intent.putExtra(Serias.COLUMN_ACTOR, vActorSpinner)
        intent.putExtra(Serias.COLUMN_PRODUCER, vProducerSpinner)
        intent.putExtra(Serias.COLUMN_IMDB, vImdbEditText)
        intent.putExtra(Serias.COLUMN_KINOPOISK, vKinopoiskEditText)
        intent.putExtra(Serias.COLUMN_WANT, vWantRadioGroup)
        intent.putExtra(Serias.COLUMN_LINK, vLinkEditText)
        intent.putExtra(Serias.COLUMN_DESCRIPTION, vDescriptionEditText)
        intent.putExtra("fromEditor", true)

        val filmId = intent.getStringExtra(Serias._ID)
        val db = FilmraryDbHelper.getInstance(this).writableDatabase
        val deleted = db.delete(Serias.TABLE_NAME, "${Serias._ID} = $filmId", null)
        Log.d("filmEditDeleteMovies", "Deleted $deleted rows", null)

        CommonFunctions.addSerias(vNameEditText, vStartYearSpinner, vSeasonsNumEditText, vEpDurationEditText,
                vEpInSeasonNumEditText, vStateSpinner, vCountrySpinner, vAgeEditText, vGenreSpinner, vActorSpinner,
                vProducerSpinner, vImdbEditText, vKinopoiskEditText, vWantRadioGroup, vLinkEditText,
                vDescriptionEditText, FilmraryDbHelper.getInstance(this))
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

package com.example.filmoteka

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import data.FilmsContract
import kotlinx.android.synthetic.main.activity_film_info_editor.*
import java.util.*

class FilmInfoEditor : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film_info_editor)

        initFields()
    }

    private fun initFields() {
        name_edit_text.setText(intent.getStringExtra(FilmsContract.Films.COLUMN_NAME))

        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        year_spinner.setSelection(intent.getIntExtra(FilmsContract.Films.COLUMN_YEAR, currentYear) - currentYear)

        setupGenreSpinner()
        setupActorSpinner()
        setupProducerSpinner()
        setupYearSpinner()

        imdb_edit_text.setText(intent.getStringExtra(FilmsContract.Films.COLUMN_IMDB))
        kinopoisk_edit_text.setText(intent.getStringExtra(FilmsContract.Films.COLUMN_KINOPOISK))
        age_edit_text.setText(intent.getStringExtra(FilmsContract.Films.COLUMN_AGE))

        radio_do_not_add.isChecked = false
        radio_want_to_watch.isChecked = false
        radio_watched.isChecked = false
        when (intent.getStringExtra(FilmsContract.Films.COLUMN_WANT)) {
            "0" -> radio_do_not_add.isChecked = true
            "1" -> radio_want_to_watch.isChecked = true
            "2" -> radio_watched.isChecked = true
        }

        link_edit_text.setText(intent.getStringExtra(FilmsContract.Films.COLUMN_LINK))
        description_edit_text.setText(intent.getStringExtra(FilmsContract.Films.COLUMN_DESCRIPTION))
    }

    private fun setupGenreSpinner() {
        val genres = arrayOf("Боевик", "Вестерн", "Гангстерский фильм", "Детектив", "Драма", "Исторический фильм",
                "Комедия", "Мелодрама", "Музыкальный фильм", "Нуар", "Политический фильм", "Приключенческий фильм",
                "Сказка", "Трагедия", "Трагикомедия")
        ganre_spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genres)
        ganre_spinner.setSelection(0)
    }

    private fun setupActorSpinner() {
        val actors = arrayOf("Брэд Питт", "Алексей Панин", "Хайден Кристенсен", "Анджелина Джоли",
                "Джет Ли", "Александр Ревва")
        actor_spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, actors)
        actor_spinner.setSelection(0)
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
        val vGenreSpinner = ganre_spinner.selectedItem.toString()
        val vAgeText = age_edit_text.text.toString()
        val vActorSpinner = actor_spinner.selectedItem.toString()
        val vProducerSpinner = producer_spinner.selectedItem.toString()
        val vImbdEditText = imdb_edit_text.text.toString()
        val vKinopoiskEditText = kinopoisk_edit_text.text.toString()
        val vWantRadioGroup = checkRadioButtons().toString()
        val vLinkText = link_edit_text.text.toString()
        val vDescriptionEditText = description_edit_text.text.toString()

        if (vNameEditText.isEmpty() || vImbdEditText.isEmpty() || vKinopoiskEditText.isEmpty()) {
            Toast.makeText(this, "Some fields are empty!", Toast.LENGTH_LONG).show()
            return
        }

        val filmId = intent.getStringExtra(FilmsContract.Films._ID)
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("delete", true)
        intent.putExtra(FilmsContract.Films._ID, filmId)
        intent.putExtra(FilmsContract.Films.COLUMN_NAME, vNameEditText)
        intent.putExtra(FilmsContract.Films.COLUMN_YEAR, vYearSpinner)
        intent.putExtra(FilmsContract.Films.COLUMN_COUNTRY, vCountrySpinner)
        intent.putExtra(FilmsContract.Films.COLUMN_GANRE, vGenreSpinner)
        intent.putExtra(FilmsContract.Films.COLUMN_ACTOR, vActorSpinner)
        intent.putExtra(FilmsContract.Films.COLUMN_AGE, vAgeText)
        intent.putExtra(FilmsContract.Films.COLUMN_PRODUCER, vProducerSpinner)
        intent.putExtra(FilmsContract.Films.COLUMN_IMDB, vImbdEditText)
        intent.putExtra(FilmsContract.Films.COLUMN_KINOPOISK, vKinopoiskEditText)
        intent.putExtra(FilmsContract.Films.COLUMN_WANT, vWantRadioGroup)
        intent.putExtra(FilmsContract.Films.COLUMN_LINK, vLinkText)
        intent.putExtra(FilmsContract.Films.COLUMN_DESCRIPTION, vDescriptionEditText)
        intent.putExtra("fromEditor", true)
        startActivity(intent)
    }

    private fun checkRadioButtons(): Int {
        return when {
            radio_do_not_add.isChecked -> 0
            radio_want_to_watch.isChecked -> 1
            else -> 2 //if (fWatchedRadioButton.isChecked())
        }
    }
}

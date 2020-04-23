package com.example.filmoteka

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import data.FilmsContract
import kotlinx.android.synthetic.main.activity_film_info_editor.*
import kotlinx.android.synthetic.main.activity_film_info_editor.actor_spinner
import kotlinx.android.synthetic.main.activity_film_info_editor.age_edit_text
import kotlinx.android.synthetic.main.activity_film_info_editor.ganre_spinner
import kotlinx.android.synthetic.main.activity_film_info_editor.imdb_edit_text
import kotlinx.android.synthetic.main.activity_film_info_editor.kinopoisk_edit_text
import kotlinx.android.synthetic.main.activity_film_info_editor.name_edit_text
import kotlinx.android.synthetic.main.activity_film_info_editor.producer_spinner
import kotlinx.android.synthetic.main.activity_film_info_editor.radio_do_not_add
import kotlinx.android.synthetic.main.activity_film_info_editor.year_spinner
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
}

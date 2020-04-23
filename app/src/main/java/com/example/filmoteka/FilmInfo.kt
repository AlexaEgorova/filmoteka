package com.example.filmoteka

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import data.FilmsContract
import kotlinx.android.synthetic.main.activity_film_info.*

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class FilmInfo : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film_info)

        appendText(filmName, readField(FilmsContract.Films.COLUMN_NAME, intent))
        appendText(filmYear, readField(FilmsContract.Films.COLUMN_YEAR, intent))
        appendText(filmGenre, readField(FilmsContract.Films.COLUMN_GANRE, intent))
        appendText(filmCountry, readField(FilmsContract.Films.COLUMN_COUNTRY, intent))
        appendText(filmProducer, readField(FilmsContract.Films.COLUMN_PRODUCER, intent))
        appendText(filmIMDB, readField(FilmsContract.Films.COLUMN_IMDB, intent))
        appendText(filmKinopoisk, readField(FilmsContract.Films.COLUMN_KINOPOISK, intent))
        appendText(filmDescription, readField(FilmsContract.Films.COLUMN_DESCRIPTION, intent))
    }

    private fun appendText(field: TextView, text: String) {
        field.text = field.text.toString() + " " + text
    }

    private fun readField(field: String, intent: Intent) = intent.getStringExtra(field).toString()

    private fun getFirstPart(textField: TextView): String {
        val string = textField.text.toString()
        var spaceIdx = 0
        for (char in string) {
            if (char == ' ') {
                break
            }
            ++spaceIdx
        }
        string.removeRange(spaceIdx..string.lastIndex)
        return string
    }

    override fun onBackPressed() {
        filmName.text = getFirstPart(filmName)
        filmYear.text = getFirstPart(filmYear)
        filmGenre.text = getFirstPart(filmGenre)
        filmCountry.text = getFirstPart(filmCountry)
        filmProducer.text = getFirstPart(filmProducer)
        filmIMDB.text = getFirstPart(filmIMDB)
        filmKinopoisk.text = getFirstPart(filmKinopoisk)
        filmDescription.text = getFirstPart(filmDescription)
        super.onBackPressed()
    }
}

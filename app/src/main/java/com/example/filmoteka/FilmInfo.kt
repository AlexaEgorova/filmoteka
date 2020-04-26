package com.example.filmoteka

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import data.FilmraryDbHelper
import data.FilmsContract.Films
import kotlinx.android.synthetic.main.activity_film_info.*

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class FilmInfo : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film_info)

        appendText(filmName, readField(Films.COLUMN_NAME, intent))
        appendText(filmYear, readField(Films.COLUMN_YEAR, intent))
        appendText(filmGenre, readField(Films.COLUMN_GANRE, intent))
        appendText(filmCountry, readField(Films.COLUMN_COUNTRY, intent))
        appendText(filmProducer, readField(Films.COLUMN_PRODUCER, intent))
        appendText(filmIMDB, readField(Films.COLUMN_IMDB, intent))
        appendText(filmKinopoisk, readField(Films.COLUMN_KINOPOISK, intent))
        appendText(filmDescription, readField(Films.COLUMN_DESCRIPTION, intent))
    }

    //todo: показывать трейлер по переданной ссылке (либо прямо сюда вставить плеер, либо перебрасывать в интернет)

    private fun appendText(field: TextView, text: String): Unit = field.append(" $text")
    private fun readField(field: String, intent: Intent) = intent.getStringExtra(field).toString()
    private fun getFirstPart(textField: TextView) = textField.text.toString().substringBefore(" ")

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

    fun onClick(view: View) {
        val filmId = intent.getStringExtra(Films._ID)
        val db = FilmraryDbHelper.getInstance(this).writableDatabase
        val deleted = db.delete(Films.TABLE_NAME, "${Films._ID} = $filmId", null)
        Log.d("filmInfoDeleteMovies", "Deleted $deleted rows", null)
        intent.setClass(this, MainActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    fun onClickEdit(view: View) {
        intent.setClass(this, FilmInfoEditor::class.java)
        startActivity(intent)
    }
}

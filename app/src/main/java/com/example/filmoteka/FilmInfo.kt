package com.example.filmoteka

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import data.FilmraryDbHelper
import data.FilmsContract.Films
import kotlinx.android.synthetic.main.activity_film_info.*
import java.util.regex.Matcher
import java.util.regex.Pattern

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

        addPlayer(intent.getStringExtra(Films.COLUMN_LINK).toString())
    }


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

    fun addPlayer(url: String) {
        val pattern = "http(?:s?)://(?:www\\.)?youtu(?:be\\.com/watch\\?v=|\\.be/)([\\w\\-_]*)(&(amp;)?" +
                "\u200C\u200B[\\w?\u200C\u200B=]*)?"

        val compiledPattern: Pattern = Pattern.compile(pattern)
        val matcher: Matcher = compiledPattern.matcher(url)

        if (!matcher.find()) {
            return
        }
        third_party_player_view.visibility = View.VISIBLE
        third_party_player_view.getPlayerUiController().showFullscreenButton(true)
        third_party_player_view.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(@NonNull youTubePlayer: YouTubePlayer) {
                val videoId = matcher.group(1)
                youTubePlayer.cueVideo(videoId!!, 0f)
            }
        })

        third_party_player_view.getPlayerUiController().setFullScreenButtonClickListener(View.OnClickListener {
            if (third_party_player_view.isFullScreen()) {
                third_party_player_view.exitFullScreen()
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
                // Show ActionBar
                if (supportActionBar != null) {
                    supportActionBar!!.show()
                }
            } else {
                third_party_player_view.enterFullScreen()
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
                // Hide ActionBar
                if (supportActionBar != null) {
                    supportActionBar!!.hide()
                }
            }
        })
    }
}

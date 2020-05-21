package com.example.filmoteka

import android.app.Activity
import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import data.FilmraryDbHelper
import data.GenresContract
import kotlinx.android.synthetic.main.activity_add_genre.*

class AddGenre : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_genre)
    }

    fun addGenre(view: View) {
        val name = add_genre_edit_text.text.toString()

        if (name.isEmpty()) {
            Toast.makeText(this, "The field is empty!", Toast.LENGTH_LONG).show()
            return
        }

        val values = ContentValues().apply {
            put(GenresContract.Genres.COLUMN_NAME, name)
        }
        FilmraryDbHelper.getInstance(this).writableDatabase.insert(GenresContract.Genres.TABLE_NAME,
                null, values)
        setResult(Activity.RESULT_OK)
        finish()
    }
}

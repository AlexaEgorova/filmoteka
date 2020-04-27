package com.example.filmoteka

import android.app.Activity
import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast

import data.FilmraryDbHelper
import kotlinx.android.synthetic.main.activity_add_actor.*

import data.ProducersContract.Producers
import kotlinx.android.synthetic.main.activity_add_producer.*

class AddProducer : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_producer)
    }

    fun addProducer(view: View) {
        val name = producer_name_edit_text.text.toString()
        val surname = producer_surname_edit_text.text.toString()

        if (name.isEmpty() || surname.isEmpty()) {
            Toast.makeText(this, "Name and surname must be filled!", Toast.LENGTH_LONG).show()
            return
        }

        val values = ContentValues().apply {
            put(Producers.COLUMN_NAME, name)
            put(Producers.COLUMN_SURNAME, surname)
        }
        FilmraryDbHelper.getInstance(this).writableDatabase.insert(Producers.TABLE_NAME, null, values)
        setResult(Activity.RESULT_OK)
        finish()
    }
}

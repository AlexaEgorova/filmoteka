package com.example.filmoteka

import android.app.Activity
import android.app.DatePickerDialog
import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_actor.*
import java.util.*

import data.ActorsContract.Actors
import data.FilmraryDbHelper

class AddActor : AppCompatActivity() {
    private var mDateSetListener: DatePickerDialog.OnDateSetListener? = null
    private var year: Int = 0
    private var month: Int = 0
    private var day: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_actor)

        mDateSetListener = DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, day: Int ->
            this.year = year
            this.month = month
            this.day = day
        }
    }

    fun showDatePickerDialog(view: View) {
        val calendar = Calendar.getInstance()

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this,
                android.R.style.ThemeOverlay_Material_Dark, mDateSetListener, year, month, day)
        datePickerDialog.show()
    }

    fun addActor(view: View) {
        val name = actor_name_edit_text.text.toString()
        val surname = actor_surname_edit_text.text.toString()

        if (name.isEmpty() || surname.isEmpty()) {
            Toast.makeText(this, "Some fields are empty!", Toast.LENGTH_LONG).show()
            return
        }

        val values = ContentValues().apply {
            put(Actors.COLUMN_NAME, name)
            put(Actors.COLUMN_SURNAME, surname)
            put(Actors.COLUMN_BIRTHDAY, "$year-$month-$day")
        }
        FilmraryDbHelper.getInstance(this).writableDatabase.insert(Actors.TABLE_NAME, null, values)
        setResult(Activity.RESULT_OK)
        finish()
    }
}

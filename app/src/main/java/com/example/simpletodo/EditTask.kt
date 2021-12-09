package com.example.simpletodo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText

class EditTask : AppCompatActivity() {

    lateinit var inputText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_task)
        val task = getIntent().getStringExtra("task")
        val position = getIntent().getIntExtra("position", 0)

        inputText = findViewById<EditText>(R.id.editTask)
        inputText.setText(task)

        findViewById<Button>(R.id.button2).setOnClickListener {
            onSubmit(findViewById<Button>(R.id.button2), position)
        }

    }

    fun onSubmit(v: View, position: Int) {
        // Prepare data intent
        val data = Intent()
        // Pass relevant data back as a result
        data.putExtra("task", inputText.text.toString())
        data.putExtra("position", position) // ints work too
        // Activity finished ok, return the data
        setResult(RESULT_OK, data) // set result code and bundle data for response
        finish() // closes the activity, pass data to parent
    }
}
package com.example.simpletodo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object :  TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                listOfTasks.removeAt(position)

                adapter.notifyDataSetChanged()

                saveItems()
            }

            override fun onItemClicked(position: Int) {
                val task = listOfTasks[position]
                launchComposeView(task, position)
            }

        }


//        findViewById<Button>(R.id.button).setOnClickListener{
//            Log.i("Dee", "Clicked on buttton")
//        }

        loadItems()

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val inputTextField = findViewById<EditText>(R.id.addTaskField)

        findViewById<Button>(R.id.button).setOnClickListener {

            val userInputtedTask = inputTextField.text.toString()
            listOfTasks.add(userInputtedTask)
            adapter.notifyItemInserted(listOfTasks.size-1)

            inputTextField.setText("")

            saveItems()

        }
    }


    fun getDataFile(): File {

        return File(filesDir, "data.txt")
    }

    fun loadItems() {
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }

    }

    fun saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }

    }

    val REQUEST_CODE = 7

    fun launchComposeView(task: String, position: Int) {
        // first parameter is the context, second is the class of the activity to launch
        val i = Intent(this@MainActivity, EditTask::class.java)
        // put "extras" into the bundle for access in the second activity
        i.putExtra("task", task)
        i.putExtra("position", position)
        // brings up the second activity
        startActivityForResult(i, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            val task = data?.getExtras()?.getString("task")
            val position = data?.getExtras()?.getInt("position", 0)

            if (position != null) {
                if (task != null) {
                    listOfTasks.set(position, task)
                    adapter.notifyItemChanged(position)
                    saveItems()
                }
            }

        }

    }
}
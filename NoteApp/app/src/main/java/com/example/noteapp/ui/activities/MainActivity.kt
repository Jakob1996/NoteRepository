package com.example.noteapp.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.noteapp.R
import com.example.noteapp.databinding.ActivityMainBinding
import com.example.noteapp.viewmodels.NoteViewModel
import com.example.noteapp.viewmodels.TodoViewModel

class MainActivity : AppCompatActivity() {

    lateinit var noteViewModel: NoteViewModel

    lateinit var todoViewModel: TodoViewModel

    private val SHARED_PREFS = "sharedPrefs"

    private val KEY = "key"

    private var state: Boolean? = null

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setTheme(R.style.Theme_NoteApp)

        setContentView(binding.root)

        noteViewModel = ViewModelProvider(this)[NoteViewModel::class.java]

        todoViewModel = ViewModelProvider(this)[TodoViewModel::class.java]

        loadData()
    }

    override fun onStop() {
        saveData()
        super.onStop()
    }

    private fun loadData() {
        val sp = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        state = sp.getBoolean(KEY, false)
        if (state != null) {
            noteViewModel.p = state!!
        }
    }

    private fun saveData() {
        if (noteViewModel.getSortDescNote().value != null) {
            val sp = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
            val editor = sp.edit()
            editor.putBoolean(KEY, noteViewModel.getSortDescNote().value!!).apply()
        }
    }
}
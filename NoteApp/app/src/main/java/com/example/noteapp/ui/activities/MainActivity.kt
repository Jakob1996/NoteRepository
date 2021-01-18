package com.example.noteapp.ui.activities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.noteapp.R
import com.example.noteapp.viewmodels.ViewModel

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_NoteApp)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[ViewModel::class.java]
    }
}


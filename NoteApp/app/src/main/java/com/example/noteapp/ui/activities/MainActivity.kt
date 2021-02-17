package com.example.noteapp.ui.activities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.noteapp.R
import com.example.noteapp.databinding.ActivityMainBinding
import com.example.noteapp.viewmodels.ViewModel

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: ViewModel
    private val SHARED_PREFS = "sharedPrefs"
    private val KEY = "key"
    private var state:Boolean?= null
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setTheme(R.style.Theme_NoteApp)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[ViewModel::class.java]

        loadData()
    }

    override fun onStop(){
        saveData()
        super.onStop()
    }

    fun loadData(){
        val sp = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        state = sp.getBoolean(KEY, false)
        if(state!=null){
            viewModel.p = state!!
        }
    }

    fun saveData(){
        if(viewModel.getSortDescNote().value!= null) {
            val sp = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
            val editor = sp.edit()
            editor.putBoolean(KEY, viewModel.getSortDescNote().value!!).apply()
        }
    }
}


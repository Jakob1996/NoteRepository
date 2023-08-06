package com.example.noteapp.ui.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.noteapp.R
import com.example.noteapp.databinding.ActivityMainBinding
import com.example.noteapp.ui.interfaces.ToolbarAction
import com.example.noteapp.viewmodels.NoteViewModel
import com.example.noteapp.viewmodels.TodoViewModel
import fadeIn
import fadeOut

class MainActivity : AppCompatActivity(), ToolbarAction {

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
            noteViewModel.state = state!!
        }
    }

    private fun saveData() {
        if (noteViewModel.getSortDescNote().value != null) {
            val sp = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
            val editor = sp.edit()
            editor.putBoolean(KEY, noteViewModel.getSortDescNote().value!!).apply()
        }
    }

    override fun setupToolbar(
        backToTitle: String,
        titleVisible: Boolean,
        backBtnVisible: Boolean,
        favouriteBtnVisible: Boolean,
        multiBtnVisible: Boolean,
        editBtnVisible: Boolean,
        doneBtnVisible: Boolean
    ) {
        binding.fragmentSearchCategoryToolbarTitleTv.text = backToTitle

        if (backBtnVisible) {
            binding.fragmentGeneralNoteToolbarBackIv.visibility = View.VISIBLE
        } else {
            binding.fragmentGeneralNoteToolbarBackIv.visibility = View.INVISIBLE
        }

        if (titleVisible) {
            binding.fragmentSearchCategoryToolbarTitleTv.visibility = View.VISIBLE
        } else {
            binding.fragmentSearchCategoryToolbarTitleTv.visibility = View.GONE
        }

        if (favouriteBtnVisible) {
            binding.fragmentMainToolbarFavouriteCb.visibility = View.VISIBLE
        } else {
            binding.fragmentMainToolbarFavouriteCb.visibility = View.GONE
        }

        if (multiBtnVisible) {
            binding.fragmentMainToolbarMultibuttonIb.visibility = View.VISIBLE
        } else {
            binding.fragmentMainToolbarMultibuttonIb.visibility = View.GONE
        }

        if (editBtnVisible) {
            binding.fragmentGeneralNoteToolbarEditIv.visibility = View.VISIBLE
        } else {
            binding.fragmentGeneralNoteToolbarEditIv.visibility = View.GONE
        }

        if (doneBtnVisible) {
            binding.fragmentEditNoteToolbarSaveIv.visibility = View.VISIBLE
        } else {
            binding.fragmentEditNoteToolbarSaveIv.visibility = View.GONE
        }
    }

    override fun onBackBtnPressed(action: () -> Unit) {
        binding.fragmentGeneralNoteToolbarBackIv.setOnClickListener {
            action()
        }
    }

    override fun onFavouriteBtnPressed(action: () -> Unit) {
        binding.fragmentMainToolbarFavouriteCb.setOnClickListener {
            action()
        }
    }

    override fun onSearchBtnPressed(action: () -> Unit) {
        binding.fragmentMainToolbarMultibuttonIb.setOnClickListener {
            action()
        }
    }

    override fun onDoneBtnPressed(action: () -> Unit) {
        binding.fragmentEditNoteToolbarSaveIv.setOnClickListener {
            action()
        }
    }

    override fun onEditBtnPressed(action: () -> Unit) {
        binding.fragmentGeneralNoteToolbarEditIv.setOnClickListener {
            action()
        }
    }

    override fun setToolbarTitle(title: String) {
        binding.fragmentSearchCategoryToolbarTitleTv.text = title
    }

    override fun changeToolbarMultiSelectIcon(icon: Int) {

    }

    override fun disableMultiSelectMode() {
        binding.fragmentMainToolbarMultibuttonIb.setImageResource(R.drawable.ic_search)
        binding.fragmentMainToolbarMultibuttonIb.fadeIn()
        binding.fragmentSearchCategoryToolbarTitleTv.text = getString(R.string.explore)
    }

    override fun enableMultiSelectMode() {
        binding.fragmentSearchCategoryToolbarTitleTv.text = getString(R.string.delete)
        binding.fragmentMainToolbarMultibuttonIb.setImageResource(R.drawable.ic_round_delete_outline)
        binding.fragmentMainToolbarFavouriteCb.fadeOut()
    }
}
package com.example.noteapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.noteapp.R
import com.example.noteapp.viewmodels.NotesViewModel
import kotlinx.android.synthetic.main.fragment_add_edit_note.*
import kotlinx.android.synthetic.main.note_item.*

class AddEditNoteFragment:Fragment() {

    lateinit var viewModel: NotesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(requireActivity())[NotesViewModel::class.java]
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_edit_note, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getSelectedNote().observe(viewLifecycleOwner, Observer {
            note -> note.let {
            title_addEditFrag.setText(it?.title)
            mess_addEditFrag.setText(it?.message)
        }
        })
    }
}
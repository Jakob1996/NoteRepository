package com.example.noteapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.noteapp.R
import com.example.noteapp.adapters.NoteAdapter
import com.example.noteapp.adapters.OnItemClickListener
import com.example.noteapp.data.Note
import com.example.noteapp.viewmodels.NotesViewModel
import kotlinx.android.synthetic.main.fragment_main_framgent.*
import java.util.*
import kotlin.collections.ArrayList


class MainFramgent : Fragment(), OnItemClickListener {

    private lateinit var viewModel: NotesViewModel
    private lateinit var noteAdapter: NoteAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.allNotes.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            updateNotes(it)
        })
    }

    private fun updateNotes(list:List<Note>) {
        noteAdapter = NoteAdapter(list, this)
        recyclerView.adapter = noteAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(requireActivity())[NotesViewModel::class.java]
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 1)

        addNote_FB.setOnClickListener {
            findNavController().navigate(R.id.addEditNoteFragment)
        }
    }

    override fun onItemClick(note: Note, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onItemLongClick(note: Note, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_framgent, container, false)
    }

}
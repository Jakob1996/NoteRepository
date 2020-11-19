package com.example.noteapp.ui.fragments

import android.graphics.drawable.Icon
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.noteapp.R
import com.example.noteapp.adapters.NoteAdapter
import com.example.noteapp.adapters.OnItemClickListener
import com.example.noteapp.data.Note
import com.example.noteapp.viewmodels.NotesViewModel
import kotlinx.android.synthetic.main.fragment_main_framgent.*


class MainFramgent : Fragment(), OnItemClickListener {

    private lateinit var viewModel: NotesViewModel
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[NotesViewModel::class.java]
        requireActivity().onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if(viewModel.multiSelectMode ){
                    exitMultiSelectMode()
                } else{
                    isEnabled = false
                    requireActivity().onBackPressed()
                }
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_framgent, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.allNotes.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            updateNotes(it)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 1)

        addNote_FB.setOnClickListener {
            if(viewModel.multiSelectMode){
                viewModel.delete(viewModel.selectedNotes.toList())
                exitMultiSelectMode()
            }else {
                findNavController().navigate(R.id.addEditNoteFragment)
            }
        }
    }

    private fun updateNotes(list:List<Note>) {
        noteAdapter = NoteAdapter(list, this)
        recyclerView.adapter = noteAdapter
    }

    override fun onItemClick(note: Note, position: Int) {
        if(viewModel.multiSelectMode){
            if(viewModel.selectedNotes.contains(note)){
                unselectNote(note, position)
            } else{
                selectNote(note, position)
            }
        } else {
            viewModel.setSelectedNote(note)
            findNavController().navigate(R.id.addEditNoteFragment)
        }
    }

    override fun onItemLongClick(note: Note, position: Int) {
        if(!viewModel.multiSelectMode){
            viewModel.multiSelectMode = !viewModel.multiSelectMode
            selectNote(note, position)
            updateButtonUI()
        }
    }

    private fun selectNote(note: Note, position: Int) {
        note.isSelected = true
        viewModel.selectedNotes.add(note)
        noteAdapter.notifyItemChanged(position)
    }

    private fun unselectNote(note: Note, position: Int) {
        note.isSelected = false
        viewModel.selectedNotes.remove(note)
        noteAdapter.notifyItemChanged(position)

        if(viewModel.selectedNotes.isEmpty())
            exitMultiSelectMode()
    }

    private fun exitMultiSelectMode() {
        viewModel.multiSelectMode = false
        viewModel.selectedNotes.forEach{it.isSelected = false}
        viewModel.selectedNotes.clear()
        updateButtonUI()
        noteAdapter.notifyDataSetChanged() // Zmuszamy nasz layout do stworzenia jeszcze raz widoków i odświeży nam
                                            // wszystkie zaznaczone elementy
    }

    private fun updateButtonUI() {
        if(viewModel.multiSelectMode){
            addNote_FB.setImageIcon(Icon.createWithResource(requireContext(), R.drawable.ic_baseline_delete))
            addNote_FB.labelText = "Delete notes"
        }else{
            addNote_FB.setImageIcon(Icon.createWithResource(requireContext(), R.drawable.ic_note_add))
            addNote_FB.labelText = "Add note"

        }
    }
}

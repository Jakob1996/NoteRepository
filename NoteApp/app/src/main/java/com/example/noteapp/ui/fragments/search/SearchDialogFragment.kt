package com.example.noteapp.ui.fragments.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.noteapp.databinding.SearchNoteDialogBinding
import com.example.noteapp.viewmodels.NoteViewModel

class SearchDialogFragment : DialogFragment() {

    private lateinit var noteViewModel: NoteViewModel

    private var _binding: SearchNoteDialogBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteViewModel = ViewModelProvider(requireActivity())[NoteViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SearchNoteDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.searchNoteDialogSeachBtn.setOnClickListener {
            if (binding.searchNoteDialogTitleEt.text.toString().trim().isEmpty()) {
                dismiss()
            } else {
                noteViewModel.setSearchMode(true)
                noteViewModel.searchInNote = binding.searchNoteDialogTitleEt.text.toString()
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
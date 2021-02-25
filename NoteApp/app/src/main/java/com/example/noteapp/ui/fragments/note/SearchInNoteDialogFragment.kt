package com.example.noteapp.ui.fragments.note

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.noteapp.databinding.FragmentSearchInNoteDialog2Binding
import com.example.noteapp.viewmodels.NoteViewModel
import com.xeoh.android.texthighlighter.TextHighlighter

class SearchInNoteDialogFragment : DialogFragment() {
    private lateinit var noteViewModel:NoteViewModel

    private var _binding:FragmentSearchInNoteDialog2Binding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteViewModel = ViewModelProvider(requireActivity())[NoteViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchInNoteDialog2Binding.inflate(inflater, container ,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.searchNoteButton.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                if(binding.searchNoteEditText.text.toString().trim().isEmpty()){
                    dismiss()
                } else {
                    //TODO!!!
                        /*
                    noteViewModel.setSearchMode(true)
                    noteViewModel.searchInNote = binding.searchNoteEditText.text.toString()
                    val textHighLighter = TextHighlighter()
                    textHighLighter
                        .setBackgroundColor(Color.parseColor("#FFFF00"))
                        .addTarget(mess_addEditFrag)
                        .highlight(noteViewModel.searchInNote, TextHighlighter.BASE_MATCHER)

                         */
                    dismiss()
                }
            }
        })
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
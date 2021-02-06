package com.example.noteapp.ui.fragments.note

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.noteapp.R
import com.example.noteapp.viewmodels.ViewModel
import com.xeoh.android.texthighlighter.TextHighlighter
import kotlinx.android.synthetic.main.fragment_before_edit_note.*
import kotlinx.android.synthetic.main.fragment_search_in_note_dialog2.*
import kotlinx.android.synthetic.main.note_edit_layout_miscellaneous.*
import kotlinx.android.synthetic.main.note_item.*


class SearchInNoteDialogFragment : DialogFragment() {
    private lateinit var viewModel:ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[ViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_in_note_dialog2, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        searchNote_Button.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                if(searchNote_editText.text.toString().trim().isEmpty()){
                    dismiss()
                } else {
                    viewModel.setSearchMode(true)
                    viewModel.searchInNote = searchNote_editText.text.toString()
                    val textHighLighter = TextHighlighter()
                    textHighLighter
                        .setBackgroundColor(Color.parseColor("#FFFF00"))
                        .addTarget(mess_addEditFrag)
                        .highlight(viewModel.searchInNote, TextHighlighter.BASE_MATCHER)
                    dismiss()
                }
            }
        })
    }
}
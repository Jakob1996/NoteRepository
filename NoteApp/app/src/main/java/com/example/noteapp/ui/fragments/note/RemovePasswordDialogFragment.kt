package com.example.noteapp.ui.fragments.note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.noteapp.R
import com.example.noteapp.data.Note
import com.example.noteapp.viewmodels.ViewModel
import kotlinx.android.synthetic.main.fragment_password_note.*
import kotlinx.android.synthetic.main.fragment_remove_password_dialog.*


class RemovePasswordDialogFragment : DialogFragment() {
    private lateinit var viewModel: ViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[ViewModel::class.java]


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_remove_password_dialog, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        noRemovePasswordButton.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
             dismiss()
            }
        })

        yesRemovePasswordButton.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                val note = viewModel.getSelectedNote().value

                val title = note!!.title
                val message = note!!.message
                val date = note.date
                val color = note.color
                val fontColor = note.fontColor
                val fontSize = note.fontSize
                val favourite = note.isFavourite
                val imagePath = note.imagePath
                val isSelected = note.isSelected
                var rowIdd = note.rowId
                val hasPassword = false
                val password = 0
                val not = Note(title, message, date, isSelected, color, imagePath, fontColor, fontSize, favourite, hasPassword, password).apply {
                    rowId = rowIdd
                }
                viewModel.setSelectedNote(not)
                dismiss()

            }
        })
    }
}
package com.example.noteapp.ui.fragments.note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.noteapp.R
import com.example.noteapp.data.Category
import com.example.noteapp.data.Note
import com.example.noteapp.viewmodels.ViewModel
import kotlinx.android.synthetic.main.fragment_password_note.*

class AddPasswordNoteFragment : Fragment() {
    private lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[ViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_password_note, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        add_password_button.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                if(viewModel.getSelectedNote().value!= null){
                    val note = viewModel.getSelectedNote().value

                    val title = note!!.title
                    val message = note.message
                    val date = note.date
                    val color = note.color
                    val fontColor = note.fontColor
                    val fontSize = note.fontSize
                    val favourite = note.isFavourite
                    val imagePath = note.imagePaths
                    val isSelected = note.isSelected
                    val rowIdd = note.rowId
                    val hasPassword = true
                    val password = password_editText.text.toString().toInt()
                    val not = Note(title, message, date, isSelected, color, imagePath, fontColor, fontSize, favourite, hasPassword, password).apply {
                        rowId = rowIdd
                    }
                    viewModel.setSelectedNote(not)
                    requireActivity().onBackPressed()
                } else {
                    val category = viewModel.getSelectedCategotyItem().value

                    val name = category!!.categoryName
                    val color = category.color
                    val date = category.date
                    val isSelected = category.isSelected
                    val isFavourite = category.isFavoutire
                    val hasPassword = true
                    val password = password_editText.text.toString().toInt()
                    val id = category.rowIdCategory

                    val cat = Category(name, color, isSelected, date, isFavourite, hasPassword, password).apply {
                        rowIdCategory = id
                    }

                    viewModel.setSelectedCategotyItem(cat)
                    requireActivity().onBackPressed()
                }
            }
        })
    }
}
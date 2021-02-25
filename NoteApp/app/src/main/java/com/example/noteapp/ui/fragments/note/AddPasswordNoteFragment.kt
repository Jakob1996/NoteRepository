package com.example.noteapp.ui.fragments.note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.noteapp.data.Category
import com.example.noteapp.data.Note
import com.example.noteapp.databinding.FragmentPasswordNoteBinding
import com.example.noteapp.viewmodels.NoteViewModel
import com.example.noteapp.viewmodels.ToDoViewModel

class AddPasswordNoteFragment : Fragment() {
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var todoViewModel: ToDoViewModel

    private var _binding: FragmentPasswordNoteBinding? = null
    private val binding get() = _binding!!

    override fun onDestroyView() {

        _binding = null
        super.onDestroyView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteViewModel = ViewModelProvider(requireActivity())[NoteViewModel::class.java]
        todoViewModel = ViewModelProvider(requireActivity())[ToDoViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentPasswordNoteBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addPasswordButton.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                if(noteViewModel.getSelectedNote().value!= null){
                    val note = noteViewModel.getSelectedNote().value

                    val title = note!!.title
                    val message = note.message
                    val date = note.date
                    val color = note.color
                    val fontColor = note.fontColor
                    val fontSize = note.fontSize
                    val favourite = note.isFavourite
                    val isSelected = note.isSelected
                    val rowIdd = note.rowId
                    val hasPassword = true
                    val password = binding.passwordEditText.text.toString().toInt()
                    val not = Note(title, message, date, isSelected, color, fontColor, fontSize, favourite, hasPassword, password).apply {
                        rowId = rowIdd
                    }

                    noteViewModel.setSelectedNote(not)
                    requireActivity().onBackPressed()
                } else {
                    val category = todoViewModel.getSelectedCategotyItem().value

                    val name = category!!.categoryName
                    val color = category.color
                    val date = category.date
                    val isSelected = category.isSelected
                    val isFavourite = category.isFavoutire
                    val hasPassword = true
                    val password = binding.passwordEditText.text.toString().toInt()
                    val id = category.rowIdCategory

                    val cat = Category(name, color, isSelected, date, isFavourite, hasPassword, password).apply {
                        rowIdCategory = id
                    }

                    todoViewModel.setSelectedCategotyItem(cat)
                    requireActivity().onBackPressed()
                }
            }
        })
    }
}
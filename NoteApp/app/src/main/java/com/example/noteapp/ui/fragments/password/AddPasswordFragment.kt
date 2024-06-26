package com.example.noteapp.ui.fragments.password

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import com.example.noteapp.data.Category
import com.example.noteapp.data.Note
import com.example.noteapp.data.PathTypeConverter
import com.example.noteapp.databinding.FragmentAddPasswordBinding
import com.example.noteapp.ui.activities.MainActivity
import com.example.noteapp.ui.fragments.baseFragment.BaseFragment
import com.example.noteapp.viewmodels.NoteViewModel
import com.example.noteapp.viewmodels.TodoViewModel

class AddPasswordFragment : BaseFragment() {

    private lateinit var noteViewModel: NoteViewModel

    private lateinit var todoViewModel: TodoViewModel

    private var _binding: FragmentAddPasswordBinding? = null
    private val binding get() = _binding!!

    override fun onDestroyView() {

        _binding = null
        super.onDestroyView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteViewModel = ViewModelProvider(requireActivity())[NoteViewModel::class.java]
        todoViewModel = ViewModelProvider(requireActivity())[TodoViewModel::class.java]

        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    isEnabled = false
                    requireActivity().onBackPressed()
                    closeKeyboard()
                }
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPasswordBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        val mainActivity = this.activity as MainActivity

        mainActivity.hideToolbar()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragmentAddPasswordAddBtn.setOnClickListener {
            if (noteViewModel.getSelectedNote().value != null) {
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
                val password = binding.fragmentAddPasswordPasswordEt.text.toString().toInt()
                val not = Note(
                    title,
                    message,
                    date,
                    isSelected,
                    color,
                    fontColor,
                    fontSize,
                    favourite,
                    hasPassword,
                    password,
                    PathTypeConverter.toJsonPathList(listOf())
                ).apply {
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
                val password = binding.fragmentAddPasswordPasswordEt.text.toString().toInt()
                val id = category.rowIdCategory

                val cat = Category(
                    name,
                    color,
                    isSelected,
                    date,
                    isFavourite,
                    hasPassword,
                    password
                ).apply {
                    rowIdCategory = id
                }

                todoViewModel.setSelectedCategotyItem(cat)
                requireActivity().onBackPressed()
                closeKeyboard()
            }
        }

        getRequestFocus()
    }

    private fun getRequestFocus(){
        binding.fragmentAddPasswordPasswordEt.requestFocus()
        showSoftKeyboard()
    }
}
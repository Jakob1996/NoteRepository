package com.example.noteapp.ui.fragments.note

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.noteapp.databinding.FragmentEditNoteBinding
import com.example.noteapp.viewmodels.NoteViewModel


class EditNoteFragment : Fragment() {

    private lateinit var noteViewModel: NoteViewModel

    private var _binding: FragmentEditNoteBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteViewModel = ViewModelProvider(requireActivity())[NoteViewModel::class.java]
    }

    private fun setupView() {
        setOnBackPressedBtnClick()
        setOnBackPressedDispatcher()
        setSaveBtnListener()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        setOnBackPressedDispatcher()

        _binding = FragmentEditNoteBinding.inflate(inflater, container, false)

        return binding.root
    }

    private fun setOnBackPressedBtnClick() {
        binding.fragmentEditNoteToolbarBackIv.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView()
        noteViewModel.getSelectedNote().observe(viewLifecycleOwner, {
            binding.fragmentEditNoteTitleEt.setText(it!!.title)
            setFontColor(it.fontColor)
            setFontSize(it.fontSize)
        })

        binding.fragmentEditNoteDescriptionEt.setText(noteViewModel.getSelectedNote().value!!.message)

        //!!!
        if (binding.fragmentEditNoteDescriptionEt.text.isNotEmpty()) {
            binding.fragmentEditNoteDescriptionEt.requestFocus(binding.fragmentEditNoteDescriptionEt.text.length)
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()

        showSoftKeyboard()
    }

    private fun showSoftKeyboard() {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?

        imm?.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
    }

    private fun setFontColor(colorPath: Int?) {
        binding.fragmentEditNoteDescriptionEt.run {
            when (colorPath) {
                1 -> {
                    setTextColor(Color.parseColor("#FFFFFF"))
                }

                2 -> {
                    setTextColor(Color.parseColor("#959595"))
                }

                3 -> {
                    setTextColor(Color.parseColor("#666666"))
                }
            }
        }
    }

    private fun setFontSize(colorSize: Int?) {

        binding.fragmentEditNoteDescriptionEt.run {
            when (colorSize) {
                1 -> {
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
                }

                2 -> {
                    setTextSize(
                        TypedValue.COMPLEX_UNIT_SP,
                        20F
                    )
                }

                3 -> {
                    setTextSize(
                        TypedValue.COMPLEX_UNIT_SP,
                        25F
                    )
                }

                4 -> {
                    setTextSize(
                        TypedValue.COMPLEX_UNIT_SP,
                        30F
                    )
                }

                5 -> {
                    setTextSize(
                        TypedValue.COMPLEX_UNIT_SP,
                        35F
                    )
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null

        super.onDestroyView()
    }

    private fun setOnBackPressedDispatcher() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    isEnabled = false
                    saveNoteState()
                    requireActivity().onBackPressed()
                }
            })
    }

    private fun setSaveBtnListener() {
        binding.fragmentEditNoteToolbarSaveIv.setOnClickListener {
            saveNoteState()
            findNavController().popBackStack()
        }
    }

    private fun saveNoteState() {
        if (binding.fragmentEditNoteTitleEt.text.isNotEmpty() || binding.fragmentEditNoteDescriptionEt.text.isNotEmpty()) {
            val note = noteViewModel.getSelectedNote().value
            val title = binding.fragmentEditNoteTitleEt.text.toString()
            val mess = binding.fragmentEditNoteDescriptionEt.text.toString()
            note!!.message = mess
            note.title = title

            noteViewModel.updateNote(note)
        }
    }
}
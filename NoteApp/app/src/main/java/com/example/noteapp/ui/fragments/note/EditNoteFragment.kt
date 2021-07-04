package com.example.noteapp.ui.fragments.note

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.noteapp.databinding.FragmentEditNoteBinding
import com.example.noteapp.viewmodels.NoteViewModel


class EditNoteFragment : Fragment() {
    private lateinit var noteViewModel: NoteViewModel

    private var _binding:FragmentEditNoteBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        noteViewModel = ViewModelProvider(requireActivity())[NoteViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        _binding = FragmentEditNoteBinding.inflate(inflater, container, false)

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(false) {
                override fun handleOnBackPressed() {
                    if (binding.titleEditNote.text.isNotEmpty() || binding.messEditNote.text.isNotEmpty()) {
                        val note = noteViewModel.getSelectedNote().value
                        val title = binding.titleEditNote.text.toString()
                        val mess = binding.messEditNote.text.toString()
                        note!!.message = mess
                        note.title = title

                        noteViewModel.updateNote(note)
                    }
                    isEnabled = false
                    exitTransaction()
                }
            })

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        noteViewModel.getSelectedNote().observe(viewLifecycleOwner, {
            binding.titleEditNote.setText(it!!.title)
            setFontColor(it.fontColor)
            setFontSize(it.fontSize)
        })

        binding.messEditNote.setText(noteViewModel.getSelectedNote().value!!.message)


        //!!!
        if(binding.messEditNote.text.isNotEmpty()) {
            binding.messEditNote.requestFocus(binding.messEditNote.text.length)
        }

    }

    override fun onStart() {
        super.onStart()

        showSoftKeyboard()
    }

    private fun showSoftKeyboard(){
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?

        imm?.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
    }

    private fun setFontColor(colorPath: Int?) {
        when (colorPath) {
            1 -> {
                binding.messEditNote.setTextColor(Color.parseColor("#FFFFFF"))
            }

            2 -> {
                binding.messEditNote.setTextColor(Color.parseColor("#959595"))
            }

            3 -> {
                binding.messEditNote.setTextColor(Color.parseColor("#666666"))
            }
        }
    }

    private fun setFontSize(colorSize: Int?) {
        when (colorSize) {
            1 -> {
                binding.messEditNote.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
            }

            2 -> {
                binding.messEditNote.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
            }

            3 -> {
                binding.messEditNote.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25F)
            }

            4 -> {
                binding.messEditNote.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30F)
            }

            5 -> {
                binding.messEditNote.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35F)
            }
        }
    }

    override fun onDestroyView() {
        _binding = null

        super.onDestroyView()
    }

    private fun exitTransaction(){
        val sm = requireActivity().supportFragmentManager
        sm.popBackStackImmediate("BEN", FragmentManager.POP_BACK_STACK_INCLUSIVE)
        sm.beginTransaction().addToBackStack("abc").commit()
    }
}
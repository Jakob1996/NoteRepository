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
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.noteapp.R
import com.example.noteapp.data.Note
import com.example.noteapp.viewmodels.ViewModel
import kotlinx.android.synthetic.main.fragment_before_edit_note.*
import kotlinx.android.synthetic.main.fragment_edit_note.*
import java.util.*


class EditNoteFragment : Fragment() {
    private lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[ViewModel::class.java]

        requireActivity().onBackPressedDispatcher.addCallback(
                this,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {

                        //Po naciśnięciu przycisku wstecz sprawdzamy czy notatka nie jest pusta
                        if (title_editNote.text.isNotEmpty() || mess_EditNote.text.isNotEmpty()) {
                            val note = viewModel.getSelectedNote().value
                            val title = title_editNote.text.toString()
                            val mess = mess_EditNote.text.toString()
                            note!!.message = mess
                            note.title = title

                            viewModel.setSelectedNote(note)
                        }
                        isEnabled = false
                       findNavController().navigate(R.id.action_editNoteFragment_to_addEditNoteFragment)
                    }
                })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_edit_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mess_EditNote.requestFocus();
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.getSelectedNote().observe(viewLifecycleOwner, Observer {
            title_editNote.setText(it!!.title)
            mess_EditNote.setText(it.message)
            setFontColor(it.fontColor)
            setFontSize(it.fontSize)
        })
    }

    override fun onStart() {
        super.onStart()

        showSoftKeyboard()
    }

    private fun showSoftKeyboard(){
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?

        imm?.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
    }

    fun setFontColor(colorPath: Int?) {
        when (colorPath) {
            1 -> {
                mess_EditNote.setTextColor(Color.parseColor("#FFFFFF"))
            }

            2 -> {
                mess_EditNote.setTextColor(Color.parseColor("#959595"))
            }

            3 -> {
                mess_EditNote.setTextColor(Color.parseColor("#666666"))
            }
        }
    }

    fun setFontSize(colorSize: Int?) {
        when (colorSize) {
            1 -> {
                mess_EditNote.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
            }

            2 -> {
                mess_EditNote.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
            }

            3 -> {
                mess_EditNote.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25F)
            }

            4 -> {
                mess_EditNote.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30F)
            }

            5 -> {
                mess_EditNote.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35F)
            }
        }
    }
}
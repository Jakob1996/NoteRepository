package com.example.noteapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.noteapp.R
import com.example.noteapp.data.Note
import com.example.noteapp.viewmodels.NotesViewModel
import kotlinx.android.synthetic.main.fragment_add_edit_note.*
import java.util.*

class AddEditNoteFragment:Fragment() {

    private lateinit var viewModel: NotesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[NotesViewModel::class.java]
        requireActivity().onBackPressedDispatcher.addCallback(this, object:OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                //Po naciśnięciu przycisku wstecz sprawdzamy czy notatka nie jest pusta
                if(title_addEditFrag.text.isNotEmpty() || mess_addEditFrag.text.isNotEmpty()){
                    val title = title_addEditFrag.text.toString()
                    val message = mess_addEditFrag.text.toString()
                    val date = Calendar.getInstance().timeInMillis

                    //Jeśli notatka nie jest pusta, oraz nie jest zaznaczona w MainFragment - Tworzymy
                    if(viewModel.getSelectedNote().value == null){
                        val note = Note(title, message, date)
                        viewModel.insert(note)

                        //Jeśli notatka nie jest pusta, ale jest zaznaczona w MainFragment - Aktualizujemy
                    } else{
                        val selectedNote = viewModel.getSelectedNote().value!!
                        if(selectedNote.title!= title || selectedNote.message!= message){
                            val note = Note(title, message, date).apply {
                                rowId = viewModel.getSelectedNote().value!!.rowId
                            }
                            viewModel.update(note)
                        }
                    }
                }
                isEnabled = false
                requireActivity().onBackPressed()
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_edit_note, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (requireActivity() as AppCompatActivity).supportActionBar?.title ="Take note"

        viewModel.getSelectedNote().observe(viewLifecycleOwner, Observer {
            note -> note.let {
            title_addEditFrag.setText(it?.title)
            mess_addEditFrag.setText(it?.message)
            (requireActivity() as AppCompatActivity).supportActionBar?.title ="Edit note"
        }
        })
    }
}
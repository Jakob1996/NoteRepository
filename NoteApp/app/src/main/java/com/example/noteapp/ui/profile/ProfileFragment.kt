package com.example.noteapp.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.noteapp.R
import com.example.noteapp.data.Note
import com.example.noteapp.viewmodels.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment() {
    private lateinit var viewModel:ViewModel
    private val fbAuth = FirebaseAuth.getInstance()
    private var notesFromRoom: List<Note>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[ViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.allNotes.observe(viewLifecycleOwner, Observer {
            notesFromRoom = it
        })

        saveButt.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                viewModel.addNotesToCloud()
            }
        })

        getFromFirebaseButton.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                viewModel.notesFromFirebase.observe(viewLifecycleOwner, Observer {
                    it.forEach {
                        insertOrUpdate(it)
                    }
                })
            }
        })

        removeDataFirebase.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                viewModel.removeData()
            }
        })
    }

    fun insertOrUpdate(note: Note){
        viewModel.insertNote(note)
    }
}
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
import com.example.noteapp.viewmodels.ProfilViewModel
import com.example.noteapp.viewmodels.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment() {
    private lateinit var viewModel:ViewModel
    private lateinit var profilViewModel: ProfilViewModel
    private val fbAuth = FirebaseAuth.getInstance()
    private var notesFromRoom: List<Note>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[ViewModel::class.java]
        profilViewModel = ViewModelProvider(requireActivity())[ProfilViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.allNotes.observe(viewLifecycleOwner, Observer {
            notesFromRoom = it
        })



        saveButt.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {

                profilViewModel.addNotesToCloud(viewModel.allNotes.value!!)
            }
        })

        removeDataFirebase.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                viewModel.allNotes.observe(viewLifecycleOwner, Observer {
                    profilViewModel.deleteDataFromFirebase(it)
                })
            }
        })

        getFromFirebaseButton.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                profilViewModel.notesFromFirebase.observe(viewLifecycleOwner, Observer {
                    it.forEach {
                        insertOrUpdate(it)
                    }
                })
            }
        })
    }

    fun insertOrUpdate(note: Note){
        viewModel.insertNote(note)
    }
}
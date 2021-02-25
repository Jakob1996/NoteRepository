package com.example.noteapp.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.noteapp.data.Note
import com.example.noteapp.databinding.FragmentProfileBinding
import com.example.noteapp.viewmodels.ProfilViewModel
import com.example.noteapp.viewmodels.NoteViewModel
import com.google.firebase.auth.FirebaseAuth


class ProfileFragment : Fragment() {
    private lateinit var noteViewModel:NoteViewModel
    private lateinit var profilViewModel: ProfilViewModel
    private val fbAuth = FirebaseAuth.getInstance()
    private var notesFromRoom: List<Note>? = null

    private var _binding:FragmentProfileBinding? = null
    private val binding = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteViewModel = ViewModelProvider(requireActivity())[NoteViewModel::class.java]
        profilViewModel = ViewModelProvider(requireActivity())[ProfilViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noteViewModel.allNotes.observe(viewLifecycleOwner, Observer {
            notesFromRoom = it
        })

        binding.saveButt.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {

                profilViewModel.addNotesToCloud(noteViewModel.allNotes.value!!)
            }
        })

        binding.removeDataFirebase.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                noteViewModel.allNotes.observe(viewLifecycleOwner, Observer {
                    profilViewModel.deleteDataFromFirebase(it)
                })
            }
        })

        binding.getFromFirebaseButton.setOnClickListener(object :View.OnClickListener{
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
        noteViewModel.insertNote(note)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
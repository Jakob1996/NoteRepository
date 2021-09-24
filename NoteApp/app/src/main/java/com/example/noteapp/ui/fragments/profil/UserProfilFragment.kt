package com.example.noteapp.ui.fragments.profil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.noteapp.data.Category
import com.example.noteapp.data.Note
import com.example.noteapp.databinding.FragmentProfilBinding
import com.example.noteapp.navigation.Navigation
import com.example.noteapp.viewmodels.NoteViewModel
import com.example.noteapp.viewmodels.ProfilViewModel
import com.example.noteapp.viewmodels.TodoViewModel
import com.google.firebase.auth.FirebaseAuth


class UserProfilFragment : Fragment(), Navigation {

    private lateinit var noteViewModel: NoteViewModel

    private lateinit var todoViewModel: TodoViewModel

    private lateinit var profilViewModel: ProfilViewModel

    private val fbAuth = FirebaseAuth.getInstance()

    private var _binding: FragmentProfilBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteViewModel = ViewModelProvider(requireActivity())[NoteViewModel::class.java]
        todoViewModel = ViewModelProvider(requireActivity())[TodoViewModel::class.java]
        profilViewModel = ViewModelProvider(requireActivity())[ProfilViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentProfilBinding.inflate(inflater, container, false)

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    popBackStack("main", requireActivity().supportFragmentManager, true)
                    isEnabled = false
                }
            }
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragmentProfilSaveBtn.setOnClickListener {
            profilViewModel.addNotesToCloud(noteViewModel.getAllNotes.value!!)
        }

        binding.fragmentProfilRemoveFirebaseBtn.setOnClickListener {
            val a = noteViewModel.getAllNotes.value
            a?.let { it1 ->
                profilViewModel.deleteDataFromFirebase(it1)
            }
        }

        binding.fragmentProfilLogoutBtn.setOnClickListener {
            fbAuth.signOut()
            Toast.makeText(activity, "Logged out successfully!!", Toast.LENGTH_LONG).show()
            popBackStack("main", requireActivity().supportFragmentManager, true)
        }

        binding.fragmentProfilGetFirebaseBtn.setOnClickListener {
            profilViewModel.addNotesToCloud(noteViewModel.getAllNotes.value!!)

            profilViewModel.getNotesFromFirebase().observe(viewLifecycleOwner, {
                val allNotes = noteViewModel.getAllNotes
                it.forEach { note ->
                    insertOrUpdateNotes(note)
                }
            })
//            profilViewModel.getCategoriesFromFirebase().observe(viewLifecycleOwner, {
//                it.forEach {
//                    insertOrUpdateCategory(it)
//                }
//            })
        }
    }

    private fun insertOrUpdateNotes(note: Note) {

        noteViewModel.insertNote(note)
    }

    private fun insertOrUpdateCategory(category: Category) {
        //todoViewModel.insertCategotyItem(category)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
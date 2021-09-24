package com.example.noteapp.ui.fragments.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.noteapp.R
import com.example.noteapp.data.Note
import com.example.noteapp.databinding.FragmentLoginBinding
import com.example.noteapp.ui.fragments.baseFragment.BaseFragment
import com.example.noteapp.viewmodels.NoteViewModel
import com.example.noteapp.viewmodels.ProfilViewModel
import com.google.android.material.snackbar.Snackbar


class LoginFragment : BaseFragment() {

    private val LOG_DEB = "LOG_DEBUG"

    private lateinit var profilViewModel: ProfilViewModel

    private lateinit var noteViewModel: NoteViewModel

    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        profilViewModel = ViewModelProvider(requireActivity())[ProfilViewModel::class.java]
        noteViewModel = ViewModelProvider(requireActivity())[NoteViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragmentLoginLoginBtn.setOnClickListener {
            val email = binding.fragmentLoginEmailEt.text?.trim().toString()
            val password = binding.fragmentLoginPasswordEt.text?.trim().toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                fbAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener { authRes ->
                        if (fbAuth.currentUser != null) {
                            profilViewModel.addNotesToCloud(noteViewModel.getAllNotes.value!!)

                            profilViewModel.getNotesFromFirebase()
                                .observe(viewLifecycleOwner, { list ->

                                    list.forEach {
                                        insertOrUpdate(it)
                                    }
                                })
                            Snackbar.make(requireView(), "Cloud updated", Snackbar.LENGTH_LONG)
                                .show()

                            navigateToFragment(
                                findNavController(),
                                R.id.action_login_fragment_to_user_profil_fragment
                            )
                        }
                    }
                    .addOnFailureListener { exc ->
                        Snackbar.make(requireView(), "Upss", Snackbar.LENGTH_SHORT).show()
                    }
            }
        }

        binding.fragmentLoginRegisterBtn.setOnClickListener {

            navigateToFragment(
                findNavController(), R.id.registrationFragment
            )
        }
    }

    private fun insertOrUpdate(note: Note) {
        noteViewModel.insertNote(note)
    }
}
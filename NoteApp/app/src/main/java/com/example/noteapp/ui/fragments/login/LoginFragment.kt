package com.example.noteapp.ui.fragments.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.noteapp.data.Note
import com.example.noteapp.databinding.FragmentLoginBinding
import com.example.noteapp.ui.fragments.profile.ProfileFragment
import com.example.noteapp.ui.fragments.registration.RegistrationFragment
import com.example.noteapp.viewmodels.NoteViewModel
import com.example.noteapp.viewmodels.ProfilViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth


class LoginFragment : Fragment(), com.example.noteapp.navigation.Navigation {

    private val fbAuth = FirebaseAuth.getInstance()

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

        binding.logInButton.setOnClickListener {
            val email = binding.signInEmail.text?.trim().toString()
            val password = binding.signInPassword.text?.trim().toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                fbAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener { authRes ->
                        Log.d("logd", "${authRes.user}")
                        if (fbAuth.currentUser != null) {
                            profilViewModel.addNotesToCloud(noteViewModel.allNotes.value!!)
                            Log.d("logd", "${fbAuth.currentUser}")
                            profilViewModel.getNotesFromFirebase().observe(viewLifecycleOwner, {

                                it.forEach {
                                    insertOrUpdate(it)
                                }
                            })
                            Snackbar.make(requireView(), "Cloud updated", Snackbar.LENGTH_LONG)
                                .show()

                            navigateToFragment(
                                ProfileFragment(),
                                "ProfFrag",
                                requireActivity().supportFragmentManager
                            )
                        }
                    }
                    .addOnFailureListener { exc ->
                        Snackbar.make(requireView(), "Upss", Snackbar.LENGTH_SHORT).show()
                        Log.d(LOG_DEB, exc.message.toString())
                    }
            }
        }

        binding.signUpButton.setOnClickListener {

            navigateToFragment(
                RegistrationFragment(),
                "RegFrag",
                requireActivity().supportFragmentManager
            )
        }
    }

    private fun insertOrUpdate(note: Note) {
        noteViewModel.insertNote(note)
    }
}
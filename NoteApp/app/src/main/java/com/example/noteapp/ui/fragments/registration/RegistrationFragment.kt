package com.example.noteapp.ui.fragments.registration

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.noteapp.R
import com.example.noteapp.databinding.FragmentRegistrationBinding
import com.example.noteapp.navigation.Navigation
import com.example.noteapp.viewmodels.ProfilViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth


class RegistrationFragment : Fragment(), Navigation {

    private val REG_DEB = "LOG_DEBUG"

    private val fbAuth = FirebaseAuth.getInstance()

    private lateinit var profilViewModel: ProfilViewModel

    private var _binding: FragmentRegistrationBinding? = null

    private val binding get() = _binding!!

    override fun onDestroyView() {
        _binding = null

        super.onDestroyView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)

        profilViewModel = ViewModelProvider(requireActivity())[ProfilViewModel::class.java]


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragmentRegistrationRegistrationBtn.setOnClickListener {
            val email = binding.fragmentRegistrationEmailEt.text?.trim().toString()
            val password = binding.fragmentRegistrationPasswordEt.text?.trim().toString()
            val repeatPassword =
                binding.fragmentRegistrationPasswordRepeatEt.text?.trim().toString()

            if (password == repeatPassword) {
                fbAuth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener { authRes ->
                        if (authRes.user != null) {
                            navigateToFragment(
                                findNavController(), R.id.userProfileFragment
                            )
                        }
                    }
                    .addOnFailureListener { exc ->
                        Snackbar.make(requireView(), "Upss", Snackbar.LENGTH_SHORT).show()
                        Log.d(REG_DEB, exc.message.toString())
                    }
            }
        }
    }
}
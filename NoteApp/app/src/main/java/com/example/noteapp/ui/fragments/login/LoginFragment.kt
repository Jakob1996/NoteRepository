package com.example.noteapp.ui.fragments.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.noteapp.R
import com.example.noteapp.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth


class LoginFragment : Fragment() {
    private val fbAuth = FirebaseAuth.getInstance()
    private val LOG_DEB = "LOG_DEBUG"

    private var _binding:FragmentLoginBinding? = null

    private val binding get() = _binding!!

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.logInButton.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                val email = binding.signInEmail.text?.trim().toString()
                val password = binding.signInPassword.text?.trim().toString()
                fbAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        authRes ->
                        if(authRes.user!=null) {
                            findNavController().navigate(R.id.action_loginFragment_to_profileFragment)
                        }
                    }
                    .addOnFailureListener{
                        exc ->
                        Snackbar.make(requireView(), "Upss", Snackbar.LENGTH_SHORT).show()
                        Log.d(LOG_DEB, exc.message.toString())
                    }
            }
        })

        binding.signUpButton.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
            }
        })
    }
}
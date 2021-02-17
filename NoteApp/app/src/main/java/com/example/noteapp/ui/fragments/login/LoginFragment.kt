package com.example.noteapp.ui.fragments.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.noteapp.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : Fragment() {
    private val fbAuth = FirebaseAuth.getInstance()
    private val LOG_DEB = "LOG_DEBUG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logInButton.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                val email = signInEmail.text?.trim().toString()
                val password = signInPassword.text?.trim().toString()
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

        signUpButton.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
            }
        })
    }
}
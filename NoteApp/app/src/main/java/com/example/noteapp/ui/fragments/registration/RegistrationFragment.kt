package com.example.noteapp.ui.fragments.registration

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
import kotlinx.android.synthetic.main.fragment_registration.*


class RegistrationFragment : Fragment() {
    private val REG_DEB = "LOG_DEBUG"
    private val fbAuth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signUpButtonInSignUpFragment.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                val email = emailInRegistrationFragment.text?.trim().toString()
                val password = passwordInRegistrationFragment.text?.trim().toString()
                val repeatPassword = repeatPassword.text?.trim().toString()

                if(password==repeatPassword){
                    fbAuth.createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener {
                            authRes ->
                            if(authRes.user!=null){
                                findNavController().navigate(R.id.action_registrationFragment_to_profileFragment)
                            }
                        }
                        .addOnFailureListener { exc ->
                            Snackbar.make(requireView(), "Upss", Snackbar.LENGTH_SHORT).show()
                            Log.d(REG_DEB, exc.message.toString())
                        }
                }
            }
        })
    }
}
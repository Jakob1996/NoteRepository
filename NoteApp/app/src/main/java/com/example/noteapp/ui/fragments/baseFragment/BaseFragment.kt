package com.example.noteapp.ui.fragments.baseFragment

import androidx.fragment.app.Fragment
import com.example.noteapp.ui.interfaces.BackPressedListener
import com.google.firebase.auth.FirebaseAuth

abstract class BaseFragment : Fragment(), com.example.noteapp.navigation.Navigation,
    BackPressedListener {

    val fbAuth = FirebaseAuth.getInstance()
}

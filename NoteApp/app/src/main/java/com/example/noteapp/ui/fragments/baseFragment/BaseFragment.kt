package com.example.noteapp.ui.fragments.baseFragment

import androidx.fragment.app.Fragment
import com.example.noteapp.navigation.Navigation
import com.example.noteapp.ui.interfaces.BackPressedListener
import com.google.firebase.auth.FirebaseAuth

abstract class BaseFragment:Fragment(), Navigation, BackPressedListener {

    val fbAuth = FirebaseAuth.getInstance()
}

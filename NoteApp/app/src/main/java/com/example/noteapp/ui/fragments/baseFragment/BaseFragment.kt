package com.example.noteapp.ui.fragments.baseFragment

import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth

abstract class BaseFragment : Fragment(), com.example.noteapp.navigation.Navigation {

    val fbAuth = FirebaseAuth.getInstance()
}

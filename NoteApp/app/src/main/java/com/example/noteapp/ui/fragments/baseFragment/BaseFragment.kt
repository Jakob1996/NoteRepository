package com.example.noteapp.ui.fragments.baseFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.noteapp.ui.interfaces.BackPressedListener
import com.google.firebase.auth.FirebaseAuth

abstract class BaseFragment : Fragment(), com.example.noteapp.navigation.Navigation,
    BackPressedListener {

    val fbAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("fsdfs", "fsfsdf")

        super.onCreate(savedInstanceState)
    }
}

package com.example.noteapp.ui.fragments.baseFragment

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.example.noteapp.ui.interfaces.ToolbarAction
import com.google.firebase.auth.FirebaseAuth

abstract class BaseFragment : Fragment(), com.example.noteapp.navigation.Navigation {

    val fbAuth = FirebaseAuth.getInstance()

    fun showSoftKeyboard() {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
    }
    fun closeKeyboard() {
        val view = requireActivity().currentFocus
        if (view != null) {
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
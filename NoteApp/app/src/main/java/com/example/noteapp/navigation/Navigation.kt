package com.example.noteapp.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavActionBuilder
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.example.noteapp.R
import com.example.noteapp.ui.fragments.sort.SortDialogFragment

interface Navigation {

    fun navigateToFragment(navController: NavController, idAction: Int) {
        navController.navigate(idAction)
    }

    fun showDialog(fragment: Fragment, tagName: String, fm: FragmentManager, requestCode: Int) {
        val dialog = SortDialogFragment()
        dialog.setTargetFragment(
            fragment,
            requestCode
        )
        dialog.show(
            fm,
            tagName
        )
    }

    fun popBackStack(fragmentTag: String, fm: FragmentManager, popBackStackInclusive: Boolean) {
        fm.popBackStack(
            fragmentTag,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
    }
}
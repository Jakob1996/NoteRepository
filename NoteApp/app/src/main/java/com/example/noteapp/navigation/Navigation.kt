package com.example.noteapp.navigation

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.noteapp.R
import com.example.noteapp.ui.fragments.sort.SortDialogFragment

interface Navigation {
    fun navigateToFragment(fragment: Fragment, name: String, fm: FragmentManager) {
        fm.beginTransaction().setCustomAnimations(
            R.anim.animation_into, R.anim.animation_zero,
            R.anim.animation_zero, R.anim.animation_backto
        ).add(
            R.id.container_keeper,
            fragment
        ).addToBackStack(name).commit()
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
package com.example.noteapp.navigation

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.noteapp.R

interface Navigation {
    fun navigateToFragment(fragment:Fragment, name:String, fm:FragmentManager){
        val transaction = fm.beginTransaction().setCustomAnimations(
            R.anim.animation_into, R.anim.animation_zero,
            R.anim.animation_zero, R.anim.animation_backto
        ).add(R.id.container_keeper,
            fragment).addToBackStack(name).commit()
    }

    fun showDialog(dialog:DialogFragment, tagName:String, fm:FragmentManager, requestCode:Int){
        dialog.setTargetFragment(dialog, requestCode)
        dialog.show(fm, tagName)
    }

    fun popBackStack(fragmentTag:String , fm:FragmentManager, popBackStackInclusive:Boolean){
        fm.popBackStack(fragmentTag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
}
package com.example.noteapp.ui.fragments.todo

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.noteapp.R
import com.example.noteapp.viewmodels.ViewModel


class AddCategoryFragmentDialog : DialogFragment() {
    private lateinit var viewModel:ViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        viewModel = ViewModelProvider(requireActivity())[ViewModel::class.java]
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val view: View = inflater.inflate(R.layout.fragment_add_category_dialog, null)


        return super.onCreateDialog(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }
}
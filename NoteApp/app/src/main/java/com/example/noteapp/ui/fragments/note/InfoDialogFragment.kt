package com.example.noteapp.ui.fragments.note

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.noteapp.databinding.FragmentInfoDialogBinding
import com.example.noteapp.viewmodels.NoteViewModel

class InfoDialogFragment : DialogFragment() {
    private lateinit var noteViewModel:NoteViewModel

    private var _binding:FragmentInfoDialogBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteViewModel = ViewModelProvider(requireActivity())[NoteViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        _binding = FragmentInfoDialogBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val value = noteViewModel.getSelectedNote().value!!.message.length
        binding.textLenghtValue.text = value.toString()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
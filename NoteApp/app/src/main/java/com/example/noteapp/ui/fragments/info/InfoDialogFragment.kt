package com.example.noteapp.ui.fragments.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.noteapp.R
import com.example.noteapp.databinding.InfoDialogBinding
import com.example.noteapp.viewmodels.NoteViewModel

class InfoDialogFragment : DialogFragment() {
    private lateinit var noteViewModel: NoteViewModel

    private var _binding: InfoDialogBinding? = null

    private val binding get() = _binding!!

    override fun getTheme(): Int = R.style.RoundedCornersDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteViewModel = ViewModelProvider(requireActivity())[NoteViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = InfoDialogBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val value = noteViewModel.getSelectedNote().value!!.message.length
        binding.infoDialogLenghtTv.text = value.toString()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
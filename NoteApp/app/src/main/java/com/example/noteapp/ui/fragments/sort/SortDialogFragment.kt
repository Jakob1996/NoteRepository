package com.example.noteapp.ui.fragments.sort

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.noteapp.R
import com.example.noteapp.databinding.SortDialogBinding
import com.example.noteapp.viewmodels.NoteViewModel
import com.example.noteapp.viewmodels.TodoViewModel

class SortDialogFragment : DialogFragment() {

    private lateinit var noteViewModel: NoteViewModel
    private lateinit var todoViewModel: TodoViewModel

    private var _binding: SortDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteViewModel = ViewModelProvider(requireActivity())[NoteViewModel::class.java]
        todoViewModel = ViewModelProvider(requireActivity())[TodoViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        _binding = SortDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (noteViewModel.sharePreferences.loadSortDescendingData()) {
            binding.fragmentDialogRg.check(binding.fragmentDialogDescRg.id)
        } else {
            binding.fragmentDialogRg.check(binding.fragmentDialogAscRg.id)
        }

        binding.sortDialogConfirmTv.setOnClickListener {
            when (binding.fragmentDialogRg.checkedRadioButtonId) {
                R.id.fragmentDialogDescRg -> {
                    noteViewModel.sharePreferences.saveSortDescendingData(true)
                    noteViewModel.sortDescendingNote.postValue(true)
                    todoViewModel.sortDescendingCategory.postValue(true)
                }

                R.id.fragmentDialogAscRg -> {
                    noteViewModel.sharePreferences.saveSortDescendingData(false)
                    noteViewModel.sortDescendingNote.postValue(false)
                    todoViewModel.sortDescendingCategory.postValue(false)
                }
            }
            dialog?.dismiss()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
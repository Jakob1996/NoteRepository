package com.example.noteapp.ui.fragments.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.noteapp.data.Category
import com.example.noteapp.databinding.AddItemDialogBinding
import com.example.noteapp.viewmodels.TodoViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class DialogAddCategoryItem : DialogFragment() {

    private lateinit var todoViewModel: TodoViewModel

    private val fbAuth = FirebaseAuth.getInstance()

    private var _binding: AddItemDialogBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        todoViewModel = ViewModelProvider(requireActivity())[TodoViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AddItemDialogBinding.inflate(inflater, container, false)


        binding.addItemDialogAddBtn.setOnClickListener {
            if (binding.addItemDialogTitleEt.text.isEmpty()) {
                dismiss()
            } else {
                val category = Category(
                    binding.addItemDialogTitleEt.text.toString(),
                    "#333333",
                    false,
                    Calendar.getInstance().timeInMillis
                ).apply { rowIdCategory = Calendar.getInstance().timeInMillis.toInt() }

                if (fbAuth.currentUser != null) {
                    todoViewModel.saveCategoryInCloud(category)
                }
                todoViewModel.insertCategotyItem(category)
                dismiss()
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
package com.example.noteapp.ui.fragments.todo

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.noteapp.R
import com.example.noteapp.data.ItemOfList
import com.example.noteapp.databinding.AddItemDialogBinding
import com.example.noteapp.viewmodels.TodoViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class AddToDoDialog : DialogFragment() {

    private lateinit var todoViewModel: TodoViewModel

    private val auth = FirebaseAuth.getInstance()

    private var _binding: AddItemDialogBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        todoViewModel = ViewModelProvider(requireActivity())[TodoViewModel::class.java]
    }

    override fun getTheme(): Int = R.style.RoundedCornersDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = AddItemDialogBinding.inflate(inflater, container, false)

        todoViewModel.getSelectedItem().observe(viewLifecycleOwner, {
            binding.addItemDialogTitleEt.setText(it?.nameItem)
        })

        binding.addItemDialogAddBtn.setOnClickListener {
            if (todoViewModel.getSelectedItem().value == null) {
                if (binding.addItemDialogTitleEt.text.toString().isEmpty()) {
                    dismiss()
                } else {
                    val item = ItemOfList(
                        binding.addItemDialogTitleEt.text.toString(),
                        todoViewModel.getSelectedCategotyItem().value!!.rowIdCategory
                    ).apply {
                        idItem = Calendar.getInstance().timeInMillis.toInt()
                    }
                    if (auth.currentUser != null) {
                        todoViewModel.saveTodoListInCloud(
                            todoViewModel.getSelectedCategotyItem().value!!.rowIdCategory,
                            listOf(item)
                        )
                    }
                    todoViewModel.insertItem(item)
                    dismiss()
                }
            } else {
                if (binding.addItemDialogTitleEt.text.toString().isEmpty()) {
                    dismiss()
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        val it = todoViewModel.getSelectedItem().value
                        val item =
                            ItemOfList(
                                binding.addItemDialogTitleEt.text.toString(),
                                it!!.categoryId,
                                false
                            )
                        item.idItem = it.idItem
                        todoViewModel.updateItem(item)
                        dismiss()
                    }
                }
            }

            /*
                        viewModel.getSelectedItem().observe(viewLifecycleOwner, Observer { note-> note.let {
                            if(it?.nameItem.isNullOrEmpty()){
                                val item = ItemOfList(item_name.text.toString(), viewModel.getSelectedCategotyItem().value!!.rowIdCategory)
                                viewModel.insertItem(item)
                            } else{
                                val item = ItemOfList(item_name.text.toString(), it!!.categoryId, false)
                                item.idItem = it.idItem
                                viewModel.updateItem(item)
                            }
                            viewModel.setSelectedItem(null)
                            dismiss()
                        }
                        })
            */
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val window = dialog?.window
        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    }

    override fun onDestroy() {
        super.onDestroy()
        todoViewModel.setSelectedItem(null)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
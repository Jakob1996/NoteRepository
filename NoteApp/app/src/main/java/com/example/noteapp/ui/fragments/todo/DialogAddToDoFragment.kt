package com.example.noteapp.ui.fragments.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.noteapp.data.ItemOfList
import com.example.noteapp.databinding.FragmentAddItemDialogBinding
import com.example.noteapp.viewmodels.ToDoViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class DialogAddToDoFragment:DialogFragment() {
    private lateinit var todoViewModel: ToDoViewModel
    private val auth = FirebaseAuth.getInstance()

    private var _binding:FragmentAddItemDialogBinding? = null

    private val binding get() =  _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        todoViewModel = ViewModelProvider(requireActivity())[ToDoViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAddItemDialogBinding.inflate(inflater, container, false)

        todoViewModel.getSelectedItem().observe(viewLifecycleOwner, Observer {
            binding.itemName.setText(it?.nameItem)
        })

        binding.addButton.setOnClickListener {
            if (todoViewModel.getSelectedItem().value == null) {
                if (binding.itemName.text.toString().isEmpty()) {
                    dismiss()
                } else {
                    val item = ItemOfList(
                        binding.itemName.text.toString(),
                        todoViewModel.getSelectedCategotyItem().value!!.rowIdCategory
                    ).apply {
                        idItem = Calendar.getInstance().timeInMillis.toInt()
                    }
                    if(auth.currentUser!= null){
                        todoViewModel.saveTodoListInCloud(todoViewModel.getSelectedCategotyItem().value!!.rowIdCategory, listOf(item))
                    }
                    todoViewModel.insertItem(item)
                    dismiss()
                }
            } else {
                if (binding.itemName.text.toString().isEmpty()) {
                    dismiss()
                } else {
                    val it = todoViewModel.getSelectedItem().value
                    val item = ItemOfList(binding.itemName.text.toString(), it!!.categoryId, false)
                    item.idItem = it.idItem
                    todoViewModel.updateItem(item)
                    dismiss()
                }
            }

            /*
                    viewModel.getSelectedItem().observe(viewLifecycleOwner, Observer {note-> note.let {

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

    override fun onDestroy() {
        super.onDestroy()
        todoViewModel.setSelectedItem(null)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
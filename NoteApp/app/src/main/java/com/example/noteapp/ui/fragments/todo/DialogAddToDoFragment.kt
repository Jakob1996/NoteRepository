package com.example.noteapp.ui.fragments.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.noteapp.R
import com.example.noteapp.data.ItemOfList
import com.example.noteapp.viewmodels.ViewModel
import kotlinx.android.synthetic.main.fragment_add_item_dialog.*

class DialogAddToDoFragment:DialogFragment() {
    private lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[ViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView:View = inflater.inflate(R.layout.fragment_add_item_dialog, container, false)
        val addButton = rootView.findViewById<Button>(R.id.add_button)

        viewModel.getSelectedItem().observe(viewLifecycleOwner, Observer {
            item_name.setText(it?.nameItem)
        })

        addButton.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {

                if(viewModel.getSelectedItem().value == null){
                    if(item_name.text.toString().isNullOrEmpty()){
                        dismiss()
                    }else {
                        val item = ItemOfList(item_name.text.toString(), viewModel.getSelectedCategotyItem().value!!.rowIdCategory)
                        viewModel.insertItem(item)
                        dismiss()
                    }
                } else{
                    if(item_name.text.toString().isNullOrEmpty()){
                        dismiss()
                    } else{
                        val it = viewModel.getSelectedItem().value
                        val item = ItemOfList(item_name.text.toString(), it!!.categoryId, false)
                        item.idItem = it.idItem
                        viewModel.updateItem(item)
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
        })

        return rootView
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.setSelectedItem(null)
    }
}
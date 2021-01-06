package com.example.noteapp.ui.fragments.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
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


        addButton.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                if(item_name.text.isEmpty()){
                    dismiss()
                } else {
                    val item = ItemOfList(item_name.text.toString(), viewModel.getSelectedCategotyItem().value!!.rowIdCategory)
                    viewModel.insertItem(item)
                    dismiss()
                }
            }
        })

        return rootView
    }
}
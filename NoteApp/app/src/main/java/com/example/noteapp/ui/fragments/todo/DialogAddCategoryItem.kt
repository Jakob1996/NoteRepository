package com.example.noteapp.ui.fragments.todo

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.noteapp.R
import com.example.noteapp.data.Category
import com.example.noteapp.viewmodels.ViewModel
import kotlinx.android.synthetic.main.fragment_add_category_dialog.*
import java.util.*

class DialogAddCategoryItem:DialogFragment() {
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
        val rootView: View = inflater.inflate(R.layout.fragment_add_category_dialog, container, false)
        val save = rootView.findViewById<Button>(R.id.add_category_button)

        save.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                if(category_title_editText.text.isEmpty()){
                    dismiss()
                } else{
                    val category = Category(category_title_editText.text.toString(), "#333333",false, Calendar.getInstance().timeInMillis)
                    viewModel.insertCategotyItem(category)
                    dismiss()
                }
            }
        })
        return rootView
    }
}
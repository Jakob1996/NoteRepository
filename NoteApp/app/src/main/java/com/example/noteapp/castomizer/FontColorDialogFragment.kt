package com.example.noteapp.castomizer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.noteapp.R
import com.example.noteapp.viewmodels.ViewModel
import kotlinx.android.synthetic.main.fragment_add_edit_to_do.*
import kotlinx.android.synthetic.main.fragment_font_color_dialog.*


class FontColorDialogFragment : DialogFragment() {
    private lateinit var viewModel: ViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[ViewModel::class.java]

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_font_color_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fontColor1.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                title_addEditFragCategory.setTextColor(R.id.fontColor1)
            }
        })

        fontColor2.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                title_addEditFragCategory.setTextColor(R.id.fontColor2)
            }
        })

        fontColor3.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                title_addEditFragCategory.setTextColor(R.id.fontColor3)
            }
        })

        fontColor4.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                title_addEditFragCategory.setTextColor(R.id.fontColor4)
            }
        })

        fontColor5.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                title_addEditFragCategory.setTextColor(R.id.imageColor3)
            }
        })
    }


}
package com.example.noteapp.ui.fragments.note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.noteapp.R
import com.example.noteapp.viewmodels.ViewModel
import kotlinx.android.synthetic.main.fragment_check_password.*


class CheckPasswordFragment : Fragment() {
    private lateinit var viewModel: ViewModel
    private var quit:Int=1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[ViewModel::class.java]

        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    isEnabled = false
                    quit = 2
                    viewModel.isSearchEdit = 1
                    requireActivity().onBackPressed()
                }
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_check_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        check_add_password_button.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                viewModel.getSelectedNote().observe(viewLifecycleOwner, Observer{
                    if(it!=null) {
                        if (check_password_editText.text.isNotEmpty()) {
                            if (check_password_editText.text.toString().toInt() == it.password) {
                                findNavController().navigate(R.id.action_checkPasswordFragment_to_addEditNoteFragment)
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "Password is not correct",
                                    Toast.LENGTH_SHORT
                                ).show()
                                check_password_editText.setText("")
                            }
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Password is not correct",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })

                viewModel.getSelectedCategotyItem().observe(viewLifecycleOwner, Observer {
                    if(it!=null){
                        if(check_password_editText.text.isNotEmpty()){
                            if(check_password_editText.text.toString().toInt() == it.password){
                                findNavController().navigate(R.id.action_checkPasswordFragment_to_addEditToDoFragment)
                            } else{
                                Toast.makeText(
                                    requireContext(),
                                    "Password is not correct",
                                    Toast.LENGTH_SHORT
                                ).show()
                                check_password_editText.setText("")
                            }
                        } else{
                            Toast.makeText(
                                requireContext(),
                                "Password is not correct",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })

            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()

        if(quit==2){
            viewModel.setSelectedNote(null)
            viewModel.noteBeforeChange = null

            viewModel.setSelectedCategotyItem(null)
            viewModel.categoryItemBeforeChange = null
        }
    }
}
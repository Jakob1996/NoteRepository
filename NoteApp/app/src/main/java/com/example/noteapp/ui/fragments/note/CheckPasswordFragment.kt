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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.noteapp.R
import com.example.noteapp.databinding.FragmentCheckPasswordBinding
import com.example.noteapp.viewmodels.NoteViewModel
import com.example.noteapp.viewmodels.ToDoViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CheckPasswordFragment : Fragment() {
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var todoViewModel: ToDoViewModel
    private var quit:Int=1

    private var _binding:FragmentCheckPasswordBinding? = null

    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteViewModel = ViewModelProvider(requireActivity())[NoteViewModel::class.java]
        todoViewModel = ViewModelProvider(requireActivity())[ToDoViewModel::class.java]

        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    isEnabled = false
                    quit = 2
                    noteViewModel.isSearchEdit = 1
                    lifecycleScope.launch{
                        withContext(Dispatchers.Main){
                            requireActivity().onBackPressed()
                        }
                    }
                }
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCheckPasswordBinding.inflate(inflater, container ,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.checkAddPasswordButton.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                noteViewModel.getSelectedNote().observe(viewLifecycleOwner, Observer{
                    if(it!=null) {
                        if (binding.checkPasswordEditText.text.isNotEmpty()) {
                            if (binding.checkPasswordEditText.text.toString().toInt() == it.password) {
                                findNavController().navigate(R.id.action_checkPasswordFragment_to_addEditNoteFragment)
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "Password is not correct",
                                    Toast.LENGTH_SHORT
                                ).show()
                                binding.checkPasswordEditText.setText("")
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

                todoViewModel.getSelectedCategotyItem().observe(viewLifecycleOwner, Observer {
                    if(it!=null){
                        if(binding.checkPasswordEditText.text.isNotEmpty()){
                            if(binding.checkPasswordEditText.text.toString().toInt() == it.password){
                                findNavController().navigate(R.id.action_checkPasswordFragment_to_addEditToDoFragment)
                            } else{
                                Toast.makeText(
                                    requireContext(),
                                    "Password is not correct",
                                    Toast.LENGTH_SHORT
                                ).show()
                                binding.checkPasswordEditText.setText("")
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
        _binding = null

        super.onDestroyView()

        if(quit==2){
            noteViewModel.setSelectedNote(null)
            noteViewModel.noteBeforeChange = null

            todoViewModel.setSelectedCategotyItem(null)
            todoViewModel.categoryItemBeforeChange = null
        }
    }
}
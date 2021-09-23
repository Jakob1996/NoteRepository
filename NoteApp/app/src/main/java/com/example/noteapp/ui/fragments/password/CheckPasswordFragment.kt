package com.example.noteapp.ui.fragments.password

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.noteapp.R
import com.example.noteapp.databinding.FragmentCheckPasswordBinding
import com.example.noteapp.navigation.Navigation
import com.example.noteapp.tools.OnBackPressListener
import com.example.noteapp.viewmodels.NoteViewModel
import com.example.noteapp.viewmodels.ToDoViewModel

class CheckPasswordFragment : Fragment(), OnBackPressListener, Navigation {
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var todoViewModel: ToDoViewModel
    private var quit: Int = 1

    private var _binding: FragmentCheckPasswordBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteViewModel = ViewModelProvider(requireActivity())[NoteViewModel::class.java]
        todoViewModel = ViewModelProvider(requireActivity())[ToDoViewModel::class.java]

        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {

                Log.d("dfds", "fsdf")
                requireActivity().supportFragmentManager.popBackStack()
                isEnabled = false
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCheckPasswordBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragmentCheckPasswordOkBtn.setOnClickListener {
            if (noteViewModel.getSelectedNote().value != null) {
                val password = noteViewModel.getSelectedNote().value!!.password
                if (it != null) {
                    if (binding.fragmentCheckPasswordPasswordEt.text.isNotEmpty()) {
                        if (binding.fragmentCheckPasswordPasswordEt.text.toString().toInt() == password) {
                            val fragManager = requireActivity().supportFragmentManager
                            navigateToFragment( findNavController(),R.id.generalNoteFragment)
                            closeKeyboard()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Password is not correct",
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.fragmentCheckPasswordPasswordEt.setText("")
                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Password is not correct",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                val password = todoViewModel.getSelectedCategotyItem().value!!.password
                if (binding.fragmentCheckPasswordPasswordEt.text.isNotEmpty()) {
                    if (binding.fragmentCheckPasswordPasswordEt.text.toString().toInt() == password) {
                        findNavController().navigate(R.id.action_check_password_fragment_to_general_todo_fragment)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Password is not correct",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.fragmentCheckPasswordPasswordEt.setText("")
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Password is not correct",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        }
    }

    override fun onDestroyView() {
        Log.d("fdfsd","onDestroy CheckPaas")

        binding.root.removeAllViewsInLayout()

        _binding = null

        super.onDestroyView()

        if (quit == 2) {
            noteViewModel.setSelectedNote(null)
            noteViewModel.noteBeforeChange = null

            todoViewModel.setSelectedCategotyItem(null)
            todoViewModel.categoryItemBeforeChange = null
        }
    }

    private fun closeKeyboard() {
        val view = requireActivity().currentFocus
        if (view != null) {
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun onBackPressed() {
        quit = 2
        noteViewModel.isSearchEdit = 1
        requireActivity().supportFragmentManager.popBackStack()
    }
}
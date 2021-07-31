package com.example.noteapp.ui.fragments.note

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
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

        binding.checkAddPasswordButton.setOnClickListener {
            if (noteViewModel.getSelectedNote().value != null) {
                val password = noteViewModel.getSelectedNote().value!!.password
                if (it != null) {
                    if (binding.checkPasswordEditText.text.isNotEmpty()) {
                        if (binding.checkPasswordEditText.text.toString().toInt() == password) {
                            val fragManager = requireActivity().supportFragmentManager
                            navigateToFragment(BeforeEditNoteFragment(), "fragCheck", fragManager)
                            closeKeyboard()
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
            } else {
                val password = todoViewModel.getSelectedCategotyItem().value!!.password
                if (binding.checkPasswordEditText.text.isNotEmpty()) {
                    if (binding.checkPasswordEditText.text.toString().toInt() == password) {
                        findNavController().navigate(R.id.action_checkPasswordFragment_to_addEditToDoFragment)
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
        }
    }

    override fun onDestroyView() {
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
        popBackStack("CheckPasswordFragment", requireActivity().supportFragmentManager, false)
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        return if(noteViewModel.n){
            val a: Animation = object : Animation() {}
            a.duration = 0
            return a
        } else{
            super.onCreateAnimation(transit, enter, nextAnim)
        }
    }
}
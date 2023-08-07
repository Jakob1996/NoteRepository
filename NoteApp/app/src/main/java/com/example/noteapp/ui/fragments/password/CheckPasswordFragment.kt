package com.example.noteapp.ui.fragments.password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.noteapp.R
import com.example.noteapp.databinding.FragmentCheckPasswordBinding
import com.example.noteapp.navigation.Navigation
import com.example.noteapp.tools.OnBackPressListener
import com.example.noteapp.ui.activities.MainActivity
import com.example.noteapp.ui.fragments.baseFragment.BaseFragment
import com.example.noteapp.viewmodels.NoteViewModel
import com.example.noteapp.viewmodels.TodoViewModel

class CheckPasswordFragment : BaseFragment(), OnBackPressListener, Navigation {

    private lateinit var noteViewModel: NoteViewModel

    private lateinit var todoViewModel: TodoViewModel
    private var quit: Int = 1

    private var _binding: FragmentCheckPasswordBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteViewModel = ViewModelProvider(requireActivity())[NoteViewModel::class.java]
        todoViewModel = ViewModelProvider(requireActivity())[TodoViewModel::class.java]

        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    isEnabled = false
                    requireActivity().onBackPressed()
                    closeKeyboard()
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

        val mainActivity = this.activity as MainActivity
        mainActivity.hideToolbar()

        binding.fragmentCheckPasswordOkBtn.setOnClickListener {
            if (noteViewModel.getSelectedNote().value != null) {
                val password = noteViewModel.getSelectedNote().value!!.password
                if (it != null) {
                    if (binding.fragmentCheckPasswordPasswordEt.text.isNotEmpty()) {
                        if (binding.fragmentCheckPasswordPasswordEt.text.toString()
                                .toInt() == password
                        ) {

                            if (noteViewModel.getIsFromMainFragmentNavigation()) {
                                navigateToFragment(
                                    findNavController(),
                                    R.id.action_to_general_note_fragment
                                )
                            } else {
                                navigateToFragment(
                                    findNavController(),
                                    R.id.action_check_note_password_fragment_to_general_todo_fragment
                                )
                            }
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
                    if (binding.fragmentCheckPasswordPasswordEt.text.toString()
                            .toInt() == password
                    ) {
                        if (todoViewModel.getIsFromMainFragmentNavigation()) {
                            findNavController().navigate(R.id.action_check_password_fragment_to_general_todo_fragment)
                        } else {
                            findNavController().navigate(R.id.action_check_search_password_fragment_to_general_todo_fragment)
                        }
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

        getRequestFocus()
    }

    override fun onDestroyView() {
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

    override fun onBackPressed() {
        quit = 2
        noteViewModel.isSearchEdit = 1
        requireActivity().supportFragmentManager.popBackStack()
    }

    private fun getRequestFocus() {
        binding.fragmentCheckPasswordPasswordEt.requestFocus()
        showSoftKeyboard()
    }
}
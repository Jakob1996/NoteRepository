package com.example.noteapp.ui.fragments.note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.noteapp.R
import com.example.noteapp.databinding.FragmentCheckPasswordDialogBinding

class CheckPasswordDialogFragment : DialogFragment() {
    private var _binding:FragmentCheckPasswordDialogBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentCheckPasswordDialogBinding.inflate(inflater, container, false)

        binding.checkButtonV2.setOnClickListener {
            val fragManager = requireActivity().supportFragmentManager
            val frag = BeforeEditNoteFragment()
            val transaction = fragManager.beginTransaction()

            transaction.setCustomAnimations(R.anim.from_right_to_left, R.anim.zero_to_zero, R.anim.zero_to_zero, R.anim.from_left_to_right)
                .add(R.id.container_keeper, frag, "noteF")
                .addToBackStack("noteF")
            transaction.commit()
        }
        return binding.root
    }

}
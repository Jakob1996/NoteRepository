package com.example.noteapp.ui.fragments.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.noteapp.R
import com.example.noteapp.databinding.FragmentFragmentsKeeperBinding

class FragmentsKeeper : Fragment() {
        private var _binding:FragmentFragmentsKeeperBinding? = null
        private val binding get() = _binding!!

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            _binding = FragmentFragmentsKeeperBinding.inflate(inflater, container, false)

            val fm = requireActivity().supportFragmentManager
            val fragmentTransaction = fm.beginTransaction()

            val mainFragment = MainFragment()
            fragmentTransaction.add(R.id.container_keeper, mainFragment)

            fragmentTransaction.commit()

            return binding.root
        }

        override fun onDestroyView() {
            _binding = null
            super.onDestroyView()
        }
}
package com.example.noteapp.ui.fragments.sort

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.noteapp.R
import com.example.noteapp.databinding.SortDialogBinding
import com.example.noteapp.ui.interfaces.OnItemClickDialogListener
import com.example.noteapp.viewmodels.NoteViewModel

class SortDialogFragment : DialogFragment() {

    private lateinit var listener: OnItemClickDialogListener

    private lateinit var noteViewModel: NoteViewModel

    private var _binding: SortDialogBinding? = null

    private val binding get() = _binding!!

//    override fun getTheme(): Int = R.style.RoundedCornersDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteViewModel = ViewModelProvider(requireActivity())[NoteViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        _binding = SortDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.sortDialogConfirmTv.setOnClickListener {
            when (binding.fragmentDialogRg.checkedRadioButtonId) {
                R.id.fragmentDialogDescRg -> {
                    noteViewModel.setSortDescCategory(true)
                }
                R.id.fragmentDialogAscRg -> {
                    noteViewModel.setSortDescCategory(false)
                }
            }
            dialog?.dismiss()
        }
    }

//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val builder = AlertDialog.Builder(requireContext())
//        val inflater = requireActivity().layoutInflater
//        val view: View = inflater.inflate(R.layout.sort_dialog, null)
//
//        radioGroup = view.findViewById(R.id.fragmentDialogRg)
//        builder.setView(view)
//            .setTitle("Sort date:")
//            .setPositiveButton("OK") { di, i ->
//                when (radioGroup.checkedRadioButtonId) {
//                    R.id.fragmentDialogDescRg -> {
//                        listener.onItemClickDialog(true)
//                    }
//                    R.id.fragmentDialogAscRg -> {
//                        listener.onItemClickDialog(false)
//                    }
//                }
//            }
//            .setNegativeButton("Cancel") { di, i: Int -> }
//        return builder.create()
//    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
//        listener = try {
//            targetFragment as OnItemClickDialogListener
//        } catch (e: TypeCastException) {
//            throw TypeCastException()
//        }
    }



    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
package com.example.noteapp.ui.fragments.sort

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.RadioGroup
import androidx.fragment.app.DialogFragment
import com.example.noteapp.R
import com.example.noteapp.ui.interfaces.OnItemClickDialogListener

class SortDialogFragment : DialogFragment() {

    private lateinit var listener: OnItemClickDialogListener

    private lateinit var radioGroup: RadioGroup

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val view: View = inflater.inflate(R.layout.sort_dialog, null)

        radioGroup = view.findViewById(R.id.fragmentDialogRg)
        builder.setView(view)
            .setTitle("Sort date:")
            .setPositiveButton("OK") { di, i ->
                when (radioGroup.checkedRadioButtonId) {
                    R.id.fragmentDialogDescRg -> {
                        listener.onItemClickDialog(true)
                    }
                    R.id.fragmentDialogAscRg -> {
                        listener.onItemClickDialog(false)
                    }
                }
            }
            .setNegativeButton("Cancel") { di, i: Int -> }
        return builder.create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = try {
            targetFragment as OnItemClickDialogListener

        } catch (e: TypeCastException) {
            throw TypeCastException()
        }
    }
}
package com.example.noteapp.ui.fragments.miscellaneous

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.noteapp.databinding.NoteCastomizerBinding
import com.example.noteapp.ui.fragments.baseFragment.BaseFragment
import com.example.noteapp.ui.fragments.info.InfoDialogFragment
import com.example.noteapp.ui.fragments.password.RemovePasswordDialogFragment
import com.example.noteapp.viewmodels.NoteViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior

open class FragmentCastomizer : BaseFragment() {

    private lateinit var noteViewModel: NoteViewModel

    private var _binding: NoteCastomizerBinding? = null

    private val binding get() = _binding!!

    private lateinit var setSubtitleIndicator: SetSubtitleIndicator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        noteViewModel = ViewModelProvider(requireActivity())[NoteViewModel::class.java]

        _binding = NoteCastomizerBinding.inflate(layoutInflater, container, true)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMiscellaneous()
//        setFontSizeListener()
//        setFontColorListener()
        setInfoListener()
    }

    private fun initMiscellaneous() {
        val layoutMiscellaneous = binding
        val bottomSheetBehavior = BottomSheetBehavior
            .from(binding.noteCastomizerLl)

        layoutMiscellaneous.noteCastomizerTitleTv.setOnClickListener {
            if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            } else {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }

//        layoutMiscellaneous.imageColor1.setOnClickListener {
//            noteViewModel.selectedNoteColor = "#333333"
//            itemSelected1()
//            setSubtitleIndicator.onSubtitleClick()
//        }
//
//        layoutMiscellaneous.imageColor2.setOnClickListener {
//            noteViewModel.selectedNoteColor = "#FDBE3B"
//            itemSelected2()
//            setSubtitleIndicator.onSubtitleClick()
//        }
//
//        layoutMiscellaneous.imageColor3.setOnClickListener {
//            noteViewModel.selectedNoteColor = "#FF4842"
//            itemSelected3()
//            setSubtitleIndicator.onSubtitleClick()
//        }
//
//        layoutMiscellaneous.imageColor4.setOnClickListener {
//            noteViewModel.selectedNoteColor = "#3A52FC"
//            itemSelected4()
//            setSubtitleIndicator.onSubtitleClick()
//        }
//
//        layoutMiscellaneous.imageColor5.setOnClickListener {
//            noteViewModel.selectedNoteColor = "#000000"
//            itemSelected5()
//            setSubtitleIndicator.onSubtitleClick()
//        }
//
//        layoutMiscellaneous.imageColor6.setOnClickListener {
//            noteViewModel.selectedNoteColor = "#ff0266"
//            itemSelected6()
//            setSubtitleIndicator.onSubtitleClick()
//        }
    }

//    fun itemSelected1() {
//        binding.run {
//            imageColor1.setImageResource(R.drawable.ic_done)
//            imageColor2.setImageResource(0)
//            imageColor3.setImageResource(0)
//            imageColor6.setImageResource(0)
//            imageColor4.setImageResource(0)
//            imageColor5.setImageResource(0)
//        }
//
//    }
//
//    fun itemSelected2() {
//        binding.run {
//            imageColor1.setImageResource(0)
//            imageColor2.setImageResource(R.drawable.ic_done)
//            imageColor3.setImageResource(0)
//            imageColor6.setImageResource(0)
//            imageColor4.setImageResource(0)
//            imageColor5.setImageResource(0)
//        }
//    }
//
//    fun itemSelected3() {
//        binding.run {
//            imageColor1.setImageResource(0)
//            imageColor2.setImageResource(0)
//            imageColor3.setImageResource(R.drawable.ic_done)
//            imageColor6.setImageResource(0)
//            imageColor4.setImageResource(0)
//            imageColor5.setImageResource(0)
//        }
//    }
//
//    fun itemSelected6() {
//        binding.run {
//            imageColor1.setImageResource(0)
//            imageColor2.setImageResource(0)
//            imageColor3.setImageResource(0)
//            imageColor6.setImageResource(R.drawable.ic_done)
//            imageColor4.setImageResource(0)
//            imageColor5.setImageResource(0)
//        }
//    }
//
//    fun itemSelected4() {
//        binding.run {
//            imageColor1.setImageResource(0)
//            imageColor2.setImageResource(0)
//            imageColor3.setImageResource(0)
//            imageColor6.setImageResource(0)
//            imageColor4.setImageResource(R.drawable.ic_done)
//            imageColor5.setImageResource(0)
//        }
//    }
//
//    fun itemSelected5() {
//        binding.run {
//            imageColor1.setImageResource(0)
//            imageColor2.setImageResource(0)
//            imageColor3.setImageResource(0)
//            imageColor6.setImageResource(0)
//            imageColor4.setImageResource(0)
//            imageColor5.setImageResource(R.drawable.ic_done)
//        }
//    }
//
//    private fun setFontColor(colorPath: Int?) {
//        when (colorPath) {
//            1 -> {
//                binding.messAddEditFrag
//                    .setTextColor(Color.parseColor("#FFFFFF"))
//            }
//
//            2 -> {
//                binding.messAddEditFrag
//                    .setTextColor(Color.parseColor("#959595"))
//            }
//
//            3 -> {
//                binding.messAddEditFrag
//                    .setTextColor(Color.parseColor("#666666"))
//            }
//        }
//    }

//    private fun setFontSize(colorSize: Int?) {
//        when (colorSize) {
//            1 -> {
//                binding.messAddEditFrag
//                    .setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
//            }
//
//            2 -> {
//                binding.messAddEditFrag
//                    .setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
//            }
//
//            3 -> {
//                binding.messAddEditFrag
//                    .setTextSize(TypedValue.COMPLEX_UNIT_SP, 25F)
//            }
//
//            4 -> {
//                binding.messAddEditFrag
//                    .setTextSize(TypedValue.COMPLEX_UNIT_SP, 30F)
//            }
//
//            5 -> {
//                binding.messAddEditFrag
//                    .setTextSize(TypedValue.COMPLEX_UNIT_SP, 35F)
//            }
//        }
//    }

    private fun setInfoListener() {
        binding.noteCastomizerInfoFl.setOnClickListener {
            val fm = requireActivity().supportFragmentManager
            val infoDialogFrag = InfoDialogFragment()
            infoDialogFrag.show(fm, "hhh")
        }
    }

//    private fun setFontColorListener() {
//        binding.colorFontL.setOnClickListener {
//            ++noteViewModel.selectedFontNote
//            if (noteViewModel.selectedFontNote > 3) {
//                noteViewModel.selectedFontNote = 1
//            }
//            setFontColor(noteViewModel.selectedFontNote)
//            noteViewModel.selectedFontNote + 1
//
//            if (noteViewModel.selectedFontNote > 3) {
//                noteViewModel.selectedFontNote = 1
//            }
//        }
//    }

//    private fun setFontSizeListener() {
//        binding.fontSizeL.setOnClickListener {
//            ++noteViewModel.selectedFontSize
//            if (noteViewModel.selectedFontSize > 5) {
//                noteViewModel.selectedFontSize = 1
//            }
//
//            setFontSize(noteViewModel.selectedFontSize)
//            noteViewModel.selectedFontSize + 1
//
//            if (noteViewModel.selectedFontSize > 5) {
//                noteViewModel.selectedFontSize = 1
//            }
//        }
//    }

    private fun setPasswordListener() {
        binding.noteCastomizerLockFl.setOnClickListener {
            if (noteViewModel.hasPassword) {
                val fm = requireActivity().supportFragmentManager
                val dialogFrag = RemovePasswordDialogFragment()
                dialogFrag.show(fm, "Ab")
            } else {

            }
        }
    }
}
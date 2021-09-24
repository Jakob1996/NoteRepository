package com.example.noteapp.ui.fragments.note

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.noteapp.R
import com.example.noteapp.data.Note
import com.example.noteapp.databinding.FragmentAddNoteBinding
import com.example.noteapp.ui.fragments.baseFragment.BaseFragment
import com.example.noteapp.ui.fragments.password.RemovePasswordDialogFragment
import com.example.noteapp.viewmodels.ProfilViewModel
import com.example.noteapp.viewmodels.NoteViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.util.*

class AddNoteFragment : BaseFragment() {

    private var _binding: FragmentAddNoteBinding? = null

    private val binding get() = _binding!!

    private lateinit var noteViewModel: NoteViewModel

    private lateinit var profileViewModel: ProfilViewModel

    private var selectedImagePath: String = ""

    private var quit = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAddNoteBinding.inflate(inflater, container, false)

        noteViewModel = ViewModelProvider(requireActivity())[NoteViewModel::class.java]

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val title = binding.fragmentAddNoteTitleEt.text.toString()
                    val description = binding.fragmentAddNoteDescriptionEt.text.toString()

                    val date = Calendar.getInstance().timeInMillis
                    val color = noteViewModel.selectedNoteColor
                    val fontSize = noteViewModel.selectedFontSize
                    val fontColor = noteViewModel.selectedFontNote
                    val favourite = binding.fragmentAddNoteCastomizer.noteCastomizerFavouriteCb.isChecked

                    Log.d("sdfaf", "$favourite")

                    val hasPassword = noteViewModel.hasPassword
                    val password = noteViewModel.password

                    if (title.isNotEmpty() || description.isNotEmpty()) {
                        val note = Note(
                            title, description, date, isSelected = false, color,
                            fontColor, fontSize, favourite, hasPassword, password
                        ).apply {
                            rowId =
                                title.hashCode() + description.hashCode() + date.hashCode() * color.hashCode()
                        }

                        noteViewModel.insertNote(note)

                        if (fbAuth.currentUser != null) {
                            profileViewModel =
                                ViewModelProvider(requireActivity())[ProfilViewModel::class.java]
                            val noteAsList = listOf(note)
                            profileViewModel.addNotesToCloud(noteAsList)
                        }
                        noteViewModel.noteState = null
                    }

                    quit = 2
                    isEnabled = false
                    closeKeyboard()

                    requireActivity().onBackPressed()
                }
            })

        return binding.root
    }

    @SuppressLint("Range")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initMiscellaneous()

//        val gradientDrawable: GradientDrawable =
//            binding.fragmentAddNoteSubtitleIndicatorV.background as GradientDrawable

        binding.fragmentAddNoteCastomizer.noteCastomizerFontSizeFl.setOnClickListener {
            ++noteViewModel.selectedFontSize
            if (noteViewModel.selectedFontSize > 5) {
                noteViewModel.selectedFontSize = 1
            }
            setFontSize(noteViewModel.selectedFontSize)
            noteViewModel.selectedFontSize + 1

            if (noteViewModel.selectedFontSize > 5) {
                noteViewModel.selectedFontSize = 1
            }
        }

        binding.fragmentAddNoteCastomizer.noteCastomizerFontColorFl.setOnClickListener {
            ++noteViewModel.selectedFontNote

            if (noteViewModel.selectedFontNote > 3) {
                noteViewModel.selectedFontNote = 1
            }

            setFontColor(noteViewModel.selectedFontNote)
            noteViewModel.selectedFontNote + 1

            if (noteViewModel.selectedFontNote > 3) {
                noteViewModel.selectedFontNote = 1
            }
        }

        binding.fragmentAddNoteToolbarBackButtonIv.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.fragmentAddNoteCastomizer.noteCastomizerFavouriteFl.setOnClickListener {
            noteViewModel.isFavourite = !noteViewModel.isFavourite
        }

        binding.fragmentAddNoteCastomizer.noteCastomizerLockIv.setOnClickListener {
            if (noteViewModel.hasPassword) {
                val fm = requireActivity().supportFragmentManager
                val dialogFragment = RemovePasswordDialogFragment()
                dialogFragment.show(fm, "abcc")
            } else {
                findNavController().navigate(R.id.action_add_note_fragment_to_add_password_fragment)
            }
        }

        noteViewModel.getSelectedNote().observe(viewLifecycleOwner, androidx.lifecycle.Observer {

            if (noteViewModel.getSelectedNote().value == null) {
                noteViewModel.selectedNoteColor = "#333333"
//                gradientDrawable.setColor(Color.parseColor(noteViewModel.selectedNoteColor))

            } else {
                noteViewModel.noteTitle = it!!.title
                noteViewModel.noteMessage = it.message
                noteViewModel.noteDate = it.date
                noteViewModel.selectedNoteColor = it.color
                noteViewModel.selectedFontSize = it.fontSize
                noteViewModel.selectedFontNote = it.fontColor
                noteViewModel.idNote = it.rowId
                noteViewModel.hasPassword = it.hasPassword
                noteViewModel.password = it.password

                setImagePassword(noteViewModel.hasPassword)
                binding.fragmentAddNoteTitleEt.setText(noteViewModel.noteTitle)
                binding.fragmentAddNoteDescriptionEt.setText(noteViewModel.noteMessage)
                setFontColor(noteViewModel.selectedFontNote)
                setFontSize(noteViewModel.selectedFontSize)

                val selectedColor = noteViewModel.getSelectedNote().value?.color
//                gradientDrawable.setColor(Color.parseColor(selectedColor))
                noteViewModel.selectedNoteColor = noteViewModel.getSelectedNote().value!!.color

//                when (noteViewModel.getSelectedNote().value?.color) {
//                    "#333333" -> {
//                        itemSelected1()
//                    }
//
//                    "#FDBE3B" -> {
//                        itemSelected2()
//                    }
//
//                    "#FF4842" -> {
//                        itemSelected3()
//                    }
//
//                    "#ff0266" -> {
//                        itemSelected6()
//                    }
//
//                    "#3A52FC" -> {
//                        itemSelected4()
//                    }
//
//                    "#000000" -> {
//                        itemSelected5()
//                    }
//                }
            }
        })
    }

    private fun closeKeyboard() {
        val view = requireActivity().currentFocus
        if (view != null) {
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun setSubtitleIndicator() {
//        val gradientDrawable: GradientDrawable =
//            binding.fragmentAddNoteSubtitleIndicatorV.background as GradientDrawable
//        gradientDrawable.setColor(Color.parseColor(noteViewModel.selectedNoteColor))
    }

    private fun initMiscellaneous() {
        val layoutMiscellaneous = binding.fragmentAddNoteCastomizer
        val bottomSheetBehavior =
            BottomSheetBehavior.from(binding.fragmentAddNoteCastomizer.noteCastomizerLl)

        binding.fragmentAddNoteCastomizer.noteCastomizerTitleTv.setOnClickListener {
            if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            } else {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }

//        layoutMiscellaneous.imageColor1.setOnClickListener {
//            noteViewModel.selectedNoteColor = "#333333"
//            itemSelected1()
//            setSubtitleIndicator()
//        }
//
//        layoutMiscellaneous.imageColor2.setOnClickListener {
//            noteViewModel.selectedNoteColor = "#FDBE3B"
//            itemSelected2()
//            setSubtitleIndicator()
//        }
//
//        layoutMiscellaneous.imageColor3.setOnClickListener {
//            noteViewModel.selectedNoteColor = "#FF4842"
//            itemSelected3()
//            setSubtitleIndicator()
//        }
//
//        layoutMiscellaneous.imageColor4.setOnClickListener {
//            noteViewModel.selectedNoteColor = "#3A52FC"
//            itemSelected4()
//            setSubtitleIndicator()
//        }
//
//        layoutMiscellaneous.imageColor5.setOnClickListener {
//            noteViewModel.selectedNoteColor = "#000000"
//            itemSelected5()
//            setSubtitleIndicator()
//        }
//
//        layoutMiscellaneous.imageColor6.setOnClickListener {
//            noteViewModel.selectedNoteColor = "#ff0266"
//            itemSelected6()
//            setSubtitleIndicator()
//        }
    }
//
//    private fun itemSelected1() {
//        binding.includeMiscellaneous.imageColor1.setImageResource(R.drawable.ic_done)
//        binding.includeMiscellaneous.imageColor2.setImageResource(0)
//        binding.includeMiscellaneous.imageColor3.setImageResource(0)
//        binding.includeMiscellaneous.imageColor6.setImageResource(0)
//        binding.includeMiscellaneous.imageColor4.setImageResource(0)
//        binding.includeMiscellaneous.imageColor5.setImageResource(0)
//    }
//
//    private fun itemSelected2() {
//        binding.includeMiscellaneous.imageColor1.setImageResource(0)
//        binding.includeMiscellaneous.imageColor2.setImageResource(R.drawable.ic_done)
//        binding.includeMiscellaneous.imageColor3.setImageResource(0)
//        binding.includeMiscellaneous.imageColor6.setImageResource(0)
//        binding.includeMiscellaneous.imageColor4.setImageResource(0)
//        binding.includeMiscellaneous.imageColor5.setImageResource(0)
//    }
//
//    private fun itemSelected3() {
//        binding.includeMiscellaneous.imageColor1.setImageResource(0)
//        binding.includeMiscellaneous.imageColor2.setImageResource(0)
//        binding.includeMiscellaneous.imageColor3.setImageResource(R.drawable.ic_done)
//        binding.includeMiscellaneous.imageColor6.setImageResource(0)
//        binding.includeMiscellaneous.imageColor4.setImageResource(0)
//        binding.includeMiscellaneous.imageColor5.setImageResource(0)
//    }
//
//    private fun itemSelected6() {
//        binding.includeMiscellaneous.imageColor1.setImageResource(0)
//        binding.includeMiscellaneous.imageColor2.setImageResource(0)
//        binding.includeMiscellaneous.imageColor3.setImageResource(0)
//        binding.includeMiscellaneous.imageColor6.setImageResource(R.drawable.ic_done)
//        binding.includeMiscellaneous.imageColor4.setImageResource(0)
//        binding.includeMiscellaneous.imageColor5.setImageResource(0)
//    }
//
//    private fun itemSelected4() {
//        binding.includeMiscellaneous.imageColor1.setImageResource(0)
//        binding.includeMiscellaneous.imageColor2.setImageResource(0)
//        binding.includeMiscellaneous.imageColor3.setImageResource(0)
//        binding.includeMiscellaneous.imageColor6.setImageResource(0)
//        binding.includeMiscellaneous.imageColor4.setImageResource(R.drawable.ic_done)
//        binding.includeMiscellaneous.imageColor5.setImageResource(0)
//    }
//
//    private fun itemSelected5() {
//        binding.includeMiscellaneous.imageColor1.setImageResource(0)
//        binding.includeMiscellaneous.imageColor2.setImageResource(0)
//        binding.includeMiscellaneous.imageColor3.setImageResource(0)
//        binding.includeMiscellaneous.imageColor6.setImageResource(0)
//        binding.includeMiscellaneous.imageColor4.setImageResource(0)
//        binding.includeMiscellaneous.imageColor5.setImageResource(R.drawable.ic_done)
//    }

    /*
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            FishBun.FISHBUN_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK){
                    imageList = data?.getParcelableArrayListExtra(FishBun.INTENT_PATH)!!

                    imageList.forEach {
                        viewModel.pathImage.add(it.toString())
                    }
                    initAdapter(viewModel.pathImage)
                }
            }
        }
    }
     */

    private fun setFontColor(colorPath: Int?) {
        when (colorPath) {
            1 -> {
                binding.fragmentAddNoteDescriptionEt.setTextColor(Color.parseColor("#FFFFFF"))
            }

            2 -> {
                binding.fragmentAddNoteDescriptionEt.setTextColor(Color.parseColor("#959595"))
            }

            3 -> {
                binding.fragmentAddNoteDescriptionEt.setTextColor(Color.parseColor("#666666"))
            }
        }
    }

    private fun setFontSize(colorSize: Int?) {
        binding.fragmentAddNoteDescriptionEt.run {
            val complexUnitSp = TypedValue.COMPLEX_UNIT_SP
            when (colorSize) {
                1 -> {
                    setTextSize(complexUnitSp, 15F)
                }

                2 -> {
                    setTextSize(complexUnitSp, 20F)
                }

                3 -> {
                    setTextSize(complexUnitSp, 25F)
                }

                4 -> {
                    setTextSize(complexUnitSp, 30F)
                }

                5 -> {
                    setTextSize(complexUnitSp, 35F)
                }
            }
        }
    }

    override fun onDestroyView() {
        if (quit == 2) {
            setDefaultState()
        }

        saveNote()

        _binding = null
        super.onDestroyView()
    }

    private fun setDefaultState() {
        selectedImagePath = ""
        binding.fragmentAddNoteTitleEt.setText("")
        binding.fragmentAddNoteDescriptionEt.setText("")
        noteViewModel.setDefaultNoteState()
    }

    private fun saveNote() {
        val title = binding.fragmentAddNoteTitleEt.text
        val message = binding.fragmentAddNoteDescriptionEt.text

        noteViewModel.run {
            val fontSize = selectedFontSize
            val fontColor = selectedFontNote
            val date = noteDate
            val color = selectedNoteColor
            val id = idNote
            val isFavourite = isFavourite
            val hasPassword = hasPassword
            val password = password

            val note = Note(
                title.toString(),
                message.toString(),
                date,
                false,
                color,
                fontColor,
                fontSize,
                isFavourite,
                hasPassword,
                password
            )

            note.rowId = id
            noteViewModel.setSelectedNote(note)
        }
    }

    private fun setImagePassword(hasPassword: Boolean) {
        binding.fragmentAddNoteCastomizer.noteCastomizerLockIv.run {
            if (hasPassword) {
                setColorFilter(Color.parseColor("#FF4343"))
                setImageResource(R.drawable.ic_lock_24)
            } else {
                setColorFilter(Color.WHITE)
                setImageResource(R.drawable.ic_baseline_lock)
            }
        }
    }

    /*
    private fun openImagePicker() {
        val count = 10 - viewModel.pathImage.size
        FishBun.with(this).setImageAdapter(GlideAdapter())
            .setMinCount(1)
            .setMaxCount(count)
            .setAllViewTitle("All")
            .setActionBarTitle("Selected Images")
            .setIsUseDetailView(false)
            .startAlbum()
    }
     */
}
package com.example.noteapp.ui.fragments.note

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.noteapp.R
import com.example.noteapp.data.Note
import com.example.noteapp.databinding.FragmentBeforeEditNoteBinding
import com.example.noteapp.navigation.Navigation
import com.example.noteapp.tools.DoubleClickListener
import com.example.noteapp.viewmodels.NoteViewModel
import com.example.noteapp.viewmodels.ProfilViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.auth.FirebaseAuth
import com.xeoh.android.texthighlighter.TextHighlighter
import java.util.*

class BeforeEditNoteFragment : Fragment(), Navigation {

    private lateinit var noteViewModel: NoteViewModel
    private lateinit var profileViewModel: ProfilViewModel

    private var value: Boolean = false

    private var selectedImagePath: String = ""

    private var quit = 1

    private val textHighLighter = TextHighlighter()

    private var _binding: FragmentBeforeEditNoteBinding? = null

    private val binding get() = _binding!!

    private val fbAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        closeKeyboard()

        _binding = FragmentBeforeEditNoteBinding.inflate(layoutInflater, container, false)

        noteViewModel = ViewModelProvider(requireActivity())[NoteViewModel::class.java]

        profileViewModel = ViewModelProvider(requireActivity())[ProfilViewModel::class.java]

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(false) {
                override fun handleOnBackPressed() {
                    if (noteViewModel.getSearchMode().value == true && binding.messAddEditFrag.text.isNotEmpty()) {
                        textHighLighter
                            .setForegroundColor(binding.messAddEditFrag.currentTextColor)
                            .setBackgroundColor(Color.TRANSPARENT)
                            .invalidate(TextHighlighter.BASE_MATCHER)
                        noteViewModel.setSearchMode(false)

                    } else {
                        val title = binding.titleAddEditFrag.text.toString()
                        val message = binding.messAddEditFrag.text.toString()
                        val date = Calendar.getInstance().timeInMillis
                        val color = noteViewModel.selectedNoteColor
                        val fontSize = noteViewModel.selectedFontSize
                        val fontColor = noteViewModel.selectedFontNote
                        val favourite = noteViewModel.isFavourite
                        val hasPassword = noteViewModel.hasPassword
                        val password = noteViewModel.password

                        val noteBefore = noteViewModel.noteBeforeChange
                        val titleBefore = noteViewModel.titleBefore
                        val messBefore = noteViewModel.messageBefore

                        if (titleBefore != title
                            || messBefore != message
                            || noteBefore?.color != color
                            || fontSize != noteBefore.fontSize
                            || fontColor != noteBefore.fontColor
                            || noteBefore.title.length != title.length
                            || noteBefore.message.length != message.length
                            || noteBefore.isFavourite != favourite
                            || noteBefore.hasPassword != hasPassword
                        ) {

                            val note = Note(
                                title, message, date, false, color,
                                fontColor, fontSize, favourite, hasPassword, password
                            ).apply {
                                rowId = noteViewModel.getSelectedNote().value!!.rowId
                            }

                            if (fbAuth.currentUser != null) {
                                profileViewModel.updateNoteInCloud(note)
                            }
                            noteViewModel.updateNote(note)
                            noteViewModel.noteState = null
                        }
                        quit = 2
                        isEnabled = false
                        closeKeyboard()
                    }
                    requireActivity().supportFragmentManager.popBackStack()
                    if (noteViewModel.isSearchEdit == 1) {
                        //requireActivity().overridePendingTransition(R.anim.zero_to_zero, R.anim.from_left_to_right)
                        //requireActivity().onBackPressed()
                        backTransaction()
                    } else {
                        //findNavController().navigate(R.id.action_beforeAddEditNoteFragment_to_searchFragment2)
                    }
                }
            }
        )

        return binding.root
    }

    @SuppressLint("Range")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("ahc", "onViewCreated")
        super.onViewCreated(view, savedInstanceState)

        val gradientDrawable: GradientDrawable =
            binding.viewSubtitleIndicator.background as GradientDrawable

        Log.d("checkn", "${noteViewModel.noteBeforeChange?.message}")
        initMiscellaneous()

        binding.includeMiscellaneousBeforeAddEdit.searchInMessageNote.setOnClickListener {
            val fm = requireActivity().supportFragmentManager
            val dialogSearchFrag = SearchInNoteDialogFragment()
            dialogSearchFrag.show(fm, "ab")
        }

        binding.includeMiscellaneousBeforeAddEdit.layoutPasswordImage.setOnClickListener {
            if (noteViewModel.hasPassword) {
                val fm = requireActivity().supportFragmentManager
                val dialogFrag = RemovePasswordDialogFragment()
                dialogFrag.show(fm, "Ab")
            } else {
                val sm = requireActivity().supportFragmentManager
                sm.beginTransaction().add(R.id.container_keeper, AddPasswordNoteFragment())
                    .addToBackStack("bF").commit()
            }
        }

        binding.editNote.setOnClickListener {
            val ft = requireActivity().supportFragmentManager.beginTransaction()
                .add(
                    R.id.container_keeper, EditNoteFragment()
                ).addToBackStack("BEN").commit()

        }

        binding.beforeLayout.setOnClickListener(
            DoubleClickListener(
                callback = object : DoubleClickListener.Callback {
                    override fun doubleClicked() {
                        navigateToFragment(
                            EditNoteFragment(),
                            "BEN",
                            requireActivity().supportFragmentManager
                        )
                    }
                }
            )
        )

        binding.messAddEditFrag.setOnClickListener(
            DoubleClickListener(
                callback = object : DoubleClickListener.Callback {
                    override fun doubleClicked() {
                        navigateToFragment(
                            EditNoteFragment(),
                            "BEN",
                            requireActivity().supportFragmentManager
                        )
                    }
                }
            )
        )



        binding.includeMiscellaneousBeforeAddEdit.fontSizeL.setOnClickListener {
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

        binding.includeMiscellaneousBeforeAddEdit.colorFontL.setOnClickListener(object :
            View.OnClickListener {
            override fun onClick(v: View?) {
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
        })

        binding.includeMiscellaneousBeforeAddEdit.layoutInfo.setOnClickListener {
            val fm = requireActivity().supportFragmentManager
            val infoDialogFrag = InfoDialogFragment()
            infoDialogFrag.show(fm, "hhh")
        }

        binding.backFromAddEdit.setOnClickListener { backTransaction() }

        binding.includeMiscellaneousBeforeAddEdit.favouriteNoteButton.setOnClickListener {
            noteViewModel.isFavourite = !noteViewModel.isFavourite
        }

        noteViewModel.getSearchMode().observe(viewLifecycleOwner, Observer {
            if (noteViewModel.getSearchMode().value == true) {
                textHighLighter
                    .setBackgroundColor(Color.parseColor("#FFFF00"))
                    .addTarget(binding.messAddEditFrag)
                    .highlight(noteViewModel.searchInNote, TextHighlighter.BASE_MATCHER)
            }
        })

        noteViewModel.getSelectedNote().observe(viewLifecycleOwner, Observer {

            if (noteViewModel.getSelectedNote().value == null) {
                noteViewModel.selectedNoteColor = "#333333"
                gradientDrawable.setColor(Color.parseColor(noteViewModel.selectedNoteColor))
            } else {
                noteViewModel.noteTitle = it!!.title
                noteViewModel.noteMessage = it.message
                noteViewModel.noteDate = it.date
                noteViewModel.selectedNoteColor = it.color
                noteViewModel.selectedFontSize = it.fontSize
                noteViewModel.selectedFontNote = it.fontColor
                noteViewModel.idNote = it.rowId
                noteViewModel.isFavourite = it.isFavourite
                noteViewModel.hasPassword = it.hasPassword
                noteViewModel.password = it.password

                setImagePassword(noteViewModel.hasPassword)

                binding.titleAddEditFrag.text = noteViewModel.noteTitle
                binding.messAddEditFrag.text = noteViewModel.noteMessage
                setFontColor(noteViewModel.selectedFontNote)
                setFontSize(noteViewModel.selectedFontSize)

                val selectedNoteColor = noteViewModel.getSelectedNote().value?.color
                gradientDrawable.setColor(Color.parseColor(selectedNoteColor))
                noteViewModel.selectedNoteColor = noteViewModel.getSelectedNote().value!!.color

                when (noteViewModel.getSelectedNote().value?.color) {
                    "#333333" -> {
                        itemSelected1()
                    }
                    "#FDBE3B" -> {
                        itemSelected2()
                    }
                    "#FF4842" -> {
                        itemSelected3()
                    }
                    "#ff0266" -> {
                        itemSelected6()
                    }
                    "#3A52FC" -> {
                        itemSelected4()
                    }
                    "#000000" -> {
                        itemSelected5()
                    }
                }
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
        val gradientDrawable: GradientDrawable =
            binding.viewSubtitleIndicator.background as GradientDrawable
        gradientDrawable.setColor(Color.parseColor(noteViewModel.selectedNoteColor))
    }

    private fun initMiscellaneous() {
        val layoutMiscellaneous = binding.includeMiscellaneousBeforeAddEdit
        val bottomSheetBehavior = BottomSheetBehavior
            .from(binding.includeMiscellaneousBeforeAddEdit.layoutMiscellaneous)

        layoutMiscellaneous.textMiscellaneous.setOnClickListener {
            if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            } else {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }

        layoutMiscellaneous.imageColor1.setOnClickListener {
            noteViewModel.selectedNoteColor = "#333333"
            itemSelected1()
            setSubtitleIndicator()
        }

        layoutMiscellaneous.imageColor2.setOnClickListener {
            noteViewModel.selectedNoteColor = "#FDBE3B"
            itemSelected2()
            setSubtitleIndicator()
        }

        layoutMiscellaneous.imageColor3.setOnClickListener {
            noteViewModel.selectedNoteColor = "#FF4842"
            itemSelected3()
            setSubtitleIndicator()
        }

        layoutMiscellaneous.imageColor4.setOnClickListener {
            noteViewModel.selectedNoteColor = "#3A52FC"
            itemSelected4()
            setSubtitleIndicator()
        }

        layoutMiscellaneous.imageColor5.setOnClickListener {
            noteViewModel.selectedNoteColor = "#000000"
            itemSelected5()
            setSubtitleIndicator()
        }

        layoutMiscellaneous.imageColor6.setOnClickListener {
            noteViewModel.selectedNoteColor = "#ff0266"
            itemSelected6()
            setSubtitleIndicator()
        }
    }

    /*
    private fun openImagePicker() {
        val count = 10 - viewModel.pathImage.size
        FishBun.with(this).setImageAdapter(GlideAdapter())
                .setMinCount(0)
                .setMaxCount(count)
                .setAllViewTitle("All")
                .setActionBarTitle("Selected Images")
                .setIsUseDetailView(false)
                .startAlbum()
    }

     */

    private fun itemSelected1() {
        binding.includeMiscellaneousBeforeAddEdit.imageColor1.setImageResource(R.drawable.ic_done)
        binding.includeMiscellaneousBeforeAddEdit.imageColor2.setImageResource(0)
        binding.includeMiscellaneousBeforeAddEdit.imageColor3.setImageResource(0)
        binding.includeMiscellaneousBeforeAddEdit.imageColor6.setImageResource(0)
        binding.includeMiscellaneousBeforeAddEdit.imageColor4.setImageResource(0)
        binding.includeMiscellaneousBeforeAddEdit.imageColor5.setImageResource(0)
    }

    private fun itemSelected2() {
        binding.includeMiscellaneousBeforeAddEdit.imageColor1.setImageResource(0)
        binding.includeMiscellaneousBeforeAddEdit.imageColor2.setImageResource(R.drawable.ic_done)
        binding.includeMiscellaneousBeforeAddEdit.imageColor3.setImageResource(0)
        binding.includeMiscellaneousBeforeAddEdit.imageColor6.setImageResource(0)
        binding.includeMiscellaneousBeforeAddEdit.imageColor4.setImageResource(0)
        binding.includeMiscellaneousBeforeAddEdit.imageColor5.setImageResource(0)
    }

    private fun itemSelected3() {
        binding.includeMiscellaneousBeforeAddEdit.imageColor1.setImageResource(0)
        binding.includeMiscellaneousBeforeAddEdit.imageColor2.setImageResource(0)
        binding.includeMiscellaneousBeforeAddEdit.imageColor3.setImageResource(R.drawable.ic_done)
        binding.includeMiscellaneousBeforeAddEdit.imageColor6.setImageResource(0)
        binding.includeMiscellaneousBeforeAddEdit.imageColor4.setImageResource(0)
        binding.includeMiscellaneousBeforeAddEdit.imageColor5.setImageResource(0)
    }

    private fun itemSelected6() {
        binding.includeMiscellaneousBeforeAddEdit.imageColor1.setImageResource(0)
        binding.includeMiscellaneousBeforeAddEdit.imageColor2.setImageResource(0)
        binding.includeMiscellaneousBeforeAddEdit.imageColor3.setImageResource(0)
        binding.includeMiscellaneousBeforeAddEdit.imageColor6.setImageResource(R.drawable.ic_done)
        binding.includeMiscellaneousBeforeAddEdit.imageColor4.setImageResource(0)
        binding.includeMiscellaneousBeforeAddEdit.imageColor5.setImageResource(0)
    }

    private fun itemSelected4() {
        binding.includeMiscellaneousBeforeAddEdit.imageColor1.setImageResource(0)
        binding.includeMiscellaneousBeforeAddEdit.imageColor2.setImageResource(0)
        binding.includeMiscellaneousBeforeAddEdit.imageColor3.setImageResource(0)
        binding.includeMiscellaneousBeforeAddEdit.imageColor6.setImageResource(0)
        binding.includeMiscellaneousBeforeAddEdit.imageColor4.setImageResource(R.drawable.ic_done)
        binding.includeMiscellaneousBeforeAddEdit.imageColor5.setImageResource(0)
    }

    private fun itemSelected5() {
        binding.includeMiscellaneousBeforeAddEdit.imageColor1.setImageResource(0)
        binding.includeMiscellaneousBeforeAddEdit.imageColor2.setImageResource(0)
        binding.includeMiscellaneousBeforeAddEdit.imageColor3.setImageResource(0)
        binding.includeMiscellaneousBeforeAddEdit.imageColor6.setImageResource(0)
        binding.includeMiscellaneousBeforeAddEdit.imageColor4.setImageResource(0)
        binding.includeMiscellaneousBeforeAddEdit.imageColor5.setImageResource(R.drawable.ic_done)
    }

    /*
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            FishBun.FISHBUN_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK){
                    imageList = data?.getParcelableArrayListExtra(FishBun.INTENT_PATH)

                    imageList?.forEach {
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
                binding.messAddEditFrag
                    .setTextColor(Color.parseColor("#FFFFFF"))
            }

            2 -> {
                binding.messAddEditFrag
                    .setTextColor(Color.parseColor("#959595"))
            }

            3 -> {
                binding.messAddEditFrag
                    .setTextColor(Color.parseColor("#666666"))
            }
        }
    }

    private fun setFontSize(colorSize: Int?) {
        when (colorSize) {
            1 -> {
                binding.messAddEditFrag
                    .setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
            }

            2 -> {
                binding.messAddEditFrag
                    .setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
            }

            3 -> {
                binding.messAddEditFrag
                    .setTextSize(TypedValue.COMPLEX_UNIT_SP, 25F)
            }

            4 -> {
                binding.messAddEditFrag
                    .setTextSize(TypedValue.COMPLEX_UNIT_SP, 30F)
            }

            5 -> {
                binding.messAddEditFrag
                    .setTextSize(TypedValue.COMPLEX_UNIT_SP, 35F)
            }
        }
    }

    override fun onDestroyView() {

        val title = binding.titleAddEditFrag.text
        val message = binding.messAddEditFrag.text
        val fontSize = noteViewModel.selectedFontSize
        val fontColor = noteViewModel.selectedFontNote
        val date = noteViewModel.noteDate
        val color = noteViewModel.selectedNoteColor
        val id = noteViewModel.idNote
        val isFavourite = noteViewModel.isFavourite
        val hasPassword = noteViewModel.hasPassword
        val password = noteViewModel.password

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

        if (quit == 2) {
            setOffAddEdit()
        }

        _binding = null

        super.onDestroyView()
    }

    private fun setOffAddEdit() {
        selectedImagePath = ""
        binding.titleAddEditFrag.text = ""
        binding.messAddEditFrag.text = ""
        noteViewModel.setSelectedNote(null)
        noteViewModel.noteBeforeChange = null
        noteViewModel.noteTitle = ""
        noteViewModel.noteMessage = ""
        noteViewModel.noteDate = 1
        noteViewModel.pathImage = arrayListOf()
        noteViewModel.selectedFontSize = 3
        noteViewModel.selectedFontNote = 1
        noteViewModel.selectedNoteColor = "#333333"
        noteViewModel.idNote = -1
        noteViewModel.isFavourite = false
        noteViewModel.hasPassword = false
        noteViewModel.password = 0
        noteViewModel.setSearchMode(false)
        noteViewModel.isSearchEdit = 1
    }

    private fun setImagePassword(hasPassword: Boolean) {
        if (hasPassword) {
            binding.includeMiscellaneousBeforeAddEdit.imageViewPassword.setColorFilter(
                Color.parseColor(
                    "#FF4343"
                )
            )
            binding.includeMiscellaneousBeforeAddEdit.imageViewPassword.setImageResource(R.drawable.ic_lock_24)
        } else {
            binding.includeMiscellaneousBeforeAddEdit.imageViewPassword.setColorFilter(Color.WHITE)
            binding.includeMiscellaneousBeforeAddEdit.imageViewPassword.setImageResource(R.drawable.ic_baseline_lock)
        }
    }

    private fun backTransaction() {
        val sm = requireActivity().supportFragmentManager
        val trans = sm.beginTransaction()
        //trans.remove(sm.findFragmentByTag("fragCheck")!!)
        sm.popBackStackImmediate("noteF", FragmentManager.POP_BACK_STACK_INCLUSIVE)
        trans.commit()
    }
}
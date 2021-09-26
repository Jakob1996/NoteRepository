package com.example.noteapp.ui.fragments.note

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.noteapp.R
import com.example.noteapp.data.Note
import com.example.noteapp.databinding.FragmentGeneralNoteBinding
import com.example.noteapp.tools.DoubleClickListener
import com.example.noteapp.ui.fragments.baseFragment.BaseFragment
import com.example.noteapp.ui.fragments.info.InfoDialogFragment
import com.example.noteapp.ui.fragments.password.RemovePasswordDialogFragment
import com.example.noteapp.ui.fragments.search.SearchDialogFragment
import com.example.noteapp.viewmodels.NoteViewModel
import com.example.noteapp.viewmodels.ProfilViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.xeoh.android.texthighlighter.TextHighlighter
import java.util.*

class GeneralNoteFragment : BaseFragment() {

    private lateinit var noteViewModel: NoteViewModel

    private lateinit var profileViewModel: ProfilViewModel

    private var value: Boolean = false

    private var quit = 1

    private val textHighLighter = TextHighlighter()

    private var _binding: FragmentGeneralNoteBinding? = null

    private val binding get() = _binding!!

    private fun setOnBackPressedDispatcher() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    saveNoteState()
                    isEnabled = false
                    requireActivity().onBackPressed()
                }
            })
    }

    private fun saveNoteState() {
        noteViewModel.run {
            if (getSearchMode().value == true && binding.fragmentGeneralNoteDescriptionTv.text.isNotEmpty()) {
                textHighLighter
                    .setForegroundColor(binding.fragmentGeneralNoteDescriptionTv.currentTextColor)
                    .setBackgroundColor(Color.TRANSPARENT)
                    .invalidate(TextHighlighter.BASE_MATCHER)
                setSearchMode(false)
            } else {
                val title = binding.fragmentGeneralNoteTitleTv.text.toString()
                val message = binding.fragmentGeneralNoteDescriptionTv.text.toString()
                val date = Calendar.getInstance().timeInMillis
                val color = selectedNoteColor
                val fontSize = selectedFontSize
                val fontColor = selectedFontNote
                val favourite =
                    binding.fragmentGeneralNoteCastomizer.noteCastomizerFavouriteCb.isChecked
                val hasPassword = hasPassword
                val password = password

                val noteBefore = noteBeforeChange
                val titleBefore = titleBefore
                val messBefore = messageBefore

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
                    val rowId = getSelectedNote().value?.rowId
                    val note = Note(
                        title, message, date, false, color,
                        fontColor, fontSize, favourite, hasPassword, password
                    )
                    note.let {
                        if (rowId != null) {
                            note.rowId = rowId
                        }
                    }

                    saveNoteInFirebase(note)
                    updateNote(note)
                    noteState = null
                }

                quit = 2
                closeKeyboard()
            }
            setDefaultNoteState()
        }
    }

    private fun saveNoteInFirebase(note: Note) {
        if (fbAuth.currentUser != null) {
            profileViewModel.updateNoteInCloud(note)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        closeKeyboard()
        setViewModels()

        _binding = FragmentGeneralNoteBinding.inflate(inflater, container, false)

        return binding.root
    }

    private fun setViewModels() {
        noteViewModel = ViewModelProvider(requireActivity())[NoteViewModel::class.java]
        profileViewModel = ViewModelProvider(requireActivity())[ProfilViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupView() {
        initCastomizer()
        setSearchDescriptionListener()
        setChangePasswordListener()
        setEditNoteListener()
        setDoubleClickListener()
        setChangeFontSizeListener()
        setChangeFontColorListener()
        setInfoListener()
        setOnFavouriteListener()
        setSearchModeObserver()
        setSelectedNoteObserver()
        setOnBackPressedListener()
        setOnBackPressedDispatcher()
    }

    private fun setOnBackPressedListener() {
        binding.fragmentGeneralNoteToolbarBackIv.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        noteViewModel.view2 = _binding

        _binding = null
    }

    private fun setInfoListener() {
        binding.fragmentGeneralNoteCastomizer.noteCastomizerInfoFl.setOnClickListener {
            val fm = requireActivity().supportFragmentManager
            val infoDialogFrag = InfoDialogFragment()
            infoDialogFrag.show(fm, "hhh")
        }
    }

    private fun setChangeFontColorListener() {
        binding.fragmentGeneralNoteCastomizer.noteCastomizerFontColorFl.setOnClickListener {
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
    }

    private fun setChangeFontSizeListener() {
        binding.fragmentGeneralNoteCastomizer.noteCastomizerFontSizeFl.setOnClickListener {
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
    }

    private fun setDoubleClickListener() {
        binding.fragmentGeneralNoteDescriptionTv.setOnClickListener {
            DoubleClickListener(callback = object : DoubleClickListener.Callback {
                override fun doubleClicked() {
                    navigateToFragment(
                        findNavController(), R.id.editNoteFragment
                    )
                }
            })
        }

        binding.fragmentGeneralNoteCrl.setOnClickListener(
            DoubleClickListener(
                callback = object : DoubleClickListener.Callback {
                    override fun doubleClicked() {
                        navigateToFragment(
                            findNavController(), R.id.editNoteFragment
                        )
                    }
                }
            )
        )
    }

    private fun setEditNoteListener() {
        binding.fragmentGeneralNoteToolbarEditIv.setOnClickListener {
            navigateToFragment(findNavController(), R.id.action_to_edit_note_fragment)
        }
    }

    private fun setChangePasswordListener() {
        binding.fragmentGeneralNoteCastomizer.noteCastomizerLockIv.setOnClickListener {
            if (noteViewModel.hasPassword) {
                val fm = requireActivity().supportFragmentManager
                val dialogFrag = RemovePasswordDialogFragment()
                dialogFrag.show(fm, "Ab")
            } else {
                navigateToFragment(
                    findNavController(),
                    R.id.action_general_note_fragment_to_add_password_fragment
                )
            }
        }
    }

    private fun setSearchDescriptionListener() {
        binding.fragmentGeneralNoteCastomizer.noteCastomizerSearchFl.setOnClickListener {
            val fm = requireActivity().supportFragmentManager
            val dialogSearchFrag = SearchDialogFragment()
            dialogSearchFrag.show(fm, "ab")
        }
    }

    private fun setOnFavouriteListener() {
        binding.fragmentGeneralNoteCastomizer.noteCastomizerFavouriteFl.setOnClickListener {
            noteViewModel.isFavourite = !noteViewModel.isFavourite
        }
    }

    private fun setSearchModeObserver() {
        noteViewModel.getSearchMode().observe(viewLifecycleOwner, {
            if (noteViewModel.getSearchMode().value == true) {
                textHighLighter
                    .setBackgroundColor(Color.parseColor("#FFFF00"))
                    .addTarget(binding.fragmentGeneralNoteDescriptionTv)
                    .highlight(noteViewModel.searchInNote, TextHighlighter.BASE_MATCHER)
            }
        })
    }

    private fun setSelectedNoteObserver() {
        noteViewModel.getSelectedNote().observe(viewLifecycleOwner, {

            if (noteViewModel.getSelectedNote().value == null) {
                noteViewModel.selectedNoteColor = "#333333"
            } else {
                noteViewModel.run {
                    noteTitle = it!!.title
                    noteMessage = it.message
                    noteDate = it.date
                    selectedNoteColor = it.color
                    selectedFontSize = it.fontSize
                    selectedFontNote = it.fontColor
                    idNote = it.rowId
                    isFavourite = it.isFavourite
                    hasPassword = it.hasPassword
                    password = it.password
                }


                setImagePassword(noteViewModel.hasPassword)
                binding.fragmentGeneralNoteCastomizer.noteCastomizerFavouriteCb.isChecked =
                    noteViewModel.isFavourite

                binding.fragmentGeneralNoteTitleTv.text = noteViewModel.noteTitle
                binding.fragmentGeneralNoteDescriptionTv.text = noteViewModel.noteMessage
                setFontColor(noteViewModel.selectedFontNote)
                setFontSize(noteViewModel.selectedFontSize)

                noteViewModel.selectedNoteColor = noteViewModel.getSelectedNote().value!!.color
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

    private fun initCastomizer() {
        val layoutMiscellaneous = binding.fragmentGeneralNoteCastomizer
        val bottomSheetBehavior = BottomSheetBehavior
            .from(binding.fragmentGeneralNoteCastomizer.noteCastomizerLl)

        layoutMiscellaneous.noteCastomizerTitleTv.setOnClickListener {
            if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            } else {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
    }


    private fun setFontColor(colorPath: Int?) {
        when (colorPath) {
            1 -> {
                binding.fragmentGeneralNoteDescriptionTv
                    .setTextColor(Color.parseColor("#FFFFFF"))
            }

            2 -> {
                binding.fragmentGeneralNoteDescriptionTv
                    .setTextColor(Color.parseColor("#959595"))
            }

            3 -> {
                binding.fragmentGeneralNoteDescriptionTv
                    .setTextColor(Color.parseColor("#666666"))
            }
        }
    }

    private fun setFontSize(colorSize: Int?) {
        when (colorSize) {
            1 -> {
                binding.fragmentGeneralNoteDescriptionTv
                    .setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
            }

            2 -> {
                binding.fragmentGeneralNoteDescriptionTv
                    .setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
            }

            3 -> {
                binding.fragmentGeneralNoteDescriptionTv
                    .setTextSize(TypedValue.COMPLEX_UNIT_SP, 25F)
            }

            4 -> {
                binding.fragmentGeneralNoteDescriptionTv
                    .setTextSize(TypedValue.COMPLEX_UNIT_SP, 30F)
            }

            5 -> {
                binding.fragmentGeneralNoteDescriptionTv
                    .setTextSize(TypedValue.COMPLEX_UNIT_SP, 35F)
            }
        }
    }

    private fun setImagePassword(hasPassword: Boolean) {
        if (hasPassword) {
            binding.fragmentGeneralNoteCastomizer.noteCastomizerLockIv.setColorFilter(
                Color.parseColor(
                    "#FF4343"
                )
            )
            binding.fragmentGeneralNoteCastomizer.noteCastomizerLockIv.setImageResource(R.drawable.ic_lock_24)
        } else {
            binding.fragmentGeneralNoteCastomizer.noteCastomizerLockIv.run {
                setColorFilter(Color.WHITE)
                setImageResource(R.drawable.ic_baseline_lock)
            }
        }
    }
}
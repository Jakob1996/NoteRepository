package com.example.noteapp.ui.fragments.note

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.noteapp.R
import com.example.noteapp.data.Note
import com.example.noteapp.viewmodels.ViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.xeoh.android.texthighlighter.TextHighlighter
import kotlinx.android.synthetic.main.fragment_before_edit_note.*
import kotlinx.android.synthetic.main.note_edit_layout_miscellaneous.*
import java.util.*

class BeforeEditNoteFragment:Fragment() {

    private lateinit var viewModel: ViewModel

    private var value: Boolean = false

    private var selectedImagePath: String = ""

    private var quit = 1

    private val textHighLighter = TextHighlighter()

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(requireActivity())[ViewModel::class.java]
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(
                this,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        if (viewModel.getSearchMode().value == true&&mess_addEditFrag.text.isNotEmpty()) {
                            textHighLighter
                                    .setForegroundColor(mess_addEditFrag.currentTextColor)
                                    .setBackgroundColor(Color.TRANSPARENT)
                                    .invalidate(TextHighlighter.BASE_MATCHER)
                            viewModel.setSearchMode(false)
                        } else {
                            val title = title_addEditFrag.text.toString()
                            val message = mess_addEditFrag.text.toString()
                            val date = Calendar.getInstance().timeInMillis
                            val color = viewModel.selectedNoteColor
                            val fontSize = viewModel.selectedFontSize
                            val fontColor = viewModel.selectedFontNote
                            val favourite = viewModel.isFavourite
                            val hasPassword = viewModel.hasPassword
                            val password = viewModel.password

                            val noteBefore = viewModel.noteBeforeChange

                            if (noteBefore!!.title != title || noteBefore.message != message || noteBefore.color != color
                                    || fontSize != noteBefore.fontSize || fontColor != noteBefore.fontColor
                                    || noteBefore.title.length != title.length || noteBefore.message.length != message.length || noteBefore.isFavourite != favourite || noteBefore.hasPassword != hasPassword) {
                                val note = Note(
                                        title,
                                        message,
                                        date,
                                        false,
                                        color,
                                        fontColor,
                                        fontSize,
                                        favourite,
                                        hasPassword,
                                        password
                                ).apply {
                                    rowId = viewModel.getSelectedNote().value!!.rowId
                                }
                                viewModel.updateNote(note)
                                viewModel.noteState = null
                            }
                            quit = 2
                            isEnabled = false
                            closeKeyboard()


                            if(viewModel.isSearchEdit==1){
                                findNavController().navigate(R.id.action_beforeAddEditNoteFragment_to_mainFramgent)
                            } else{
                                findNavController().navigate(R.id.action_beforeAddEditNoteFragment_to_searchFragment2)
                            }
                        }
                    }
                }
        )

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_before_edit_note, container, false)
    }

    @SuppressLint("Range")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gradientDrawable: GradientDrawable = viewSubtitleIndicator.background as GradientDrawable

        initMiscellaneous()


        searchInMessageNote.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                val fm = requireActivity().supportFragmentManager
                val dialogSearchFrag = SearchInNoteDialogFragment()
                dialogSearchFrag.show(fm, "ab")
            }
        })

        layoutPasswordImage.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                if(viewModel.hasPassword){
                    val fm = requireActivity().supportFragmentManager
                    val dialogFrag = RemovePasswordDialogFragment()
                    dialogFrag.show(fm, "Ab")
                } else{
                    findNavController().navigate(R.id.action_beforeAddEditNoteFragment_to_passwordNoteFragment)
                }
            }
        })

        editNote.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                findNavController().navigate(R.id.action_before_AddEditNoteFragment_to_editNoteFragment)
            }
        })

        fontSizeL.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                ++viewModel.selectedFontSize
                if (viewModel.selectedFontSize > 5) {
                    viewModel.selectedFontSize = 1
                }

                setFontSize(viewModel.selectedFontSize)
                viewModel.selectedFontSize + 1

                if (viewModel.selectedFontSize > 5) {
                    viewModel.selectedFontSize = 1
                }
            }
        })

        colorFontL.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                ++viewModel.selectedFontNote
                if (viewModel.selectedFontNote > 3) {
                    viewModel.selectedFontNote = 1
                }
                setFontColor(viewModel.selectedFontNote)
                viewModel.selectedFontNote + 1

                if (viewModel.selectedFontNote > 3) {
                    viewModel.selectedFontNote = 1
                }
            }
        })

        layoutInfo.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                val fm = requireActivity().supportFragmentManager
                val infoDialogFrag = InfoDialogFragment()
                infoDialogFrag.show(fm, "hhh")
            }
        })

        back_from_addEdit.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                requireActivity().onBackPressed()
            }
        })

        favouriteNoteButton.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                if(viewModel.isFavourite){
                    viewModel.isFavourite = false
                    setFavourite(viewModel.isFavourite)
                } else{
                    viewModel.isFavourite = true
                    setFavourite(viewModel.isFavourite)
                }
            }
        })

        viewModel.getSearchMode().observe(viewLifecycleOwner, Observer{
            if(viewModel.getSearchMode().value==true){
                textHighLighter
                    .setBackgroundColor(Color.parseColor("#FFFF00"))
                    .addTarget(mess_addEditFrag)
                    .highlight(viewModel.searchInNote, TextHighlighter.BASE_MATCHER)
            }
        })

        viewModel.getSelectedNote().observe(viewLifecycleOwner, Observer {

            if(viewModel.getSelectedNote().value==null) {
                viewModel.selectedNoteColor = "#333333"
                gradientDrawable.setColor(Color.parseColor(viewModel.selectedNoteColor))
            } else{
                viewModel.noteTitle = it!!.title
                viewModel.noteMessage = it.message
                viewModel.noteDate = it.date
                viewModel.selectedNoteColor = it.color
                viewModel.selectedFontSize = it.fontSize
                viewModel.selectedFontNote = it.fontColor
                viewModel.idNote = it.rowId
                viewModel.isFavourite = it.isFavourite
                viewModel.hasPassword = it.hasPassword
                viewModel.password = it.password

                setImagePassword(viewModel.hasPassword)

                setFavourite(viewModel.isFavourite)
                title_addEditFrag.setText(viewModel.noteTitle)
                mess_addEditFrag.setText(viewModel.noteMessage)
                setFontColor(viewModel.selectedFontNote)
                setFontSize(viewModel.selectedFontSize)

                val selectedNoteColor = viewModel.getSelectedNote().value?.color
                gradientDrawable.setColor(Color.parseColor(selectedNoteColor))
                viewModel.selectedNoteColor = viewModel.getSelectedNote().value!!.color

                when (viewModel.getSelectedNote().value?.color) {
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

    fun closeKeyboard() {
        val view = requireActivity().currentFocus
        if (view != null) {
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun setSubtitleIndicator() {
        val gradientDrawable: GradientDrawable = viewSubtitleIndicator.background as GradientDrawable
        gradientDrawable.setColor(Color.parseColor(viewModel.selectedNoteColor))
    }

    fun initMiscellaneous() {
        val layoutMiscellaneous = requireActivity().findViewById<LinearLayout>(R.id.layoutMiscellaneous)
        val bottomSheetBehavior = BottomSheetBehavior.from(requireActivity().findViewById(R.id.layoutMiscellaneous))

        layoutMiscellaneous.findViewById<TextView>(R.id.textMiscellaneous).setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                } else {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            }
        })

        layoutMiscellaneous.findViewById<ImageView>(R.id.imageColor1).setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                viewModel.selectedNoteColor = "#333333"
                itemSelected1()
                setSubtitleIndicator()
            }
        })

        layoutMiscellaneous.findViewById<ImageView>(R.id.imageColor2).setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                viewModel.selectedNoteColor = "#FDBE3B"
                itemSelected2()
                setSubtitleIndicator()
            }
        })

        layoutMiscellaneous.findViewById<ImageView>(R.id.imageColor3).setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                viewModel.selectedNoteColor = "#FF4842"
                itemSelected3()
                setSubtitleIndicator()
            }
        })

        layoutMiscellaneous.findViewById<ImageView>(R.id.imageColor4).setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                viewModel.selectedNoteColor = "#3A52FC"
                itemSelected4()
                setSubtitleIndicator()
            }
        })

        layoutMiscellaneous.findViewById<ImageView>(R.id.imageColor5).setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                viewModel.selectedNoteColor = "#000000"
                itemSelected5()
                setSubtitleIndicator()
            }
        })

        layoutMiscellaneous.findViewById<ImageView>(R.id.imageColor6).setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                viewModel.selectedNoteColor = "#ff0266"
                itemSelected6()
                setSubtitleIndicator()
            }
        })
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

    fun itemSelected1() {
        imageColor1.setImageResource(R.drawable.ic_done)
        imageColor2.setImageResource(0)
        imageColor3.setImageResource(0)
        imageColor6.setImageResource(0)
        imageColor4.setImageResource(0)
        imageColor5.setImageResource(0)
    }

    fun itemSelected2() {
        imageColor1.setImageResource(0)
        imageColor2.setImageResource(R.drawable.ic_done)
        imageColor3.setImageResource(0)
        imageColor6.setImageResource(0)
        imageColor4.setImageResource(0)
        imageColor5.setImageResource(0)
    }

    fun itemSelected3() {
        imageColor1.setImageResource(0)
        imageColor2.setImageResource(0)
        imageColor3.setImageResource(R.drawable.ic_done)
        imageColor6.setImageResource(0)
        imageColor4.setImageResource(0)
        imageColor5.setImageResource(0)
    }

    fun itemSelected6() {
        imageColor1.setImageResource(0)
        imageColor2.setImageResource(0)
        imageColor3.setImageResource(0)
        imageColor6.setImageResource(R.drawable.ic_done)
        imageColor4.setImageResource(0)
        imageColor5.setImageResource(0)
    }

    fun itemSelected4() {
        imageColor1.setImageResource(0)
        imageColor2.setImageResource(0)
        imageColor3.setImageResource(0)
        imageColor6.setImageResource(0)
        imageColor4.setImageResource(R.drawable.ic_done)
        imageColor5.setImageResource(0)
    }

    fun itemSelected5() {
        imageColor1.setImageResource(0)
        imageColor2.setImageResource(0)
        imageColor3.setImageResource(0)
        imageColor6.setImageResource(0)
        imageColor4.setImageResource(0)
        imageColor5.setImageResource(R.drawable.ic_done)
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

    fun setFontColor(colorPath: Int?) {
        when (colorPath) {
            1 -> {
                mess_addEditFrag.setTextColor(Color.parseColor("#FFFFFF"))
            }

            2 -> {
                mess_addEditFrag.setTextColor(Color.parseColor("#959595"))
            }

            3 -> {
                mess_addEditFrag.setTextColor(Color.parseColor("#666666"))
            }
        }
    }

    fun setFontSize(colorSize: Int?) {
        when (colorSize) {
            1 -> {
                mess_addEditFrag.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
            }

            2 -> {
                mess_addEditFrag.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
            }

            3 -> {
                mess_addEditFrag.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25F)
            }

            4 -> {
                mess_addEditFrag.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30F)
            }

            5 -> {
                mess_addEditFrag.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35F)
            }
        }
    }

    override fun onDestroyView() {

        val title = title_addEditFrag.text
        val message = mess_addEditFrag.text
        val fontSize = viewModel.selectedFontSize
        val fontColor = viewModel.selectedFontNote
        val date = viewModel.noteDate
        val color = viewModel.selectedNoteColor
        val id = viewModel.idNote
        val isFavourite = viewModel.isFavourite
        val hasPassword = viewModel.hasPassword
        val password = viewModel.password

        val note = Note(title.toString(), message.toString(), date, false, color, fontColor, fontSize, isFavourite, hasPassword, password)
        note.rowId = id

        viewModel.setSelectedNote(note)

        if(quit==2){
            setOffAddEdit()
        }

        super.onDestroyView()
    }

    fun setOffAddEdit(){
        selectedImagePath = ""
        title_addEditFrag.setText("")
        mess_addEditFrag.setText("")
        viewModel.setSelectedNote(null)
        viewModel.noteBeforeChange = null
        viewModel.noteTitle = ""
        viewModel.noteMessage=""
        viewModel.noteDate = 1
        viewModel.pathImage = arrayListOf()
        viewModel.selectedFontSize = 3
        viewModel.selectedFontNote = 1
        viewModel.selectedNoteColor = "#333333"
        viewModel.idNote = -1
        viewModel.isFavourite = false
        viewModel.hasPassword = false
        viewModel.password = 0
        viewModel.setSearchMode(false)
        viewModel.isSearchEdit = 1
    }

    fun setFavourite(boolean: Boolean){
        if(boolean){
            favouriteImage.setColorFilter(Color.parseColor("#FDBE3B"))
        } else{
            favouriteImage.setColorFilter(Color.WHITE)
        }
    }

    private fun setImagePassword(hasPassword: Boolean) {
        if(hasPassword){
            imageViewPassword.setColorFilter(Color.parseColor("#FF4343"))
            imageViewPassword.setImageResource(R.drawable.ic_lock_24)
        } else{
            imageViewPassword.setColorFilter(Color.WHITE)
            imageViewPassword.setImageResource(R.drawable.ic_baseline_lock)
        }
    }
}

package com.example.noter.presentation.view

import android.os.Bundle
import android.text.Spannable
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.example.noter.R
import com.example.noter.databinding.NoteFragmentBinding
import com.example.noter.domain.model.Note
import com.example.noter.presentation.view.dialogs.AlertDialogType
import com.example.noter.presentation.view.dialogs.CustomAlertDialog
import com.example.noter.presentation.viewmodel.NoteViewModel
import com.example.noter.utils.*
import org.koin.androidx.viewmodel.ext.android.viewModel

const val CLICKED_NOTE_ARGS_KEY = "clickedNote"
const val IS_NEW_NOTE_ARGS_KEY = "isNewNote"
const val FOLDER_NAME_ARGS_KEY = "folderName"

class NoteFragment: Fragment(R.layout.note_fragment) {

    private val noteViewModel: NoteViewModel by viewModel()
    private lateinit var binding: NoteFragmentBinding
    private var isNewNote = true
    private val textFormatter = TextFormatter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = NoteFragmentBinding.bind(view)
        isNewNote = getIsNewNoteFromArgs()
        setupData()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun resolveDateText(): String =
        if (isNewNote) Calendar().getCurrentDate(resources) else noteViewModel.getNoteDateCreated()


    private fun setupData() {
        getNoteFromArgs()
        setFolderName()
        fetchNoteData()
        setSpansFromContainers(noteViewModel.getNoteSpans())

        binding.tvNoteDate.text = resolveDateText()
        binding.backButton.setOnClickListener { onBack() }
        binding.toolbar.setOnMenuItemClickListener(toolbarMenuItemClickListener())
        binding.noteMenuParent.children.forEach { it.setOnClickListener(noteMenuClickListener()) }
    }

    private fun setSpansFromContainers(containers: List<SpanContainer>?) {
        binding.etNoteContent.apply {
            this.setText(
                textFormatter.setSpansFromContainers(containers, (this.text as Spannable)),
                TextView.BufferType.SPANNABLE
            )
        }
    }

    private fun saveSpans(): List<SpanContainer> {
        val spannableText = binding.etNoteContent.text as Spannable
        return textFormatter.grabAllSpans(spannableText)
    }

    private fun toolbarMenuItemClickListener() : Toolbar.OnMenuItemClickListener {
        return Toolbar.OnMenuItemClickListener { item ->
            when(item?.itemId) {
                R.id.action_share_note -> shareNote(noteContent = binding.etNoteContent.text.toString())
                // I should make it as bottomSheetDialog instead of Alert Dialog
                R.id.action_delete_note -> showDeleteAlertDialog()
            }
            true
        }
    }

    private fun noteMenuClickListener(): View.OnClickListener {
        return View.OnClickListener {
            val styleSpan = textFormatter.setTypeFromId(it.id)
            launchTextFormatter(styleSpan = styleSpan)
        }
    }

    private fun launchTextFormatter(styleSpan: TextFormatterType) {

        val text = binding.etNoteContent.text as Spannable
        val selectionStart = binding.etNoteContent.selectionStart
        val selectionEnd = binding.etNoteContent.selectionEnd

        binding.etNoteContent.setText(
            textFormatter.formatText(
            text = text,
            selectionStart = selectionStart,
            selectionEnd = selectionEnd,
            type = styleSpan), TextView.BufferType.SPANNABLE)
    }

    private fun showDeleteAlertDialog() {
        CustomAlertDialog(
            context = requireContext(),
            positiveButtonClickHandler = ::positiveButtonClickHandler,
            dialogType = AlertDialogType.DELETE_NOTE)
            .showDialog()
    }

    private fun shareNote(noteContent: String) =
        activity?.startActivity(ShareNote(noteContent).constructIntent())

    override fun onStop() {
        noteViewModel.resolveNoteOnStop(
            isNewNote = isNewNote,
            content = binding.etNoteContent.text.toString(),
            dateCreated = Calendar().getCurrentDate(resources),
            spanContainers = saveSpans()
        )
        super.onStop()
    }

    private fun fetchNoteData() {
        if (!isNewNote) {
            val clickedNote = noteViewModel.getClickedNoteAndDate()
            binding.tvNoteDate.text = clickedNote.dateCreated
            binding.etNoteContent.setText(clickedNote.content)
        }
    }

    private fun getIsNewNoteFromArgs(): Boolean =
        arguments?.getBoolean(IS_NEW_NOTE_ARGS_KEY) ?: false


    // TODO(handle an exception in getNoteFromArgs() on configuration change)
    private fun getNoteFromArgs() {
        if (!isNewNote) {
            val clickedNote = arguments?.getSerializable(CLICKED_NOTE_ARGS_KEY) as Note
            noteViewModel.setNote(note = clickedNote)
        }
    }

    private fun setFolderName() {
        val noteFolder = arguments?.getString(FOLDER_NAME_ARGS_KEY)
        noteFolder?.let { noteViewModel.setFolderName(name = it) }
    }

    private fun onBack(): View.OnClickListener =
        View.OnClickListener { parentFragmentManager.popBackStack() }

    private fun positiveButtonClickHandler() {
        if (!isNewNote) {
            noteViewModel.deleteOldNote()
        } else binding.etNoteContent.text.clear()

        parentFragmentManager.popBackStack()
    }
}
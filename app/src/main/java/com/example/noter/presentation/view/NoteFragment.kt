package com.example.noter.presentation.view

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.SpannedString
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.example.noter.R
import com.example.noter.databinding.NoteFragmentBinding
import com.example.noter.domain.model.Note
import com.example.noter.presentation.view.dialogs.DeleteBottomSheetDialog
import com.example.noter.presentation.viewmodel.NoteViewModel
import com.example.noter.utils.*
import org.koin.androidx.viewmodel.ext.android.viewModel

const val CLICKED_NOTE_ARGS_KEY = "clickedNote"
const val IS_NEW_NOTE_ARGS_KEY = "isNewNote"
const val SHARE_INTENT_TYPE = "text/plain"


class NoteFragment: Fragment(R.layout.note_fragment) {

    private val noteViewModel: NoteViewModel by viewModel()
    private val textFormatter = TextFormatter()
    private var noteDateCreated = ""
    private lateinit var binding: NoteFragmentBinding
    private var isNewNote = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = NoteFragmentBinding.bind(view)
        isNewNote = arguments?.getBoolean(IS_NEW_NOTE_ARGS_KEY) ?: true
        noteDateCreated = Calendar().getCurrentDate(resources)

        if (!isNewNote) {
            val clickedNote = arguments?.getSerializable(CLICKED_NOTE_ARGS_KEY) as Note
            noteViewModel.setNote(note = clickedNote)
            noteDateCreated = clickedNote.dateCreated
            binding.tvNoteDate.text = noteDateCreated

            val spannedText = textFormatter.setSpansFromContainers(
                containers = noteViewModel.getNoteSpans(),
                text = clickedNote.content
            )
            binding.etNoteContent.setText(spannedText, TextView.BufferType.SPANNABLE)
        } else binding.tvNoteDate.text = noteDateCreated

        arguments?.getString(FOLDER_NAME_ARGS_KEY)?.let {
            noteViewModel.setFolderName(name = it)
        }

        binding.backButton.setOnClickListener { parentFragmentManager.popBackStack() }
        binding.toolbar.setOnMenuItemClickListener { toolbarMenuItemClickListener(it); true }
        for (child in binding.noteMenuParent.children) {
            child.setOnClickListener { launchTextFormatter(textFormatter.setTypeFromId(it.id)) }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun toolbarMenuItemClickListener(item: MenuItem?) {
        when(item?.itemId) {
            R.id.action_share_note -> shareNote(binding.etNoteContent.text.toString())
            // I should make it as bottomSheetDialog instead of Alert Dialog
            R.id.action_delete_note -> {
                val dialog = DeleteBottomSheetDialog(::positiveButtonClickHandler)
                val args = Bundle()
                args.putString(DELETE_DIALOG_TYPE_ARGS_KEY, DELETE_NOTE)
                dialog.arguments = args
                dialog.show(parentFragmentManager, null)
            }
        }
    }

    private fun launchTextFormatter(styleSpan: TextFormatterType) {
        if (binding.etNoteContent.hasSelection()) {
            //todo i shouldn't pass all note content, just selected text
            val text = binding.etNoteContent.text as Spannable
            val formattedText = textFormatter.formatText(
                text, styleSpan, binding.etNoteContent.selectionStart, binding.etNoteContent.selectionEnd)
            binding.etNoteContent.setText(formattedText, TextView.BufferType.SPANNABLE)
        }
    }

    private fun shareNote(noteContent: String)  {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, noteContent)
            type = SHARE_INTENT_TYPE
        }
        Intent.createChooser(intent, null)
        startActivity(intent)
    }

    private fun positiveButtonClickHandler() {
        if (!isNewNote) noteViewModel.deleteOldNote() else binding.etNoteContent.text.clear()
        parentFragmentManager.popBackStack()
    }

    override fun onStop() {
        val noteContent = binding.etNoteContent.text.toString()
        val spannableText = binding.etNoteContent.text as Spannable
        noteViewModel.resolveNoteOnStop(isNewNote, noteContent,
            noteDateCreated, textFormatter.grabAllSpans(spannableText))
        super.onStop()
    }
}
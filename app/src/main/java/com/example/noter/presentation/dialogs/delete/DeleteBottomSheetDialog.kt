package com.example.noter.presentation.dialogs.delete

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.example.noter.R
import com.example.noter.databinding.DeleteBottomsheetDialogBinding
import com.example.noter.presentation.folder_notes.DELETE_REQUEST_KEY
import com.example.noter.presentation.folder_notes.SHOULD_DELETE_FOLDER
import com.example.noter.utils.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DeleteBottomSheetDialog: BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.delete_bottomsheet_dialog, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = DeleteBottomsheetDialogBinding.bind(view)
        val tvDeleteResId = when (arguments?.getString(DELETE_DIALOG_TYPE_ARGS_KEY)) {
            DELETE_NOTE -> R.string.delete_note_title
            DELETE_FOLDER -> R.string.delete_folder_title
            DELETE_SELECTED_NOTES -> R.string.delete_selected_notes_title
            else -> R.string.delete_note_title
        }
        binding.tvDeleteTitle.setText(tvDeleteResId)
        binding.btnDelete.setOnClickListener {
            setFragmentResult(DELETE_REQUEST_KEY, bundleOf(SHOULD_DELETE_FOLDER to true))
            dialog?.dismiss()
        }
        binding.btnCancel.setOnClickListener { dialog?.dismiss() }

        super.onViewCreated(view, savedInstanceState)
    }
}
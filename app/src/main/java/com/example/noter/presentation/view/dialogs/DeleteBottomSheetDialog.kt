package com.example.noter.presentation.view.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.noter.R
import com.example.noter.databinding.DeleteBottomsheetDialogBinding
import com.example.noter.utils.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlin.reflect.KFunction
import kotlin.reflect.KFunction0

class DeleteBottomSheetDialog(
    private val onPositiveButtonClicked: KFunction0<Unit>
): BottomSheetDialogFragment() {

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
        binding.btnDelete.setOnClickListener { onPositiveButtonClicked(); dialog?.dismiss() }
        binding.btnCancel.setOnClickListener { dialog?.dismiss() }

        super.onViewCreated(view, savedInstanceState)
    }

}
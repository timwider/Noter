package com.example.noter.presentation.view.dialogs

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import com.example.noter.R
import com.example.noter.adapters.NotesAdapter
import kotlin.reflect.KFunction0

class CustomAlertDialog(
    private val context: Context,
    private val positiveButtonClickHandler: KFunction0<Unit>,
    private val dialogType: AlertDialogType
    ) : BaseCustomAlertDialog  {

    override fun setupDialog(): AlertDialog.Builder {

        return AlertDialog.Builder(context)
            .setTitle(resolveResId().first)
            .setMessage(resolveResId().second)
            .setPositiveButton(R.string.delete_note_positive_button) { p1, p2 ->
                positiveButtonClickHandler
            }
            .setNegativeButton(R.string.delete_note_negative_button) { dialog, _ -> dialog.dismiss() }

    }

    override fun showDialog() {
        setupDialog().show()
    }

    override fun resolveResId(): Pair<Int, Int> {
        return when (dialogType) {
            AlertDialogType.DELETE_NOTE -> {
                Pair(R.string.delete_note_title, R.string.delete_note_description)
            }

            AlertDialogType.DELETE_FOLDER -> {
                Pair(R.string.delete_folder_title, R.string.delete_folder_description)
            }

            AlertDialogType.DELETE_SELECTED_NOTES -> {
                Pair(R.string.delete_selected_notes, R.string.delete_selected_notes_description)
            }
        }
    }

}
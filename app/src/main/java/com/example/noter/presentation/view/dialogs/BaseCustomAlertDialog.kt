package com.example.noter.presentation.view.dialogs

import androidx.appcompat.app.AlertDialog

interface BaseCustomAlertDialog {
    fun setupDialog() : AlertDialog.Builder

    fun showDialog()

    fun resolveResId() : Pair<Int, Int>

}

enum class AlertDialogType {
    DELETE_NOTE,
    DELETE_FOLDER,
    DELETE_SELECTED_NOTES
}

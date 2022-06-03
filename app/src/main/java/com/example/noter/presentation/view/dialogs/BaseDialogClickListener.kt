package com.example.noter.presentation.view.dialogs

import android.content.DialogInterface

interface BaseDialogClickListener {

    fun onPositiveButtonClick(dialog: DialogInterface)

    fun onNegativeButtonClick(dialog: DialogInterface)
}
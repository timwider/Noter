package com.example.noter.presentation.view.dialogs

import android.view.View
import com.google.android.material.snackbar.Snackbar

class DialogManager {

    fun makeSnackbar(view: View, text: String) = Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show()
}
package com.example.noter.utils

import android.content.Intent

const val SHARE_INTENT_TYPE = "text/plain"

class IntentHelper {

    fun createShareNoteIntent(noteContent: String): Intent {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, noteContent)
            type = SHARE_INTENT_TYPE
        }
        Intent.createChooser(intent, null)
        return intent
    }
}
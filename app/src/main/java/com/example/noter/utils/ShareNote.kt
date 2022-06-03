package com.example.noter.utils

import android.content.Intent


const val SHARE_NOTE_TYPE = "text/plain"

class ShareNote(
    private val noteContent: String
) : BaseShareNote {
    override fun constructIntent() : Intent {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, noteContent)
            type = SHARE_NOTE_TYPE
        }
        return Intent.createChooser(intent, null)
    }
}
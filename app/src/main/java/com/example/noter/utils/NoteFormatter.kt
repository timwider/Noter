package com.example.noter.utils

const val MAX_NOTE_LENGTH = 39
const val FIRST_LINE_INDEX = 0

class NoteFormatter(
    private val note: String
) : BaseNoteFormatter {

    override fun extractName(): String {

        val firstLine = note.split("\n")[FIRST_LINE_INDEX]
        var noteName = ""
        var charCount = 0

        for (i in firstLine) {
            charCount++
            if (charCount <= MAX_NOTE_LENGTH) {
                noteName += i
            }
        }

        return if (charCount >= MAX_NOTE_LENGTH) {
            "$noteName..."
        } else noteName
    }
}


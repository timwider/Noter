package com.example.noter.utils

import androidx.lifecycle.MutableLiveData
import com.example.noter.domain.model.Note

private const val MAX_NOTE_LENGTH = 39

fun <T> MutableLiveData<T>.notifyObservers() {
    value = value
}

fun <T> MutableLiveData<MutableList<T>?>.addAndNotify(element: T) {
    if (value != null) {
        value?.add(element)
        notifyObservers()
    } else value = mutableListOf(element)
}

fun <T> MutableLiveData<MutableList<T>?>.removeAndNotify(element: T) {
    value?.remove(element)
    notifyObservers()
}

fun NoteRV.extractName(): String {
    val firstLine = this.content.split("\n")[0]
    return if (firstLine.length > MAX_NOTE_LENGTH) {
        firstLine.substring(0, MAX_NOTE_LENGTH) + "..."
    } else firstLine
}

// TODO change note type in FolderNotesAdapter. (I can use NotesAdapter instead of it)
fun Note.extractName(): String {
    val firstLine = this.content.split("\n")[0]
    return if (firstLine.length > MAX_NOTE_LENGTH) {
        firstLine.substring(0, MAX_NOTE_LENGTH) + "..."
    } else firstLine
}


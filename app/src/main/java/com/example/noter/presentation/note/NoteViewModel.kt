package com.example.noter.presentation.note

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noter.domain.model.Note
import com.example.noter.domain.usecase.notes.DeleteNoteUseCase
import com.example.noter.domain.usecase.notes.SaveNoteUseCase
import com.example.noter.domain.usecase.notes.UpdateNoteUseCase
import com.example.noter.utils.SpanContainer
import com.example.noter.utils.emptyString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val ID_NOT_SET = 0

class NoteViewModel(
    private val saveNoteUseCase: SaveNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase
): ViewModel() {
    private val clickedNote = MutableLiveData<Note>()
    private val folderName = MutableLiveData(emptyString())
    private val noteState = MutableLiveData(NoteState.NOT_SET)
    private var isNewNote = true
    private var noteDateCreated = emptyString()

    fun isNewNote() = isNewNote

    fun setIsNewNote(value: Boolean) { isNewNote = value }

    fun getNoteDateCreated() = noteDateCreated

    fun setNoteDateCreated(value: String) { noteDateCreated = value }

    private fun updateExistingNote(content: String, spanContainers: List<SpanContainer>) {
        val noteToUpdate = Note(
            id = clickedNote.value!!.id,
            dateCreated = clickedNote.value!!.dateCreated,
            content = content,
            folderName = clickedNote.value!!.folderName,
            spanContainers = spanContainers
        )
        viewModelScope.launch(Dispatchers.IO) {
            updateNoteUseCase.execute(noteToUpdate = noteToUpdate)
        }
        noteState.value = NoteState.UPDATED
    }

    fun getNote() = clickedNote.value!!

    private fun saveNewNote(content: String, dateCreated: String, spanContainers: List<SpanContainer>) {
        val newNote = Note(
            id = ID_NOT_SET,
            dateCreated = dateCreated,
            content = content,
            folderName = folderName.value!!,
            spanContainers = spanContainers)

        viewModelScope.launch(Dispatchers.IO) {
            saveNoteUseCase.execute(note = newNote)
        }
    }

    // We don't actually delete it, we just update it with empty contents
    fun deleteOldNote() {
        viewModelScope.launch(Dispatchers.IO) {
            deleteNoteUseCase.execute(note = clickedNote.value!!)
        }
    }

    fun setNote(note: Note) {
        clickedNote.value = note
        noteDateCreated = note.dateCreated
    }

    fun resolveNoteOnStop(content: String, spanContainers: List<SpanContainer>) {
        when {
            isNewNote && content.isNotBlank() -> saveNewNote(content, noteDateCreated, spanContainers)
            !isNewNote && content.isNotBlank() -> updateExistingNote(content, spanContainers)
            !isNewNote && content.isBlank() -> deleteOldNote()
        }
    }

    fun setFolderName(name: String) { folderName.value = name }

    fun  getNoteSpans(): List<SpanContainer>? = clickedNote.value?.spanContainers
}

enum class NoteState{
    NOT_SET,
    UPDATED
}
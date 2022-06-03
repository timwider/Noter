package com.example.noter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noter.domain.model.Note
import com.example.noter.domain.usecase.notes.DeleteNoteUseCase
import com.example.noter.domain.usecase.notes.SaveNoteUseCase
import com.example.noter.domain.usecase.notes.UpdateNoteUseCase
import com.example.noter.utils.SpanContainer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(
    private val saveNoteUseCase: SaveNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
): ViewModel() {

    private val _clickedNote = MutableLiveData<Note>()
    val clickedNote: LiveData<Note> = _clickedNote

    val isNoteUpdated = MutableLiveData(false)
    val noteToUpdateLiveData = MutableLiveData<Note>()

    private val folderName = MutableLiveData("")

    val noteState = MutableLiveData(NoteState.NOT_SET)

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

        noteToUpdateLiveData.value = noteToUpdate
        noteState.value = NoteState.UPDATED
    }

    fun getClickedNoteAndDate() : Note {
        return clickedNote.value!!
    }

    fun getNoteDateCreated() : String = clickedNote.value!!.dateCreated

    private fun saveNewNote(content: String, dateCreated: String, spanContainers: List<SpanContainer>) {
        val newNote = Note(id = 0,
            dateCreated = dateCreated,
            content = content, folderName = folderName.value!!,
            spanContainers = spanContainers)

        viewModelScope.launch(Dispatchers.IO) {
            saveNoteUseCase.execute(note = newNote)
        }
    }

    fun deleteOldNote() {
        viewModelScope.launch(Dispatchers.IO) {
            deleteNoteUseCase.execute(note = _clickedNote.value!!)
        }
    }

    fun setNote(note: Note) {
        _clickedNote.value = note
    }

    // I can
    fun resolveNoteOnStop(isNewNote: Boolean, content: String, dateCreated: String, spanContainers: List<SpanContainer>) {
        when {
            isNewNote && content.isNotEmpty() -> saveNewNote(content, dateCreated, spanContainers)
            !isNewNote && content.isNotEmpty() -> updateExistingNote(content, spanContainers)
            !isNewNote && content.isEmpty() -> deleteOldNote()
        }
    }

    fun setFolderName(name: String) {
        folderName.value = name
    }

    fun getNoteSpans(): List<SpanContainer>? = _clickedNote.value?.spanContainers

}

enum class NoteState{
    NOT_SET,
    UPDATED
}
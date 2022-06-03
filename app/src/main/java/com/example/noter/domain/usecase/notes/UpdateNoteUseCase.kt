package com.example.noter.domain.usecase.notes

import com.example.noter.domain.NotesRepository
import com.example.noter.domain.model.Note

class UpdateNoteUseCase(
    private val repository: NotesRepository
) {

    suspend fun execute(noteToUpdate: Note) {
        repository.updateNote(note = noteToUpdate)
    }
}
package com.example.noter.domain.usecase.notes

import com.example.noter.domain.NotesRepository
import com.example.noter.domain.model.Note

class DeleteNoteUseCase(
    private val repository: NotesRepository
){

    suspend fun execute(note: Note) {
        repository.deleteNote(note = note)
    }
}
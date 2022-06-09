package com.example.noter.domain.usecase.notes

import com.example.noter.domain.NotesRepository

class DeleteNotesByIdUseCase(
    private val repository: NotesRepository
) {
    suspend fun execute(ids: List<Int>) {
        repository.deleteNotesById(ids)
    }
}
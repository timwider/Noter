package com.example.noter.domain.usecase.notes

import com.example.noter.domain.NotesRepository

class DeleteEmptyNotesUseCase(
    private val repository: NotesRepository
){
    suspend fun execute() {
        repository.clearEmptyNotes()
    }
}
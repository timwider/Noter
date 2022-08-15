package com.example.noter.domain.usecase.notes

import com.example.noter.domain.NotesRepository
import com.example.noter.domain.model.Note

class GetNotesUseCase(
    private val repository: NotesRepository
) {

    suspend fun getAllNotes(): List<Note> {
        return repository.getNotes()
    }

    suspend fun getNotesNoFolder(): List<Note> {
        return repository.getNotesNoFolder()
    }

    suspend fun getFolderNotes(folderName: String): List<Note> {
        return repository.getFolderNotes(folderName)
    }
}
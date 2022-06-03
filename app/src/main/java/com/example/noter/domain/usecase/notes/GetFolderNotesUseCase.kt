package com.example.noter.domain.usecase.notes

import com.example.noter.domain.NotesRepository
import com.example.noter.domain.model.Note

class GetFolderNotesUseCase(
    private val repository: NotesRepository
) {
    suspend fun execute(folderName: String): List<Note> {
        return repository.getFolderNotes(folderName = folderName)
    }
}
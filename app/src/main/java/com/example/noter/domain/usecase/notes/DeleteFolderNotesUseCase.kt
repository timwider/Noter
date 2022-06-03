package com.example.noter.domain.usecase.notes

import com.example.noter.domain.NotesRepository

class DeleteFolderNotesUseCase(
    private val repository: NotesRepository
) {
    suspend fun execute(folderName: String) {
        repository.deleteFolderNotes(folderName = folderName)
    }
}
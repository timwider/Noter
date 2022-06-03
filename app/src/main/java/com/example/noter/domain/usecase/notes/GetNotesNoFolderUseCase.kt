package com.example.noter.domain.usecase.notes

import com.example.noter.domain.NotesRepository
import com.example.noter.domain.model.Note

class GetNotesNoFolderUseCase(
    private val repository: NotesRepository
) {

    suspend fun execute() : List<Note> {
        return repository.getNotesNoFolder()
    }
}
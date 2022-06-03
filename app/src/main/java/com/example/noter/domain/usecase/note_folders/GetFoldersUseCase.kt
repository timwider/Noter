package com.example.noter.domain.usecase.note_folders

import com.example.noter.domain.NoteFoldersRepository
import com.example.noter.domain.model.NoteFolder

class GetFoldersUseCase(
    private val repository: NoteFoldersRepository
) {

    suspend fun execute(): List<NoteFolder> {
        return repository.getFolders()
    }
}
package com.example.noter.domain.usecase.note_folders

import com.example.noter.domain.NoteFoldersRepository
import com.example.noter.domain.model.NoteFolder

class DeleteFolderUseCase(
    private val repository: NoteFoldersRepository
) {
    suspend fun execute(noteFolderTitle: String) {
        repository.deleteFolder(noteFolderTitle = noteFolderTitle)
    }
}
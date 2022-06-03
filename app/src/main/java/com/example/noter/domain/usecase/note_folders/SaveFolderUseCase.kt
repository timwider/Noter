package com.example.noter.domain.usecase.note_folders

import com.example.noter.domain.NoteFoldersRepository
import com.example.noter.domain.model.NoteFolder

class SaveFolderUseCase(
    private val repository: NoteFoldersRepository
) {
    suspend fun execute(noteFolder: NoteFolder) {
        repository.insertFolder(noteFolder = noteFolder)
    }
}
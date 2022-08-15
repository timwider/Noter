package com.example.noter.domain.usecase.note_folders

import com.example.noter.domain.NoteFoldersRepository
import com.example.noter.domain.NotesRepository
import com.example.noter.domain.model.NoteFolder

class DeleteFolderUseCase(
    private val noteFoldersRepository: NoteFoldersRepository,
    private val notesRepository: NotesRepository
) {
    suspend fun execute(noteFolderTitle: String) {
        noteFoldersRepository.deleteFolder(noteFolderTitle)
        notesRepository.deleteFolderNotes(noteFolderTitle)
    }
}
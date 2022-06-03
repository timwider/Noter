package com.example.noter.data

import com.example.noter.data.storage.NoteFolder
import com.example.noter.data.storage.NotesFolderDao
import com.example.noter.domain.NoteFoldersRepository
import com.example.noter.domain.model.Note

class NoteFoldersRepositoryImpl(
    private val notesFolderDao: NotesFolderDao
): NoteFoldersRepository {

    override suspend fun getFolders(): List<com.example.noter.domain.model.NoteFolder> {
        return mapListToDomain(notesFolderDao.getNoteFolders())
    }

    override suspend fun insertFolder(noteFolder: com.example.noter.domain.model.NoteFolder) {
        notesFolderDao.insert(noteFolder = mapToStorage(noteFolder = noteFolder))
    }

    override suspend fun updateFolder(noteFolder: com.example.noter.domain.model.NoteFolder) {
        notesFolderDao.update(noteFolder = mapToStorage(noteFolder = noteFolder))
    }

    override suspend fun deleteFolder(noteFolderTitle: String) {
        notesFolderDao.delete(noteFolderTitle = noteFolderTitle)
    }

    private fun mapListToDomain(noteFolders: List<NoteFolder>): List<com.example.noter.domain.model.NoteFolder> {

        val newList = mutableListOf<com.example.noter.domain.model.NoteFolder>()

        for (noteFolder in noteFolders) {
            val newNoteFolder = com.example.noter.domain.model.NoteFolder(
                id = noteFolder.id,
                title = noteFolder.title
            )
            newList.add(newNoteFolder)
        }
        return newList
    }

    private fun mapToStorage(noteFolder: com.example.noter.domain.model.NoteFolder): NoteFolder {
        return NoteFolder(
            id = noteFolder.id,
            title = noteFolder.title
        )
    }
}
package com.example.noter.domain

import com.example.noter.domain.model.NoteFolder

interface NoteFoldersRepository {

    suspend fun getFolders(): List<NoteFolder>

    suspend fun insertFolder(noteFolder: NoteFolder)

    suspend fun updateFolder(noteFolder: NoteFolder)

    suspend fun deleteFolder(noteFolderTitle: String)


}
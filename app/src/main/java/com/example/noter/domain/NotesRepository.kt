package com.example.noter.domain

import com.example.noter.domain.model.Note
import com.example.noter.utils.SpanContainer

interface NotesRepository {

    suspend fun getNotes(): List<Note>

    suspend fun getNotesNoFolder() : List<Note>

    suspend fun insertNote(note: Note)

    suspend fun updateNote(note: Note)

    suspend fun deleteNote(note: Note)

    suspend fun getFolderNotes(folderName: String): List<Note>

    suspend fun deleteFolderNotes(folderName: String)

    suspend fun deleteNotesById(ids: List<Int>)
}
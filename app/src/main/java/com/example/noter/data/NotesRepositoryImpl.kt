package com.example.noter.data

import android.util.Log
import com.example.noter.data.storage.NotesDao
import com.example.noter.domain.NotesRepository
import com.example.noter.domain.model.Note
import com.example.noter.utils.SpanContainersConverter


class NotesRepositoryImpl(
    private val notesDao: NotesDao
): NotesRepository {

    // Give domain
    override suspend fun getNotes(): List<Note> {
       return mapListToDomain(notesDao.getNotes())
    }

    override suspend fun getNotesNoFolder(): List<Note> {
        return mapListToDomain(notesDao.getNotesNoFolder())
    }

    override suspend fun insertNote(note: Note) {
        notesDao.insert(mapToStorage(note = note))
    }

    // Receive domain, map to storage
    override suspend fun updateNote(note: Note) {
        notesDao.update(note = mapToStorage(note = note))
    }

    // Receive domain, map to storage
    override suspend fun deleteNote(note: Note) {
        notesDao.delete(mapToStorage(note = note))
    }

    override suspend fun getFolderNotes(folderName: String): List<Note> {
        return mapListToDomain(notesDao.getFolderNotes(folderNameToGet = folderName))
    }

    override suspend fun deleteFolderNotes(folderName: String) {
        notesDao.deleteFolderNotes(folderName = folderName)
    }

    override suspend fun deleteNotesById(ids: List<Int>) {
        notesDao.deleteNotesById(ids = ids)
    }

    private fun mapListToDomain(notes: List<com.example.noter.data.storage.Note>): List<Note> {

        val newList = mutableListOf<Note>()

        for (note in notes) {
            val newNote = Note(
                id = note.id,
                dateCreated = note.dateCreated,
                content = note.content,
                folderName = note.folderName,
                spanContainers = SpanContainersConverter().fromStringToContainers(stringContainers = note.spanContainers)
            )
            newList.add(newNote)
        }

        return newList
    }

    private fun mapToStorage(note: Note): com.example.noter.data.storage.Note {
        return com.example.noter.data.storage.Note(
            id = note.id,
            dateCreated = note.dateCreated,
            content = note.content,
            folderName = note.folderName,
            spanContainers = SpanContainersConverter().fromContainersToString(containers = note.spanContainers)
        )
    }

    override suspend fun clearEmptyNotes() {
        notesDao.clearEmptyNotes()
    }

}
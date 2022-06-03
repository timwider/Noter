package com.example.noter.data.storage

import androidx.room.*


@Dao
interface NotesDao {

    @Insert
    fun insert(note: Note)

    @Update
    fun update(note: Note)

    @Delete
    fun delete(note: Note)

    @Query("SELECT * FROM notes")
    fun getNotes(): List<Note>

    @Query("SELECT * FROM notes WHERE folderName=''")
    fun getNotesNoFolder() : List<Note>

    @Query("SELECT * FROM notes WHERE folderName=:folderNameToGet")
    fun getFolderNotes(folderNameToGet: String): List<Note>

    @Query("DELETE FROM notes WHERE folderName=:folderName")
    fun deleteFolderNotes(folderName: String)

}
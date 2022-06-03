package com.example.noter.data.storage

import androidx.room.*

@Dao
interface NotesFolderDao {

    @Insert
    fun insert(noteFolder: NoteFolder)

    @Update
    fun update(noteFolder: NoteFolder)

    @Query("DELETE FROM notes_folder WHERE title=:noteFolderTitle")
    fun delete(noteFolderTitle: String)

    @Query("SELECT * FROM notes_folder")
    fun getNoteFolders(): List<NoteFolder>
}
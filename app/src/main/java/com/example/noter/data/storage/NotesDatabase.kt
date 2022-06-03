package com.example.noter.data.storage
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.noter.utils.SpanContainersConverter

@Database(entities = [Note::class, NoteFolder::class], version = 3)
abstract class NotesDatabase: RoomDatabase() {

    abstract fun notesDao(): NotesDao

    abstract fun notesFolderDao(): NotesFolderDao
}

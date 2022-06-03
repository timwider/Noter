package com.example.noter.data.storage

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_folder")
data class NoteFolder(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
)
package com.example.noter.data.storage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.noter.utils.ListConverter
import com.example.noter.utils.SpanContainer
import com.example.noter.utils.SpanContainersConverter

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey (autoGenerate = true) val id: Int = 0,
    @ColumnInfo var dateCreated: String,
    @ColumnInfo var content: String,
    @ColumnInfo var folderName: String,
    @ColumnInfo @field:TypeConverters(ListConverter::class) var spanContainers: List<String>
)
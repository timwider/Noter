package com.example.noter.data.model

import com.example.noter.utils.SpanContainer

data class Note(
    val id: Int,
    val dateCreated: String,
    var content: String,
    var folderName: String?,
    var spanContainers: List<String>
)
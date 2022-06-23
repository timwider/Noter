package com.example.noter.data.model

data class Note(
    val id: Int,
    val dateCreated: String,
    var content: String,
    var folderName: String?,
    var spanContainers: List<String>
)
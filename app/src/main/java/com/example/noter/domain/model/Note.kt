package com.example.noter.domain.model

import com.example.noter.utils.SpanContainer
import java.io.Serializable

data class Note(
    val id: Int,
    val dateCreated: String,
    val content: String,
    var folderName: String,
    var spanContainers: List<SpanContainer>
): Serializable

data class NewNote(
    val id: Int
): Serializable, BaseModel(id) {

}
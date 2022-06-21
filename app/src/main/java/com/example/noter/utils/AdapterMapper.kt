package com.example.noter.utils

import com.example.noter.domain.model.Note

class AdapterMapper {

    private fun toAdapterModel(note: Note): NoteRV =
        NoteRV(
            id = note.id,
            dateCreated = note.dateCreated,
            content = note.content,
            folderName = note.folderName,
            spanContainers = note.spanContainers,
            selectionState = NoteSelectionState.NO_SELECTION
        )

    fun toDomainModel(note: NoteRV): Note =
        Note(
            id = note.id,
            dateCreated = note.dateCreated,
            content = note.content,
            folderName = note.folderName,
            spanContainers = note.spanContainers
        )

    fun listToAdapterModel(notes: List<Note>): List<NoteRV> {
        return if (notes.isNotEmpty()) {
            val newList = mutableListOf<NoteRV>()
            for (note in notes) {
                val noteRV = toAdapterModel(note)
                newList.add(noteRV)
            }
            newList.toList()
        } else emptyList()
    }

    fun listToDomainModel(notes: List<NoteRV>) : List<Note> {
        return if (notes.isNotEmpty()) {
            val newList = mutableListOf<Note>()
            for (note in notes) {
                val domainNote = toDomainModel(note)
                newList.add(domainNote)
            }
            newList.toList()
        } else emptyList()
    }
}

// This model is for RecyclerView adapter only. It stays on presentation level and is mapped to
// domain once it moves out of the adapter (e.g. to fragment when opening a note)
class NoteRV(
    val id: Int,
    val dateCreated: String,
    val content: String,
    var folderName: String,
    var spanContainers: List<SpanContainer>,
    var selectionState: NoteSelectionState
)

enum class NoteSelectionState {
    SELECTED,
    NOT_SELECTED,
    NO_SELECTION
}

enum class SelectionMode {
    ENABLED,
    DISABLED,
    NOT_SET // this is the default state
}

enum class SelectionModeAction {
    DELETE,
    SELECT_ALL,
    CANCEL,
    NOT_SET // default
}
package com.example.noter.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.noter.R
import com.example.noter.domain.model.Note
import com.example.noter.utils.NoteFormatter

class NotesAdapter(
    private val noteClickListener: (Note) -> Unit

): ListAdapter<Note, NotesAdapter.ViewHolder>(NotesCallback()), NoteFormatterSetup {

    class ViewHolder(item: View): RecyclerView.ViewHolder(item) {
        val tvName: TextView = item.findViewById(R.id.tv_note_name)
        val tvDateCreated: TextView = item.findViewById(R.id.tv_date_created)
        val noteCard: CardView = item.findViewById(R.id.note_card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_rv_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val noteItem = getItem(position)
        holder.tvName.text = setupNoteFormatter(note = noteItem.content)
        holder.tvDateCreated.text = noteItem.dateCreated
        holder.noteCard.setOnClickListener { noteClickListener(noteItem) }
    }


    class NotesCallback: DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean =
            oldItem.id == newItem.id

    }
    override fun setupNoteFormatter(note: String) : String {
        return NoteFormatter(note).extractName()
    }
}
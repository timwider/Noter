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

class FolderNotesAdapter(
    private val noteClickListener: (Note) -> Unit
): ListAdapter<Note, FolderNotesAdapter.ViewHolder>(FolderNotesCallback()), NoteFormatterSetup {

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val tvName: TextView = item.findViewById(R.id.tv_note_name)
        val tvDateCreated: TextView = item.findViewById(R.id.tv_date_created)
        val noteCard: CardView = item.findViewById(R.id.note_card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.note_rv_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.tvName.text = setupNoteFormatter(note = currentItem.content)
        holder.tvDateCreated.text = currentItem.dateCreated
        holder.noteCard.setOnClickListener { noteClickListener(currentItem) }
    }

    class FolderNotesCallback : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean =
            oldItem.id == newItem.id
    }

    override fun setupNoteFormatter(note: String): String {
        return NoteFormatter(note = note).extractName()
    }

}
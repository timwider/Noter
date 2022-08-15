package com.example.noter.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.noter.R
import com.example.noter.utils.*

class NotesAdapter(
    private val noteClickListener: (NoteRV, ImageView) -> Unit,
    private val noteLongClickListener: (NoteRV, Int) -> Unit,
    private val selectionHelper: SelectionHelper

): ListAdapter<NoteRV, NotesAdapter.ViewHolder>(NotesCallback()) {

    class ViewHolder(item: View): RecyclerView.ViewHolder(item) {
        val tvName: TextView = item.findViewById(R.id.tv_note_name)
        val tvDateCreated: TextView = item.findViewById(R.id.tv_date_created)
        val noteCard: CardView = item.findViewById(R.id.note_card)
        val ivSelection: ImageView = item.findViewById(R.id.iv_selection_state)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_rv_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val noteItem = getItem(position)
        holder.tvName.text = noteItem.extractName()
        holder.tvDateCreated.text = noteItem.dateCreated

        val drawableId = when (noteItem.selectionState) {
            NoteSelectionState.SELECTED -> R.drawable.selected_vector
            NoteSelectionState.NOT_SELECTED -> R.drawable.unselected_vector
            NoteSelectionState.NO_SELECTION -> null
        }
        drawableId?.let {
            holder.ivSelection.setImageResource(it)
            holder.ivSelection.visibility = View.VISIBLE
        }

        holder.noteCard.setOnClickListener { noteClickListener(noteItem, holder.ivSelection) }
        holder.noteCard.setOnLongClickListener { noteLongClickListener(noteItem, position); true }
    }

    fun changeListSelection(selectionState: NoteSelectionState) {
        val notes = currentList
        notes.forEach { it.selectionState = selectionState }
        submitList(notes)
        notifyItemRangeChanged(0, itemCount)
    }

    fun enableSelection(selectedNoteId: Int, selectedNotePosition: Int) {
        if (selectedNoteId != NOTE_NOT_FOUND_ID) {
            getItem(selectedNotePosition).selectionState = NoteSelectionState.SELECTED
            notifyItemChanged(selectedNotePosition)
        }
    }


    class NotesCallback: DiffUtil.ItemCallback<NoteRV>() {
        override fun areItemsTheSame(oldItem: NoteRV, newItem: NoteRV): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: NoteRV, newItem: NoteRV): Boolean {
            return (oldItem.id == newItem.id &&
                    oldItem.selectionState == newItem.selectionState &&
                    oldItem.content == newItem.content)
        }
    }
}
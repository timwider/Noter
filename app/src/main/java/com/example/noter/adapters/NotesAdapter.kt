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
import com.example.noter.utils.NoteFormatter
import com.example.noter.utils.NoteRV
import com.example.noter.utils.NoteSelectionState

class NotesAdapter(
    private val noteClickListener: (NoteRV, ImageView) -> Unit,
    private val noteLongClickListener: (NoteRV, ImageView) -> Unit

): ListAdapter<NoteRV, NotesAdapter.ViewHolder>(NotesCallback()), NoteFormatterSetup {

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
        holder.tvName.text = setupNoteFormatter(note = noteItem.content)
        holder.tvDateCreated.text = noteItem.dateCreated

//        val ivSelectionDrawable = (holder.ivSelection.drawable as AnimatedVectorDrawable)

        when (noteItem.selectionState) {
            NoteSelectionState.SELECTED -> {
                holder.ivSelection.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        holder.ivSelection.context.resources,
                        R.drawable.selected_vector,
                        null
                    )
                )
                holder.ivSelection.visibility = View.VISIBLE
            }
            NoteSelectionState.NOT_SELECTED -> {
                    holder.ivSelection.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            holder.ivSelection.context.resources,
                            R.drawable.unselected_vector,
                            null
                        )
                    )
                holder.ivSelection.visibility = View.VISIBLE
                }
            NoteSelectionState.NO_SELECTION -> {
                holder.ivSelection.visibility = View.GONE
            }
        }

        holder.noteCard.setOnClickListener{ noteClickListener(noteItem, holder.ivSelection) }
        holder.noteCard.setOnLongClickListener { noteLongClickListener(noteItem, holder.ivSelection); true }
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
    override fun setupNoteFormatter(note: String) : String {
        return NoteFormatter(note).extractName()
    }
}
package com.example.noter.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.noter.R
import com.example.noter.domain.model.NoteFolder

class FoldersAdapter(
    private val folderClickListener: (NoteFolder) -> Unit
): ListAdapter<NoteFolder, FoldersAdapter.ViewHolder>(FoldersCallback()) {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val folderName: TextView = itemView.findViewById(R.id.tv_folder_name)
        val folderCardLayout: LinearLayout = itemView.findViewById(R.id.folder_card_layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoldersAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.folder_rv_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.folderName.text = currentItem.title
        holder.folderCardLayout.setOnClickListener { folderClickListener(currentItem) }
    }

    class FoldersCallback: DiffUtil.ItemCallback<NoteFolder>() {
        override fun areItemsTheSame(oldItem: NoteFolder, newItem: NoteFolder): Boolean =
            oldItem.title == newItem.title

        override fun areContentsTheSame(oldItem: NoteFolder, newItem: NoteFolder): Boolean =
            oldItem.title == oldItem.title
    }
}
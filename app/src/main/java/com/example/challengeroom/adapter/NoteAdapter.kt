package com.example.challengeroom.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.challengeroom.databinding.ListNoteBinding
import com.example.challengeroom.model.Note

class NoteAdapter(val listener : Clicked):RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private val diff = object : DiffUtil.ItemCallback<Note>(){
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return when{
                oldItem.id != newItem.id -> false
                oldItem.tittle != newItem.tittle -> false
                oldItem.description != newItem.description -> false
                else -> true
            }
        }
    }

    private var data = AsyncListDiffer(this,diff)
    fun submitData(datas : MutableList<Note>) = data.submitList(datas)

    inner class NoteViewHolder(val binding : ListNoteBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(ListNoteBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.binding.apply {
            note = data.currentList[position]
            card.setOnClickListener {
                listener.onClick(data.currentList[position])
            }
            btnEdit.setOnClickListener {
                listener.onUpdate(data.currentList[position])
            }
            btnDelete.setOnClickListener {
                listener.onDelete(data.currentList[position])
            }

        }
    }

    override fun getItemCount(): Int = data.currentList.size

    interface Clicked{
        fun onClick(note: Note)
        fun onUpdate(note : Note)
        fun onDelete(note : Note)
    }

}
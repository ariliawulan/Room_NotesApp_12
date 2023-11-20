package com.example.room_notesapp_12.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.room_notesapp_12.database.Note
import com.example.room_notesapp_12.databinding.ItemNoteBinding

class NoteAdapter (private var ListNote: List<Note>):
    RecyclerView.Adapter<NoteAdapter.ItemNoteViewHolder>() {

    inner class ItemNoteViewHolder(private val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // aksi saat button delete di klik, maka function delete akan berjalan
        init {
            binding.Delete.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onDeleteClickListener?.onDeleteClick(ListNote[position])
                }
            }
        }

        // menyambung data yang ada di data class note dengan tampilan item_note
        fun bind(note: Note) {
            with(binding) {
                tvItemTitle.text = note.title
                tvItemDescription.text = note.description
                tvItemDate.text = note.date
            }
        }
    }

    // tempat untuk menaruh adta di adapternya
    @SuppressLint("NotifyDataSetChanged")
    fun setData(newNote: List<Note>) {
        ListNote = newNote
        notifyDataSetChanged()
    }

    // respon saat button delete di klik di UI maka datanya akan ke hapus
    interface OnDeleteClickListener {
        fun onDeleteClick(note: Note)
    }

    // menghubungkan listener saat buuton di klik
    private var onDeleteClickListener: OnDeleteClickListener? = null

    // menyimpan listener sebelum nantinya di delete
    fun setOnDeleteClickListener(listener: OnDeleteClickListener) {
        onDeleteClickListener = listener
    }

    // membuat item vew holder saat recycler viewe di panggil
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemNoteViewHolder {
        val binding = ItemNoteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ItemNoteViewHolder(binding)
    }

    // menghitung jumlah data
    override fun getItemCount(): Int = ListNote.size

    // bind posisi view holder agar tiap item view itu da datanya
    override fun onBindViewHolder(holder: ItemNoteViewHolder, position: Int) {
        holder.bind(ListNote[position])
    }
}
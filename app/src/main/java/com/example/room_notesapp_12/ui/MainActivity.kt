package com.example.room_notesapp_12.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.room_notesapp_12.AddNoteActivity
import com.example.room_notesapp_12.database.Note
import com.example.room_notesapp_12.database.NoteRoomDatabase
import com.example.room_notesapp_12.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    // inisiasi binding dan adapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        // ambil data dari database di NoteRoomDatabase
        val db = NoteRoomDatabase.getDatabase(this)
        val noteDao = db?.noteDao()

        // ambil semua data
        val allNoteapp: LiveData<List<Note>>? = noteDao?.allNotes

        // menaruh data ke noteAdapter
        allNoteapp?.observe(this) { note ->
            note?.let { noteAdapter.setData(it) }
        }

        // mengatur listsener delete ke adapter
        noteAdapter.setOnDeleteClickListener(object : NoteAdapter.OnDeleteClickListener {
            override fun onDeleteClick(note: Note) {
                // hapus data di background
                deleteNoteInBackground(note)
            }
        })

        // intent button add ke addnote
        binding.addButton.setOnClickListener{
            val intent = Intent(this, AddNoteActivity::class.java)
            startActivity(intent)
        }
    }

    // bg pengahpusan data di Dao nya akan di delete yang ada di Note Dao
    private fun deleteNoteInBackground(note: Note) {
        val noteDao = NoteRoomDatabase.getDatabase(this)?.noteDao()
        noteDao?.let {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    // delete menggunakan function di Dao nya
                    it.delete(note)
                }
            }
        }
    }

    // menghubungkan RV dengan data yang akan diisi
    private fun setupRecyclerView() {
        noteAdapter = NoteAdapter(emptyList())
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = noteAdapter
        }
    }

}
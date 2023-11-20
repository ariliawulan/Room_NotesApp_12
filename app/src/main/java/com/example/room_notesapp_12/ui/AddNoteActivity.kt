package com.example.room_notesapp_12

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.room_notesapp_12.database.Note
import com.example.room_notesapp_12.database.NoteDao
import com.example.room_notesapp_12.database.NoteRoomDatabase
import com.example.room_notesapp_12.databinding.ActivityAddNote2Binding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class AddNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNote2Binding
    private lateinit var noteDao: NoteDao
    private lateinit var executorService: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNote2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // operasi database dari thread yang beda
        executorService = Executors.newSingleThreadExecutor()

        // bbuat objek databasenya
        val db = NoteRoomDatabase.getDatabase(this)
        // ambil data dari db di ote dao
        if (db != null) {
            noteDao = db.noteDao()!!
        }

        // binding inisiasi saat button di klik
        // ketika di klik save nanti bakal di simpan
        binding.saveButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val description = binding.contentEditText.text.toString()
            val date = binding.dateEditText.text.toString()

            // cek data kosong
            if (title.isNotEmpty() && description.isNotEmpty() && date.isNotEmpty()) {
                val note = Note(title = title, description = description, date = date)

                // nambah pake insert di Note Dao
                insert(note)
                }
            else {
                // muncul toast
                Toast.makeText(applicationContext, "Masukkan data dengan benar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun insert(note: Note) {
        executorService.execute {
            noteDao.insert(note)
            // ketika data yang diisikan telah di klik maka dia akan kembali ke homepage nya
            // lalu nemapilkan list notes
            setResult(RESULT_OK)
            finish()
        }
    }
}

package com.example.room_notesapp_12.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.room_notesapp_12.database.NoteDao

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NoteRoomDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao?

    companion object {
        @Volatile
        private var INSTANCE: NoteRoomDatabase? = null

        fun getDatabase(context: Context): NoteRoomDatabase? {
            if (INSTANCE == null) {
                synchronized(NoteRoomDatabase::class.java) {
                    INSTANCE= databaseBuilder (
                        context.applicationContext,
                        NoteRoomDatabase::class.java,
                        "note_database"
                    )
                        .addMigrations(MIGRATION_2_1)
                        .build()
                }
            }
            return INSTANCE
        }

        private val MIGRATION_2_1: Migration = object : Migration(2, 1) {
            override fun migrate(database: SupportSQLiteDatabase) {
            }
        }
    }
}
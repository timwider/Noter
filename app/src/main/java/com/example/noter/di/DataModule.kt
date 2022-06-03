package com.example.noter.di

import androidx.room.Room
import androidx.room.migration.Migration
import com.example.noter.data.NoteFoldersRepositoryImpl
import com.example.noter.data.NotesRepositoryImpl
import com.example.noter.data.storage.NotesDatabase
import com.example.noter.domain.NoteFoldersRepository
import com.example.noter.domain.NotesRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

const val DATABASE_NAME = "notes_db"

val dataModule = module {

    single<NotesRepository> { NotesRepositoryImpl(notesDao = get()) }

    single<NoteFoldersRepository> { NoteFoldersRepositoryImpl(notesFolderDao = get()) }

    single {
        Room.databaseBuilder(
            androidApplication(),
            NotesDatabase::class.java,
            DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    single {
        val database = get<NotesDatabase>()
        database.notesDao()
    }

    single {
        val database = get<NotesDatabase>()
        database.notesFolderDao()
    }

}
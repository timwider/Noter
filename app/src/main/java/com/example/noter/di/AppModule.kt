package com.example.noter.di

import com.example.noter.presentation.viewmodel.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val appModule = module {
    viewModel { HomeViewModel(getNotesNoFolderUseCase = get()) }
    viewModel { CreateFolderViewModel(saveFolderUseCase = get()) }
    viewModel { HolderViewModel(getFoldersUseCase = get()) }
    viewModel { FolderViewModel(getFoldersUseCase = get()) }
    viewModel { FolderNotesViewModel(getFolderNotesUseCase = get(), deleteFolderNotesUseCase = get(), deleteFolderUseCase = get()) }
    viewModel {
        NoteViewModel(
            saveNoteUseCase = get(),
            deleteNoteUseCase = get(),
            updateNoteUseCase = get())
    }
}
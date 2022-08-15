package com.example.noter.di

import com.example.noter.presentation.dialogs.create_folder.CreateFolderViewModel
import com.example.noter.presentation.folder_notes.FolderNotesViewModel
import com.example.noter.presentation.folders.FolderViewModel
import com.example.noter.presentation.holder.HolderViewModel
import com.example.noter.presentation.home.HomeViewModel
import com.example.noter.presentation.viewmodel.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val appModule = module {
    viewModel { HomeViewModel(
        getNotesNoFolderUseCase = get(),
        deleteNotesByIdUseCase = get(),
        getFolderNotesUseCase = get(), deleteEmptyNotesUseCase = get()) }
    viewModel { CreateFolderViewModel(saveFolderUseCase = get()) }
    viewModel { HolderViewModel() }
    viewModel { FolderViewModel(getFoldersUseCase = get()) }
    viewModel { FolderNotesViewModel(deleteFolderUseCase = get(), deleteFolderNotesUseCase = get()) }
    viewModel { NoteViewModel(saveNoteUseCase = get(), deleteNoteUseCase = get(), updateNoteUseCase = get()) }
}
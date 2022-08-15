package com.example.noter.di

import com.example.noter.domain.usecase.note_folders.DeleteFolderUseCase
import com.example.noter.domain.usecase.note_folders.GetFoldersUseCase
import com.example.noter.domain.usecase.note_folders.SaveFolderUseCase
import com.example.noter.domain.usecase.note_folders.UpdateFolderUseCase
import com.example.noter.domain.usecase.notes.*
import org.koin.dsl.module

val domainModule = module {
    // For notes
    factory { GetNotesUseCase( repository = get()  ) }
    factory { UpdateNoteUseCase( repository = get() ) }
    factory { SaveNoteUseCase( repository = get() ) }
    factory { DeleteNoteUseCase( repository = get() ) }
    factory { GetNotesNoFolderUseCase(repository = get()) }
    factory { DeleteNotesByIdUseCase(repository = get()) }
    factory { DeleteEmptyNotesUseCase(repository = get()) }

    // For note folders
    factory { GetFoldersUseCase(repository = get()) }
    factory { UpdateFolderUseCase(repository = get()) }
    factory { SaveFolderUseCase(repository = get()) }
    factory { DeleteFolderUseCase(noteFoldersRepository = get(), notesRepository = get()) }

    // For folder notes
    factory { GetFolderNotesUseCase(repository = get()) }
    factory { DeleteFolderNotesUseCase(repository = get()) }



}
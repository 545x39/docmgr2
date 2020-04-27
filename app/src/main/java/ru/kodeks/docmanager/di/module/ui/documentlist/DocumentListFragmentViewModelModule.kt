package ru.kodeks.docmanager.di.module.ui.documentlist

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.kodeks.docmanager.di.key.ViewModelKey
import ru.kodeks.docmanager.ui.fragments.documentslist.list.DocumentListFragmentViewModel

@Module
abstract class DocumentListFragmentViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(DocumentListFragmentViewModel::class)
    abstract fun bindDocumentListFragmentViewModel(viewModel: DocumentListFragmentViewModel): ViewModel
}
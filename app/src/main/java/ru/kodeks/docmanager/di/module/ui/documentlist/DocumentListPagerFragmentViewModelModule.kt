package ru.kodeks.docmanager.di.module.ui.documentlist

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.kodeks.docmanager.di.key.ViewModelKey
import ru.kodeks.docmanager.ui.fragments.documentslist.pager.DocumentListPagerFragmentViewModel

@Module
abstract class DocumentListPagerFragmentViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(DocumentListPagerFragmentViewModel::class)
    abstract fun bindDocumentListPagerFragmentViewModel(viewModel: DocumentListPagerFragmentViewModel): ViewModel
}
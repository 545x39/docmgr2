package ru.kodeks.docmanager.di.modules.auth.progress

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.kodeks.docmanager.di.ViewModelKey
import ru.kodeks.docmanager.ui.fragments.auth.progress.AuthProgressViewModel

@Module
abstract class AuthProgressViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(AuthProgressViewModel::class)
    abstract fun bindMainViewModel(viewModel: AuthProgressViewModel): ViewModel
}
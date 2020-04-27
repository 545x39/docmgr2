package ru.kodeks.docmanager.di.module.ui.auth

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.kodeks.docmanager.di.key.ViewModelKey
import ru.kodeks.docmanager.ui.fragments.auth.AuthFragmentViewModel

@Module
abstract class AuthFragmentViewModelModule{

    @Binds
    @IntoMap
    @ViewModelKey(AuthFragmentViewModel::class)
    abstract fun bindBaseAuthViewModel(viewModel: AuthFragmentViewModel): ViewModel
}
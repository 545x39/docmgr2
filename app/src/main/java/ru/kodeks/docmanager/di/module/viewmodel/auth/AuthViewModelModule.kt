package ru.kodeks.docmanager.di.module.viewmodel.auth

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.kodeks.docmanager.di.key.ViewModelKey
import ru.kodeks.docmanager.ui.fragments.auth.base.AuthViewModel

@Module
abstract class AuthViewModelModule{
    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    abstract fun bindBaseAuthViewModel(viewModel: AuthViewModel): ViewModel
}
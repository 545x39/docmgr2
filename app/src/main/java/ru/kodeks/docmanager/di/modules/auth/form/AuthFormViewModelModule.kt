package ru.kodeks.docmanager.di.modules.auth.form

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.kodeks.docmanager.di.ViewModelKey
import ru.kodeks.docmanager.ui.fragments.auth.form.AuthFormViewModel

@Module
abstract class AuthFormViewModelModule{
    @Binds
    @IntoMap
    @ViewModelKey(AuthFormViewModel::class)
    abstract fun bindMainViewModel(viewModel: AuthFormViewModel): ViewModel
}
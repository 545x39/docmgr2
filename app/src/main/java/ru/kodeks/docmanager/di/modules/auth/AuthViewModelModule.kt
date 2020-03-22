package ru.kodeks.docmanager.di.modules.auth

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.kodeks.docmanager.di.ViewModelKey
import ru.kodeks.docmanager.ui.auth.AuthActivityViewModel

/** Зависимость для класса AuthActivityViewModel, которая предоставляется
 * посредством ViewModelProviderFactory.*/
@Module
abstract class AuthViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(AuthActivityViewModel::class)
    abstract fun bindAuthActivityViewModel(viewModel: AuthActivityViewModel): ViewModel
}
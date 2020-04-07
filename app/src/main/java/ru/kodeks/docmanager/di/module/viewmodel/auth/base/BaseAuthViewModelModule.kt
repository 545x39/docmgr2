package ru.kodeks.docmanager.di.module.viewmodel.auth.base

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.kodeks.docmanager.di.key.ViewModelKey
import ru.kodeks.docmanager.ui.fragments.auth.base.BaseAuthViewModel

@Module
abstract class BaseAuthViewModelModule{
    @Binds
    @IntoMap
    @ViewModelKey(BaseAuthViewModel::class)
    abstract fun bindBaseAuthViewModel(viewModel: BaseAuthViewModel): ViewModel
}
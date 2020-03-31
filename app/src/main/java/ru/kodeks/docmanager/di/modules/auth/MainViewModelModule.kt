package ru.kodeks.docmanager.di.modules.auth

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.kodeks.docmanager.di.ViewModelKey
import ru.kodeks.docmanager.ui.fragments.auth.MainViewModel

@Module
abstract class MainViewModelModule{
    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel
}
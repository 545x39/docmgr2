package ru.kodeks.docmanager.di.modules.main

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.kodeks.docmanager.di.ViewModelKey
import ru.kodeks.docmanager.ui.main.MainActivityViewModel

/** Зависимость для класса MainActivityViewModel, которая
 * предоставляется посредством ViewModelProviderFactory.*/
@Module
abstract class MainActivityViewModelModule{
    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    abstract fun bindMainActivityViewModel(viewModel: MainActivityViewModel): ViewModel
}

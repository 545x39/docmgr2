package ru.kodeks.docmanager.di.module.ui.mainactivity

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.kodeks.docmanager.di.key.ViewModelKey
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

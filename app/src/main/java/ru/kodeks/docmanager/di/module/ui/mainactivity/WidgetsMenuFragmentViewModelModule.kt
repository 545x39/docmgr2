package ru.kodeks.docmanager.di.module.ui.mainactivity

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.kodeks.docmanager.di.key.ViewModelKey
import ru.kodeks.docmanager.ui.fragments.widgetsmenu.WidgetsMenuFragmentViewModel

@Module
interface WidgetsMenuFragmentViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(WidgetsMenuFragmentViewModel::class)
    fun bindWidgetsMenuFragmentViewModel(viewModel: WidgetsMenuFragmentViewModel): ViewModel
}
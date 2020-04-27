package ru.kodeks.docmanager.di.module.ui

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import ru.kodeks.docmanager.di.factory.ViewModelProviderFactory
import javax.inject.Singleton

@Module
abstract class ViewModelFactoryModule {
    @Binds
    @Singleton
    /** Возвращать нужно именно родительский тип, иначе будет "found dependency cycle."*/
    abstract fun bindViewModelFactory(viewModelProviderFactory: ViewModelProviderFactory): ViewModelProvider.Factory
}
package ru.kodeks.docmanager.di.module.viewmodel.main

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.kodeks.docmanager.di.module.viewmodel.AuthScope
import ru.kodeks.docmanager.di.module.viewmodel.MainScope
import ru.kodeks.docmanager.di.module.viewmodel.auth.AuthFragmentModule
import ru.kodeks.docmanager.di.module.viewmodel.auth.AuthViewModelModule
import ru.kodeks.docmanager.ui.fragments.auth.AuthFragment
import ru.kodeks.docmanager.ui.fragments.start.StartFragment
import ru.kodeks.docmanager.ui.main.MainActivity

/** Модуль для всех активностей/фрагментов, которые потребуют внедрения зависимостей.*/
@Module
abstract class MainActivitySubcomponentModule {

    /** Таким образом модуль будет доступен не во всём приложении, а толькот в нужной активности.*/
    /** Аннотация @ContributesAndroidInjector генерирует @Subcomponent для этой активности!*/
    @ContributesAndroidInjector(
        modules = [MainActivityViewModelModule::class]
    )
    abstract fun contributeMainActivity(): MainActivity

}

@Module
interface AuthFragmentSubcomponentsModule {

    @AuthScope
    @ContributesAndroidInjector(modules = [AuthViewModelModule::class])
    fun contributeStartFragment(): StartFragment

    @AuthScope
    @ContributesAndroidInjector(modules = [AuthViewModelModule::class, AuthFragmentModule::class])
    fun contributeAuthFragment(): AuthFragment

}

@Module
interface MainFragmentsSubcomponentsModule{

    @MainScope
    @ContributesAndroidInjector(modules = [AuthViewModelModule::class])
    fun contributeDocumentListFragment(): StartFragment
}
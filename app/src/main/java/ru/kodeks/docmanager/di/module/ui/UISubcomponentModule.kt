package ru.kodeks.docmanager.di.module.ui

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.kodeks.docmanager.di.module.ui.auth.AuthFragmentViewModelModule
import ru.kodeks.docmanager.di.module.ui.documentlist.DocumentListFragmentViewModelModule
import ru.kodeks.docmanager.di.module.ui.documentlist.DocumentListPagerFragmentViewModelModule
import ru.kodeks.docmanager.di.module.ui.mainactivity.MainActivityViewModelModule
import ru.kodeks.docmanager.di.module.ui.mainactivity.WidgetsMenuFragmentViewModelModule
import ru.kodeks.docmanager.ui.fragments.auth.AuthFragment
import ru.kodeks.docmanager.ui.fragments.documentslist.list.DocumentListFragment
import ru.kodeks.docmanager.ui.fragments.documentslist.pager.DocumentListPagerFragment
import ru.kodeks.docmanager.ui.fragments.start.StartFragment
import ru.kodeks.docmanager.ui.fragments.widgetsmenu.WidgetsMenuFragment
import ru.kodeks.docmanager.ui.main.MainActivity

/** Модуль для всех активностей/фрагментов, которые потребуют внедрения зависимостей.*/
@Module
abstract class MainActivitySubcomponentModule {

    /** Таким образом модуль будет доступен не во всём приложении, а толькот в нужной активности.*/
    /** Аннотация @ContributesAndroidInjector генерирует @Subcomponent для этой активности!*/
    @ContributesAndroidInjector(
        modules = [MainActivityViewModelModule::class,
            AuthFragmentsModule::class,
            MainFragmentsModule::class]
    )
    abstract fun contributeMainActivity(): MainActivity
}

@Module
interface AuthFragmentsModule {

    @AuthScope
    @ContributesAndroidInjector(modules = [AuthFragmentViewModelModule::class])
    fun contributeStartFragment(): StartFragment

    @AuthScope
    @ContributesAndroidInjector(modules = [//AuthFragmentModule::class,
        AuthFragmentViewModelModule::class])
    fun contributeAuthFragment(): AuthFragment
}

@Module
interface MainFragmentsModule {

    @MainScope
    @ContributesAndroidInjector(
        modules = [//MainScopeModule::class,
            DocumentListPagerFragmentViewModelModule::class]
    )
    fun contributeDocumentListPagerFragment(): DocumentListPagerFragment

    @MainScope
    @ContributesAndroidInjector(
        modules = [//MainScopeModule::class,
            DocumentListFragmentViewModelModule::class]
    )
    fun contributeDocumentListFragment(): DocumentListFragment

    @MainScope
    @ContributesAndroidInjector(
        modules = [//MainScopeModule::class,
            WidgetsMenuFragmentViewModelModule::class]
    )
    fun contributeWidgetsMenuFragment(): WidgetsMenuFragment
}
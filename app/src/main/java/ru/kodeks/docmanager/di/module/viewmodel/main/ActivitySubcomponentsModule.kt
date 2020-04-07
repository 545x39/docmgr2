package ru.kodeks.docmanager.di.module.viewmodel.main

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.kodeks.docmanager.di.module.viewmodel.auth.AuthFragmentBuildersModule
import ru.kodeks.docmanager.ui.main.MainActivity

/** Модуль для всех активностей/фрагментов, которые потребуют внедрения зависимостей.*/
@Module
abstract class ActivitySubcomponentsModule {

    /** Таким образом модуль будет доступен не во всём приложении, а толькот в нужной активности.*/
    /** Аннотация @ContributesAndroidInjector генерирует @Subcomponent для этой активности!*/
    @ContributesAndroidInjector(
        modules = [MainActivityViewModelModule::class,
            MainActivityModule::class,
            AuthFragmentBuildersModule::class]
    )
    abstract fun contributeMainActivity(): MainActivity

}
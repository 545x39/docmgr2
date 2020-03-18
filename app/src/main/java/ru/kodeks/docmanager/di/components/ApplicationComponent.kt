package ru.kodeks.docmanager.di.components

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import ru.kodeks.docmanager.DocManagerApp
import ru.kodeks.docmanager.di.modules.ActivitiesModule
import ru.kodeks.docmanager.di.modules.ApplicationModule
import ru.kodeks.docmanager.di.modules.StubUserModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidSupportInjectionModule::class,
        ActivitiesModule::class,
        ApplicationModule::class,
        StubUserModule::class
    ]
)
interface ApplicationComponent : AndroidInjector<DocManagerApp> {


    @Component.Builder
    interface Builder {

        /** Связывает экземпляр приложения с компонентом.*/
        @BindsInstance
        fun application(application: DocManagerApp): Builder

        fun build(): ApplicationComponent
    }
}
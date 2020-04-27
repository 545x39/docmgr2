package ru.kodeks.docmanager.di

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import ru.kodeks.docmanager.DocManagerApp
import ru.kodeks.docmanager.di.module.ApplicationModule
import ru.kodeks.docmanager.di.module.ui.MainActivitySubcomponentModule
import ru.kodeks.docmanager.di.module.ui.ViewModelFactoryModule
import ru.kodeks.docmanager.di.module.work.signaturestamp.GetQualifiedSignatureStampWorkerModule
import ru.kodeks.docmanager.di.module.work.signaturestamp.GetSimpleSignatureStampWorkerModule
import ru.kodeks.docmanager.di.module.work.sync.CheckStateWorkerModule
import ru.kodeks.docmanager.di.module.work.sync.ParserWorkerModule
import ru.kodeks.docmanager.di.module.work.sync.SyncWorkerModule
import ru.kodeks.docmanager.di.module.work.sync.TestWorkerModule
import ru.kodeks.docmanager.work.checkstate.CheckStateWorker
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidSupportInjectionModule::class,
        ApplicationModule::class,
        //UI
        MainActivitySubcomponentModule::class,
        //ViewModelFactory
        ViewModelFactoryModule::class,
        //Workers
        CheckStateWorkerModule::class,
        SyncWorkerModule::class,
        TestWorkerModule::class,
        ParserWorkerModule::class,
        GetSimpleSignatureStampWorkerModule::class,
        GetQualifiedSignatureStampWorkerModule::class
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

    fun injectCheckStateWorker(worker: CheckStateWorker)

}
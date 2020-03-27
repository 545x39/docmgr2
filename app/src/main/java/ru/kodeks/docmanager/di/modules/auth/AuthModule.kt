package ru.kodeks.docmanager.di.modules.auth

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import ru.kodeks.docmanager.network.api.AuthApi

/** Модуль зависимостей, которые будут нужны только в подкомпоненте AuthActivity.*/
@Module
class AuthModule {
    @Provides
    fun provideApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }
}
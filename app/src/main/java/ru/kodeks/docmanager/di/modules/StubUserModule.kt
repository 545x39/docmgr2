package ru.kodeks.docmanager.di.modules

import dagger.Module
import dagger.Provides
import ru.kodeks.docmanager.User
import javax.inject.Singleton

//TODO Временная заглушка пользователя, пока не умеем вычитывать его из настроек/базы.
// Потом модуль нужно будет удалить, пользвателя получать напрямую из внедряемого конструктора User
@Module
class StubUserModule {

    @Provides
    @Singleton
    fun provideUser(): User {
        return User(login = "meyksin", password = "11111")
    }
}
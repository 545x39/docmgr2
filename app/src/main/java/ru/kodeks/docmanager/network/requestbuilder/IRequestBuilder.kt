package ru.kodeks.docmanager.network.requestbuilder

import ru.kodeks.docmanager.model.data.User
import ru.kodeks.docmanager.model.io.RequestBase

interface IRequestBuilder<T : RequestBase> {
    fun request(): T
    fun user(): Pair<String, String>// For login and password
    fun preview(): Boolean?
    fun runDeferred(): Boolean?
    fun delay(): Int?
    fun build(): T
}

abstract class RequestBuilder<T : RequestBase> : IRequestBuilder<T> {

    override fun preview(): Boolean? = null

    override fun runDeferred(): Boolean? = true

    override fun delay(): Int? = null

    override fun build(): T {
        return request().apply {
            user = User(
                login = user().first,
                password = user().second
            )
            preview = preview()
            delay = delay()
            runDeferred = runDeferred() ?: true
        }
    }
}
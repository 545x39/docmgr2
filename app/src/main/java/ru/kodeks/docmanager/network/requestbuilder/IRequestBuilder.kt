package ru.kodeks.docmanager.network.requestbuilder

import ru.kodeks.docmanager.const.DataFilter.CLASSIFIERS
import ru.kodeks.docmanager.const.DataFilter.DOCUMENTS
import ru.kodeks.docmanager.const.DataFilter.SETTINGS
import ru.kodeks.docmanager.const.DataFilter.WORKBENCH
import ru.kodeks.docmanager.const.DataFilter.WORKBENCH_META
import ru.kodeks.docmanager.model.data.User
import ru.kodeks.docmanager.model.io.RequestBase

interface IRequestBuilder<T : RequestBase> {
    /** For login and password*/
    fun credentials(): Pair<String, String>
    fun request(): T
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
                login = credentials().first,
                password = credentials().second
            )
            preview = preview()
            delay = delay()
            runDeferred = runDeferred() ?: true
        }
    }
}

const val DEFAULT_DATA_FILTER = CLASSIFIERS or DOCUMENTS or SETTINGS or WORKBENCH or WORKBENCH_META
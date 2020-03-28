package ru.kodeks.docmanager.network.resource

class AuthResource<T>(val status: UserAuthStatus, val data: T?, val message: String?) {

    enum class UserAuthStatus {
        AUTHENTICATED, ERROR, LOADING, NOT_AUTHENTICATED
    }

    companion object {
        fun <T> authenticated(data: T?): AuthResource<T?> {
            return AuthResource(UserAuthStatus.AUTHENTICATED, data, null)
        }

        fun <T> error(msg: String, data: T?): AuthResource<T?> {
            return AuthResource(UserAuthStatus.ERROR, data, msg)
        }

        fun <T> loading(data: T?): AuthResource<T?> {
            return AuthResource(UserAuthStatus.LOADING, data, null)
        }

        fun <T> logout(): AuthResource<T?> {
            return AuthResource(UserAuthStatus.NOT_AUTHENTICATED, null, null)
        }
    }

}
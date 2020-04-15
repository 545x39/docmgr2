package ru.kodeks.docmanager.const

sealed class SettingConstant

object Settings : SettingConstant() {
    /**
     * Адрес сервера
     */
    const val SERVER_ADDRESS = "addressPref"

    /**
     * Настройка шифрования пароля
     */
    const val ENCRYPT_PASSWORD = "encryptPassword"

    /**
     * Автоматический вход (флаг "запомнить" в форме аутентификции)
     */
    const val AUTO_LOGIN = "autoLogin"

    const val LAST_ENTERED_LOGIN = "lastEnteredLogin"
    const val LAST_ENTERED_PASSWORD = "lastEnteredPassword"

    /**
     * Период автоматического обновления
     */
    const val PREFERENCE_UPDATE_PERIOD_PREF = "updatePeriodPref"

    /** Периодичность оправки отложенных запросов*/
    const val DEFERRED_REQUEST_TIMEOUT = "deferredTimeout"

    /**
     * Режим предпросмотра
     */
    const val PREVIEW_MODE_PREFERENCE_KEY: String = "previewModePreference"

    /**
     * Таймауты
     */
    object Timeouts {
        const val PREFERENCE_GLOBAL_CATALOG_UPDATE_PERIOD: String = "globalCatalogAutoUpdatePeriod"
        const val PREFERENCE_GLOBAL_CATALOG_LAST_UPDATE_TIME = "globalCatalogLastUpdateTime"
    }

    /**
     * Оффсет сиквенса
     */
    const val SEQUENCE_OFFSET_PREFERENCE_KEY: String = "sequenceOffsetPreference"

    object SslSettings {
        const val SSL_CERTIFICATE_PASSWORD = "SSLCertificatePassword"
        const val USE_SSL_CERT_SETTING = "useSSLCertificate"
    }
}
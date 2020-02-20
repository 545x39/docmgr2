package ru.kodeks.docmanager.constants

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
     * Период автоматического обналвения
     */
    const val PREFERENCE_UPDATE_PERIOD_PREF = "updatePeriodPref"
    /**
     * Режим предпросмотра
     */
    const val PREVIEW_MODE_PREFERENCE_KEY: String = "previewModePreference"
    /**
     * СКРЫТЫЕ НАСТРОЙКИ
     * */
    const val PREFERENCE_LOGIN = "loginPref"
    const val PREFERENCE_PASSWORD = "passwordPref"
    const val PREFERENCE_FLURRY_KEY = "flurryKeyPref"
    /**
     * Таймауты
     */
    val PREFERENCE_GLOBAL_CATALOG_UPDATE_PERIOD: String? = "globalCatalogAutoUpdatePeriod"
    const val PREFERENCE_GLOBAL_CATALOG_LAST_UPDATE_TIME = "globalCatalogLastUpdateTime"
    /**
     * Оффсет сиквенса
     */
    val SEQUENCE_OFFSET_PREFERENCE_KEY: String? = "sequenceOffsetPreference"
}
package ru.kodeks.docmanager.model.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/** Класс c данными по сиквенсам */
data class Version(
        /** Основной текущий сиквенс, который используется для версионирования документов, справочников и многих других объектов. */
        @SerializedName("main")
        @Expose
        var main: Int? = 0,
        /** Текущий сиквенс ГК (данный сиквенс не позволяет корректно обновлять ГК). */
        @SerializedName("global")
        @Expose
        var global: Int? = 0,
        /** Текущий сиквенс настроек. */
        @SerializedName("settings")
        @Expose
        var settings: Int? = 0,
        /** Текущий сиквенс для личных папок. */
        @SerializedName("privateFolders")
        @Expose
        var privateFolders: Long? = 0,
        /** Текущий сиквенс для общих папок. */
        @SerializedName("sharedFolders")
        @Expose
        var sharedFolders: Long? = 0
)

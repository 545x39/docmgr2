package ru.kodeks.docmanager.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/** Настройки пользователя.*/
@Entity(tableName = "settings")
data class Setting(
        /** Название настройки*/
        @SerializedName("key")
        @Expose
        @ColumnInfo(name = "key")
        @PrimaryKey
        var key: String = "",
        /** Тип значения настройки.(Byte)*/
        @SerializedName("type")
        @Expose
        @ColumnInfo(name = "type")
        var type: Int? = null,
        /** Значение настройки*/
        @SerializedName("value")
        @Expose
        @ColumnInfo(name = "value")
        var value: String? = null
) : ObjectBase()
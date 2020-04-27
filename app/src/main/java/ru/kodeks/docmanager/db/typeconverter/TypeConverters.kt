package ru.kodeks.docmanager.db.typeconverter

import androidx.room.TypeConverter

class IntListToStringTypeConverter {

    @TypeConverter
    fun toIntList(string: String?): List<Int> {
        val split = string?.split(",").orEmpty()
        val out: MutableList<Int> = mutableListOf()
        for (i in split.indices) {
            kotlin.runCatching { out[i] = split[i].toInt() }
        }
        return out.toList()
    }

    @TypeConverter
    fun toString(list: List<Int>?): String? {
        return list?.let {
            buildString {
                for (i in list.indices) {
                    append(list[i])
                    when (i < list.size - 1) {
                        true -> append(",")
                        false -> {
                        }
                    }
                }
            }
        }
    }
}
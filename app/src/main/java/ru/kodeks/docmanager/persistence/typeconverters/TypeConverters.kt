package ru.kodeks.docmanager.persistence.typeconverters

import androidx.room.TypeConverter

class UserRightsTypeConverter {

    @TypeConverter
    fun toIntList(string: String): List<Int> {
        val split = string.split(",")
        val out: MutableList<Int> = mutableListOf()
        for (i in split.indices) {
            try {
                out[i] = split[i].toInt()
            } catch (e: NumberFormatException) {
                //
            }
        }
        return out.toList()
    }

    @TypeConverter
    fun toString(list: List<Int>): String {
        return buildString {
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
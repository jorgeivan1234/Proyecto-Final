package com.fic.dualhabit10.data.local

import androidx.room.TypeConverter
import com.google.common.reflect.TypeToken
import com.google.gson.Gson

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromstringList(value: List<String>?): String {
        return gson.toJson(value ?: emptyList<String>())
    }

    @TypeConverter
    fun tostringList(value: String): List<String> {
        if (value.isNullOrEmpty()) return emptyList()
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType)
    }
}
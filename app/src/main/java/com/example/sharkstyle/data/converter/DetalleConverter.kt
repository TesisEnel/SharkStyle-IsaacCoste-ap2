package com.example.sharkstyle.data.converter

import androidx.room.TypeConverter
import com.example.sharkstyle.data.local.entities.DetalleCarritoEntity
import com.example.sharkstyle.data.local.entities.DetalleCompraEntity
import com.example.sharkstyle.data.local.entities.DetalleProductoEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DetalleConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromDetalleCarritoList(value: List<DetalleCarritoEntity>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toDetalleCarritoList(value: String): List<DetalleCarritoEntity> {
        val listType = object : TypeToken<List<DetalleCarritoEntity>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromDetalleCompraList(value: List<DetalleCompraEntity>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toDetalleCompraList(value: String): List<DetalleCompraEntity> {
        val listType = object : TypeToken<List<DetalleCompraEntity>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromDetalleProductoList(value: List<DetalleProductoEntity>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toDetalleProductoList(value: String): List<DetalleProductoEntity> {
        val listType = object : TypeToken<List<DetalleProductoEntity>>() {}.type
        return gson.fromJson(value, listType)
    }
}

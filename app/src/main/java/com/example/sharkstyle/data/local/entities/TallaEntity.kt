package com.example.sharkstyle.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Tallas")
data class TallaEntity (
    @PrimaryKey
    val tallaId: Int,
    val medida: String
)

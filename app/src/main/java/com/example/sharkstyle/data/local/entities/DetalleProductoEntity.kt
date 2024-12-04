package com.example.sharkstyle.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "DetalleProductos",
    foreignKeys = [
        ForeignKey(
            entity = ProductoEntity::class,
            parentColumns = ["productoId"],
            childColumns = ["productoId"],
        ),
        ForeignKey(
            entity = TallaEntity::class,
            parentColumns = ["tallaId"],
            childColumns = ["tallaId"]
        )
    ]
)
data class DetalleProductoEntity(
    @PrimaryKey
    val detalleProductoId: Int,
    val productoId: Int,
    val tallaId: Int,
    val existencia: Int
)

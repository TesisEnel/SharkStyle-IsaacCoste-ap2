package com.example.sharkstyle.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "CarritoDetalles",
    foreignKeys = [
        ForeignKey(
            entity = CarritoEntity::class,
            parentColumns = ["carritoId"],
            childColumns = ["carritoId"],
        ),
        ForeignKey(
            entity = ProductoEntity::class,
            parentColumns = ["productoId"],
            childColumns = ["productoId"]
        )
    ],
    indices = [Index("carritoId"), Index("productoId")]
)
data class DetalleCarritoEntity(
    @PrimaryKey
    val detalleCarritoId: Int? = null,
    val carritoId: Int? = null,
    val productoId: Int? = null,
    val cantidad: Int? = null,
    val precio: Double? = null
)
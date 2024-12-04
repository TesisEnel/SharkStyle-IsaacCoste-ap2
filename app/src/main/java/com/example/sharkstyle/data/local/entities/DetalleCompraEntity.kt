package com.example.sharkstyle.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "CompraDetalles",
    foreignKeys = [
        ForeignKey(
            entity = CompraEntity::class,
            parentColumns = ["compraId"],
            childColumns = ["compraId"],
        ),
        ForeignKey(
            entity = ProductoEntity::class,
            parentColumns = ["productoId"],
            childColumns = ["productoId"]
        )
    ]
)


data class DetalleCompraEntity(
    @PrimaryKey
    val detalleCompraId: Int,
    val compraId: Int,
    val productoId: Int,
    val cantidad: Int,
)

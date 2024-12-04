package com.example.sharkstyle.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName="Compras",
    foreignKeys = [
        ForeignKey(
            entity = UsuarioEntity::class,

            parentColumns = ["usuarioId"],
            childColumns = ["usuarioId"]
        )
    ]
)
data class CompraEntity (
    @PrimaryKey
    val compraId: Int,
    val usuarioId: Int,
    val total: Double,
    val detallesCompra: List<DetalleCompraEntity> = listOf()
)
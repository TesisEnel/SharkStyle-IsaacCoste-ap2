package com.example.sharkstyle.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "Carritos",
    foreignKeys = [
        ForeignKey(
            entity = UsuarioEntity::class,

            parentColumns = ["usuarioId"],
            childColumns = ["usuarioId"]
        )
    ],
    indices = [Index("usuarioId")]
)

data class CarritoEntity(
    @PrimaryKey
    val carritoId: Int? = null,
    val usuarioId: Int,
    val pagado : Boolean,
    val detallesCarrito: List<DetalleCarritoEntity> = listOf()
)
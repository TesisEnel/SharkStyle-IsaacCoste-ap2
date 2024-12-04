package com.example.sharkstyle.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.sharkstyle.data.local.entities.CategoriaEntity
import com.example.sharkstyle.data.local.entities.DetalleProductoEntity

@Entity(
    tableName = "Productos",
    foreignKeys = [
        ForeignKey(
            entity = CategoriaEntity::class,

            parentColumns = ["categoriaId"],
            childColumns = ["categoriaId"]
        )
    ]
)

data class ProductoEntity(
    @PrimaryKey
    val productoId: Int,
    val titulo: String,
    val categoriaId: Int,
    val precio: Double,
    val descripcion: String,
    val existencia: Int,
    val imagen: String,
    val impuesto: Double,
    val detalleProducto: List<DetalleProductoEntity> = listOf()
)
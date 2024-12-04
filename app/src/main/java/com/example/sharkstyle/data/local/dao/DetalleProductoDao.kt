package com.example.sharkstyle.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.sharkstyle.data.local.entities.DetalleProductoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DetalleProductoDao {
    @Insert
    suspend fun insert(detalle: DetalleProductoEntity)

    @Query("SELECT * FROM DetalleProductos WHERE productoId = :productoId")
    suspend fun getDetallesByProducto(productoId: Int): List<DetalleProductoEntity>

    @Query("SELECT * FROM DetalleProductos WHERE tallaId = :tallaId")
    suspend fun getDetallesByTalla(tallaId: Int): List<DetalleProductoEntity>

    @Query("SELECT * FROM DetalleProductos WHERE detalleProductoId = :detalleProductoId")
    suspend fun getDetalleById(detalleProductoId: Int): DetalleProductoEntity?

    @Query("SELECT * FROM DetalleProductos")
    suspend fun getAllDetalles(): List<DetalleProductoEntity>

    @Delete
    suspend fun deleteDetalle(detalle: DetalleProductoEntity)
    @Query("SELECT * FROM DetalleProductos")
    fun getAllDetallesFlow(): Flow<List<DetalleProductoEntity>>

}
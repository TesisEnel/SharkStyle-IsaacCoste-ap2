package com.example.sharkstyle.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.sharkstyle.data.local.entities.DetalleCompraEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DetalleCompraDao {
    @Insert
    suspend fun insert(detalle: DetalleCompraEntity)

    @Query("SELECT * FROM CompraDetalles WHERE compraId = :compraId")
    fun getDetallesByCompra(compraId: Int): Flow<List<DetalleCompraEntity>>

    @Delete
    suspend fun deleteDetalle(detalle: DetalleCompraEntity)
}
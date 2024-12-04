package com.example.sharkstyle.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.sharkstyle.data.local.entities.DetalleCarritoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DetalleCarritoDao{
    @Insert
    suspend fun insert(detalle: DetalleCarritoEntity)

    @Query("SELECT * FROM CarritoDetalles WHERE carritoId = :carritoId")
    fun getDetallesByCarrito(carritoId: Int): Flow<List<DetalleCarritoEntity>>

    @Delete
    suspend fun deleteDetalle(detalle: DetalleCarritoEntity)

    @Query(
        """
            SELECT EXISTS 
                (SELECT 1 
                 FROM carritoDetalles 
                 WHERE productoId = :productoId AND carritoId = :carritoId)
        """
    )
    suspend fun carritoDetalleExit(productoId: Int, carritoId: Int): Boolean

    @Query(
        """
            SELECT * FROM CarritoDetalles
            WHERE productoId=:id and carritoId=:idCarrito
            LIMIT 1
        """
    )
    suspend fun getCarritoDetalleByProductoId(id: Int, idCarrito: Int): DetalleCarritoEntity?

}
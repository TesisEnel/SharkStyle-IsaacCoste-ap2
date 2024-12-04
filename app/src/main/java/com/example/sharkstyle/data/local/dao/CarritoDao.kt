package com.example.sharkstyle.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.sharkstyle.data.local.entities.CarritoEntity
import com.example.sharkstyle.data.local.entities.DetalleCarritoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CarritoDao {
    @Upsert
    suspend fun save(carrito: CarritoEntity)
    @Query("""
        SELECT *
        FROM Carritos
        WHERE carritoId = :id
        LIMIT 1     
        """)
    suspend fun find(id: Int): List<CarritoEntity?>

    @Query("""
        SELECT *
        FROM Carritos
        WHERE usuarioId = :id
        LIMIT 1     
        """)
    suspend fun findUsuario(id: Int): CarritoEntity?

    @Query("SELECT * FROM carritos WHERE usuarioId = :usuarioId")
    fun getCarritosPorUsuario(usuarioId: Int): List<CarritoEntity>

    @Query(
        """
            SELECT *
            FROM carritos
            WHERE pagado = 0 AND usuarioId = :usuarioId 
            ORDER BY carritoId DESC 
            LIMIT 1
        """
    )
    suspend fun getLastCarritoByUsuario(usuarioId: Int): CarritoEntity?

    @Query("SELECT * FROM carritos WHERE usuarioId = :usuarioId AND pagado = 0 LIMIT 1")
    suspend fun getCarritoNoPagadoPorUsuario(usuarioId: Int): CarritoEntity?
    @Delete
    suspend fun delete (ticket: CarritoEntity)
    @Query("SELECT * FROM Carritos")
    fun getAll(): Flow<List<CarritoEntity>>

    @Query("SELECT * FROM CarritoDetalles WHERE carritoId = :carritoId")
    fun getCarritoDetallesPorCarritoId(carritoId: Int): Flow<List<DetalleCarritoEntity>>












}
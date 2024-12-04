package com.example.sharkstyle.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.sharkstyle.data.local.entities.CompraEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CompraDao {
    @Upsert
    suspend fun save(compra: CompraEntity)
    @Query("""
        SELECT *
        FROM Compras
        WHERE compraId = :id
        LIMIT 1     
        """)
    suspend fun find(id: Int): CompraEntity?

    @Query("""
        SELECT *
        FROM Compras
        WHERE usuarioId = :id
        LIMIT 1     
        """)
    suspend fun findUsuario(id: Int): CompraEntity?

    @Query("""
        SELECT *
        FROM Compras
        WHERE LOWER(:total)
        LIMIT 1
    """)
    suspend fun findByTotal(total: Double): CompraEntity?

    @Delete
    suspend fun delete (ticket: CompraEntity)
    @Query("SELECT * FROM Compras")
    fun getAll(): Flow<List<CompraEntity>>










}
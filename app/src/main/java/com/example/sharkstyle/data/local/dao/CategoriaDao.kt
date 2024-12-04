package com.example.sharkstyle.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.sharkstyle.data.local.entities.CategoriaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriaDao{
    @Upsert
    suspend fun save(categoria: CategoriaEntity)
    @Query("""
        SELECT *
        FROM Categorias
        WHERE categoriaId = :id
        LIMIT 1     
        """)
    suspend fun find(id: Int): CategoriaEntity?

    @Query(
        """
            SELECT *
            FROM Categorias
            WHERE LOWER(:nombre)
            LIMIT 1
        """
    )
    suspend fun findByNombre(nombre: String): CategoriaEntity?

    @Query("""
        SELECT *
        FROM Categorias
        WHERE LOWER(:imagen)
        LIMIT 1
        """)
    suspend fun findByImagen(imagen: String): CategoriaEntity?


    @Delete
    suspend fun delete (ticket: CategoriaEntity)
    @Query("SELECT * FROM Categorias")
    fun getAll(): Flow<List<CategoriaEntity>>











}
package com.example.sharkstyle.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.sharkstyle.data.local.entities.UsuarioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    @Upsert
    suspend fun save(usuario: UsuarioEntity)

    @Query("""
        SELECT *
        FROM Usuarios
        WHERE usuarioId = :id
        LIMIT 1     
    """)
    suspend fun find(id: Int): UsuarioEntity?

    @Query("""
        SELECT * FROM Usuarios
        WHERE email = :correo
        LIMIT 1 
    """)
    suspend fun getUsuarioByCorreo(correo: String): UsuarioEntity?

    @Query("""
        SELECT *
        FROM Usuarios
        WHERE LOWER(:nombre)
        LIMIT 1""")
    suspend fun findByNombre(nombre: String): UsuarioEntity?

    @Query("""
        SELECT *
        FROM Usuarios
        WHERE LOWER(:email)
        LIMIT 1
    """)
    suspend fun findByEmail(email: String): UsuarioEntity?


    @Delete
    suspend fun delete(usuario: UsuarioEntity)
    @Query("SELECT * FROM Usuarios")
    fun getAll(): Flow<List<UsuarioEntity>>

}
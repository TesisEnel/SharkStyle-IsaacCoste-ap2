package com.example.sharkstyle.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.sharkstyle.data.local.entities.ProductoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao{
    @Upsert
    suspend fun save(producto: ProductoEntity)
    @Query("""
        SELECT *
        FROM Productos
        WHERE productoId = :id
        LIMIT 1     
        """)
    suspend fun find(id: Int): ProductoEntity?

    @Query(
        """
            SELECT *
            FROM Productos
            WHERE LOWER(:titulo)
            LIMIT 1
        """
    )
    suspend fun findBytitulo(titulo: String): ProductoEntity?

    @Query("""
        SELECT * 
        FROM productos 
        WHERE categoriaId = 
        :categoriaId
        """)
    suspend fun getProductosByCategoria(categoriaId: Int): List<ProductoEntity>


    @Query("""
    SELECT *
    FROM Productos
    WHERE LOWER(:precio)
    LIMIT 1
""")
suspend fun findByprecio(precio: Double): ProductoEntity?

@Query("""
    SELECT *
    FROM Productos
    WHERE LOWER(:descripcion)
    LIMIT 1""")
suspend fun findBydescripcion(descripcion: String): ProductoEntity?

@Query("""
    SELECT *
    FROM Productos
    WHERE LOWER(:existencia)
    LIMIT 1""")
suspend fun findByexistencia(existencia: Int): ProductoEntity?

@Query("""
    SELECT *
    FROM Productos
    WHERE LOWER(:imagen)
    LIMIT 1""")
suspend fun findByimagen(imagen: String): ProductoEntity?

@Query("""
    SELECT *
    FROM Productos
    WHERE LOWER(:impuesto)
    LIMIT 1""")
suspend fun findByimpuesto(impuesto: Double): ProductoEntity?

    @Delete
    suspend fun delete (ticket: ProductoEntity)
    @Query("SELECT * FROM Productos")
    fun getAll(): Flow<List<ProductoEntity>>
}
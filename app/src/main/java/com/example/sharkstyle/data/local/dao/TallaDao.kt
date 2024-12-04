package com.example.sharkstyle.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.sharkstyle.data.local.entities.TallaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TallaDao {
    @Insert
    suspend fun insert(talla: TallaEntity)

    @Query(
        """
        SELECT * 
        FROM tallas 
        WHERE tallaId = :tallaId
        """
    )
    suspend fun getTallaById(tallaId: Int): TallaEntity?

    @Query(
        """
        SELECT * 
        FROM tallas 
        WHERE LOWER(medida) = LOWER(:medida)
        """
    )
    suspend fun getTallaByMedida(medida: String): TallaEntity?

    @Delete
    suspend fun deleteTalla(talla: TallaEntity)

    @Query(
        """
        SELECT * 
        FROM tallas
        """
    )
    fun getAllTallas(): Flow<List<TallaEntity>>
}

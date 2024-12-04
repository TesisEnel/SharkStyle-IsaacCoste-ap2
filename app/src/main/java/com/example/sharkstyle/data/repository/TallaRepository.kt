package com.example.sharkstyle.data.repository

import com.example.sharkstyle.data.local.dao.TallaDao
import com.example.sharkstyle.data.local.entities.TallaEntity
import com.example.sharkstyle.data.remote.RemoteDataSource
import com.example.sharkstyle.data.remote.dto.TallaDto
import com.example.sharkstyle.data.remote.dto.toEntity
import com.example.sharkstyle.data.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class TallaRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val tallaDao: TallaDao
    ) {
    fun getTallas(): Flow<Resource<List<TallaEntity>>> = flow {
        try {
            emit(Resource.Loading())

            val remoteTallas = remoteDataSource.getTallas()

            val localTallas = remoteTallas.map { it.toEntity() }
            localTallas.forEach { tallaDao.insert(it) }

            emit(Resource.Success(localTallas))
        } catch (e: HttpException) {
            emit(Resource.Error( "Error de conexion: ${e.message()}"))
        } catch (e: Exception) {
            val localTallas = tallaDao.getAllTallas().firstOrNull()
            if (localTallas.isNullOrEmpty()) {
                emit(Resource.Error( "No se encuentran datos locales"))
            } else {
                emit(Resource.Success(localTallas))
            }
        }
    }
    fun getTalla(id: Int): Flow<Resource<TallaEntity>> = flow {
        try {
            emit(Resource.Loading())

            val localTalla = tallaDao.getTallaById(id)
            emit(Resource.Success(localTalla!!))
        } catch (e: HttpException) {
            emit(Resource.Error( "Error de conexion: ${e.message()}"))
        } catch (e: Exception) {
            emit(Resource.Error( "Error desconocido: ${e.message}"))
        }
    }
    fun saveTalla(talla: TallaDto): Flow<Resource<TallaEntity>> = flow {
        try {
            emit(Resource.Loading())
            val remoteTalla = remoteDataSource.addTallas(talla)
            val localTalla = remoteTalla.toEntity()
            tallaDao.insert(localTalla)
            emit(Resource.Success(localTalla))
        } catch (e: HttpException) {
            emit(Resource.Error( "Error de conexion: ${e.message()}"))
        } catch (e: Exception) {
            emit(Resource.Error( "Error desconocido: ${e.message}"))
        }
    }
    suspend fun deleteTalla(id: Int): Resource<Unit> {
        return try {
            remoteDataSource.deleteTalla(id)
            val localTalla = tallaDao.getTallaById(id)
            if (localTalla != null) {
                tallaDao.deleteTalla(localTalla)
            }
            Resource.Success(Unit)
        } catch (e: HttpException) {
            Resource.Error( "Error de conexion: ${e.message()}")
        } catch (e: Exception) {
            val localTalla = tallaDao.getTallaById(id)
            if (localTalla != null) {
                tallaDao.deleteTalla(localTalla)
            }
            Resource.Success(Unit)
        }
    }
}
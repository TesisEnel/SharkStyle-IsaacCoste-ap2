package com.example.sharkstyle.data.repository

import com.example.sharkstyle.data.local.dao.CompraDao
import com.example.sharkstyle.data.local.entities.CompraEntity
import com.example.sharkstyle.data.remote.RemoteDataSource
import com.example.sharkstyle.data.remote.dto.CompraDto
import com.example.sharkstyle.data.remote.dto.toEntity
import com.example.sharkstyle.data.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class CompraRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val compraDao: CompraDao
) {
    fun getCompras(): Flow<Resource<List<CompraEntity>>> = flow {
        try {
            emit(Resource.Loading())

            val remoteCompras = remoteDataSource.getCompras()
            val localCompras = remoteCompras.map { it.toEntity() }
            localCompras.forEach { compraDao.save(it) }

            emit(Resource.Success(localCompras))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión: ${e.message()}"))
        } catch (e: Exception) {
            val localCompras = compraDao.getAll().firstOrNull()
            if (localCompras.isNullOrEmpty()) {
                emit(Resource.Error("No se encuentran datos locales"))
            } else {
                emit(Resource.Success(localCompras))
            }
        }
    }

    fun getCompra(id: Int): Flow<Resource<CompraEntity>> = flow {
        try {
            emit(Resource.Loading())
            val localCompra = compraDao.find(id)
            if (localCompra != null) {
                emit(Resource.Success(localCompra))
            } else {
                emit(Resource.Error("Compra no encontrada"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Error desconocido: ${e.message}"))
        }
    }

    suspend fun saveCompra(compra: CompraDto): Resource<Unit> {
        return try {
            val remoteCompra = remoteDataSource.addCompra(compra)
            compraDao.save(remoteCompra.toEntity())
            Resource.Success(Unit)
        } catch (e: HttpException) {
            Resource.Error("Error de conexión: ${e.message()}")
        } catch (e: Exception) {
            Resource.Error("Error desconocido: ${e.message}")
        }
    }

    suspend fun deleteCompra(id: Int): Resource<Unit> {
        return try {
            remoteDataSource.deleteCompra(id)
            compraDao.find(id)?.let { compraDao.delete(it) }
            Resource.Success(Unit)
        } catch (e: HttpException) {
            Resource.Error("Error de conexión: ${e.message()}")
        } catch (e: Exception) {
            compraDao.find(id)?.let { compraDao.delete(it) }
            Resource.Success(Unit)
        }
    }
}

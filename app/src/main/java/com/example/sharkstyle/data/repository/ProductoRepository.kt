package com.example.sharkstyle.data.repository

import com.example.sharkstyle.data.local.dao.ProductoDao
import com.example.sharkstyle.data.local.entities.ProductoEntity
import com.example.sharkstyle.data.remote.RemoteDataSource
import com.example.sharkstyle.data.remote.dto.ProductoDto
import com.example.sharkstyle.data.remote.dto.toEntity
import com.example.sharkstyle.data.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class ProductoRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val productoDao: ProductoDao
) {
    fun getProductos(): Flow<Resource<List<ProductoEntity>>> = flow {
        try {
            emit(Resource.Loading())

            val remoteProductos = remoteDataSource.getProductos()
            val localProductos = remoteProductos.map { it.toEntity() }
            localProductos.forEach { productoDao.save(it) }

            emit(Resource.Success(localProductos))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión: ${e.message()}"))
        } catch (e: Exception) {
            val localProductos = productoDao.getAll().firstOrNull()
            if (localProductos.isNullOrEmpty()) {
                emit(Resource.Error("No se encuentran datos locales"))
            } else {
                emit(Resource.Success(localProductos))
            }
        }
    }

    fun getProducto(id: Int): Flow<Resource<ProductoEntity>> = flow {
        try {
            emit(Resource.Loading())
            val localProducto = productoDao.find(id)
            if (localProducto != null) {
                emit(Resource.Success(localProducto))
            } else {
                emit(Resource.Error("Producto no encontrado"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Error desconocido: ${e.message}"))
        }
    }

    suspend fun saveProducto(producto: ProductoDto): Resource<Unit> {
        return try {
            val remoteProducto = remoteDataSource.addProducto(producto)
            productoDao.save(remoteProducto.toEntity())
            Resource.Success(Unit)
        } catch (e: HttpException) {
            Resource.Error("Error de conexión: ${e.message()}")
        } catch (e: Exception) {
            Resource.Error("Error desconocido: ${e.message}")
        }
    }

    suspend fun deleteProducto(id: Int): Resource<Unit> {
        return try {
            remoteDataSource.deleteProducto(id)
            productoDao.find(id)?.let { productoDao.delete(it) }
            Resource.Success(Unit)
        } catch (e: HttpException) {
            Resource.Error("Error de conexión: ${e.message()}")
        } catch (e: Exception) {
            productoDao.find(id)?.let { productoDao.delete(it) }
            Resource.Success(Unit)
        }
    }
}
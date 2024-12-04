package com.example.sharkstyle.data.repository

import com.example.sharkstyle.data.local.dao.CategoriaDao
import com.example.sharkstyle.data.local.dao.ProductoDao
import com.example.sharkstyle.data.local.entities.CategoriaEntity
import com.example.sharkstyle.data.local.entities.ProductoEntity
import com.example.sharkstyle.data.remote.RemoteDataSource
import com.example.sharkstyle.data.remote.dto.CategoriaDto
import com.example.sharkstyle.data.remote.dto.toEntity
import com.example.sharkstyle.data.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class CategoriaRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val categoriaDao: CategoriaDao,
    private val productoDao: ProductoDao
) {
    fun getCategorias(): Flow<Resource<List<CategoriaEntity>>> = flow {
        try {
            emit(Resource.Loading())

            val remoteCategorias = remoteDataSource.getCategorias()
            val localCategorias = remoteCategorias.map { it.toEntity() }
            localCategorias.forEach { categoriaDao.save(it) }

            emit(Resource.Success(localCategorias))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión: ${e.message()}"))
        } catch (e: Exception) {
            val localCategorias = categoriaDao.getAll().firstOrNull()
            if (localCategorias.isNullOrEmpty()) {
                emit(Resource.Error("No se encuentran datos locales"))
            } else {
                emit(Resource.Success(localCategorias))
            }
        }
    }

    fun getProductosByCategoria(categoriaId: Int): Flow<Resource<List<ProductoEntity>>> = flow {
        try {
            emit(Resource.Loading())

            val localProductos = productoDao.getProductosByCategoria(categoriaId)
            if (localProductos.isNotEmpty()) {
                emit(Resource.Success(localProductos))
            } else {
                emit(Resource.Error("No se encontraron productos para esta categoría"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Error desconocido: ${e.message}"))
        }
    }

    fun getCategoria(id: Int): Flow<Resource<CategoriaEntity>> = flow {
        try {
            emit(Resource.Loading())
            val localCategoria = categoriaDao.find(id)
            if (localCategoria != null) {
                emit(Resource.Success(localCategoria))
            } else {
                emit(Resource.Error("Categoría no encontrada"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Error desconocido: ${e.message}"))
        }
    }

    suspend fun saveCategoria(categoria: CategoriaDto): Resource<Unit> {
        return try {
            val remoteCategoria = remoteDataSource.addCategoria(categoria)
            categoriaDao.save(remoteCategoria.toEntity())
            Resource.Success(Unit)
        } catch (e: HttpException) {
            Resource.Error("Error de conexión: ${e.message()}")
        } catch (e: Exception) {
            Resource.Error("Error desconocido: ${e.message}")
        }
    }

    suspend fun deleteCategoria(id: Int): Resource<Unit> {
        return try {
            remoteDataSource.deleteCategoria(id)
            categoriaDao.find(id)?.let { categoriaDao.delete(it) }
            Resource.Success(Unit)
        } catch (e: HttpException) {
            Resource.Error("Error de conexión: ${e.message()}")
        } catch (e: Exception) {
            categoriaDao.find(id)?.let { categoriaDao.delete(it) }
            Resource.Success(Unit)
        }
    }
}

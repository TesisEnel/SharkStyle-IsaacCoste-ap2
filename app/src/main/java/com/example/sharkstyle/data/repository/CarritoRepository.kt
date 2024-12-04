package com.example.sharkstyle.data.repository

import com.example.sharkstyle.data.local.dao.CarritoDao
import com.example.sharkstyle.data.local.dao.DetalleCarritoDao
import com.example.sharkstyle.data.local.entities.CarritoEntity
import com.example.sharkstyle.data.local.entities.DetalleCarritoEntity
import com.example.sharkstyle.data.remote.RemoteDataSource
import com.example.sharkstyle.data.remote.dto.CarritoDto
import com.example.sharkstyle.data.remote.dto.toEntity
import com.example.sharkstyle.data.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class CarritoRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val carritoDao: CarritoDao,
    private val detalleCarritoDao: DetalleCarritoDao
) {
    fun getCarritos(): Flow<Resource<List<CarritoEntity>>> = flow {
        try {
            emit(Resource.Loading())

            val remoteCarritos = remoteDataSource.getCarritos()
            val localCarritos = remoteCarritos.map { it.toEntity() }
            localCarritos.forEach { carritoDao.save(it) }

            emit(Resource.Success(localCarritos))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión: ${e.message()}"))
        } catch (e: Exception) {
            val localCarritos = carritoDao.getAll().firstOrNull()
            if (localCarritos.isNullOrEmpty()) {
                emit(Resource.Error("No se encuentran datos locales"))
            } else {
                emit(Resource.Success(localCarritos))
            }
        }
    }
    suspend fun addCarritoDetalle(detalle: DetalleCarritoEntity) {
        detalleCarritoDao.insert(detalle)
    }

    suspend fun CarritoExiste(productoId: Int, carritoId: Int) {
        detalleCarritoDao.carritoDetalleExit(productoId, carritoId)
    }

    suspend fun getCarritoDetalleByProductoId(productoId: Int, carritoId: Int) =
        detalleCarritoDao.getCarritoDetalleByProductoId(productoId, carritoId)

    fun getCarritoDetallesPorCarritoId(carritoId: Int): Flow<List<DetalleCarritoEntity>> {
        return carritoDao.getCarritoDetallesPorCarritoId(carritoId)
    }
    suspend fun getLastCarritoByPersona(personaId: Int) =
        carritoDao.getLastCarritoByUsuario(personaId)

    suspend fun getCarritoNoPagadoPorUsuario(usuarioId: Int): CarritoEntity? {
        return carritoDao.getCarritoNoPagadoPorUsuario(usuarioId)
    }

//    fun getCarrito(id: Int): Flow<Resource<List<CarritoEntity>>> = flow {
//        try {
//            emit(Resource.Loading())
//            val localCarrito = carritoDao.find(id)
//            if (localCarrito != null) {
//                emit(Resource.Success(localCarrito))
//            } else {
//                emit(Resource.Error("Carrito no encontrado"))
//            }
//        } catch (e: Exception) {
//            emit(Resource.Error("Error desconocido: ${e.message}"))
//        }
//    }


    fun getCarritosPorUsuario(usuarioId: Int): Flow<Resource<List<CarritoEntity>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val carritos = carritoDao.getCarritosPorUsuario(usuarioId)
                emit(Resource.Success(carritos))
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: "Error al obtener carritos"))
            }
        }
    }

    suspend fun saveCarrito(carrito: CarritoEntity) = carritoDao.save(carrito)

    suspend fun addCarritoApi(carrito: CarritoDto) = remoteDataSource.addCarrito(carrito)

//    suspend fun deleteCarrito(id: Int): Resource<Unit> {
//        return try {
//            remoteDataSource.deleteCarrito(id)
//            carritoDao.find(id)?.let { carritoDao.delete(it) }
//            Resource.Success(Unit)
//        } catch (e: HttpException) {
//            Resource.Error("Error de conexión: ${e.message()}")
//        } catch (e: Exception) {
//            carritoDao.find(id)?.let { carritoDao.delete(it) }
//            Resource.Success(Unit)
//        }
//    }
}

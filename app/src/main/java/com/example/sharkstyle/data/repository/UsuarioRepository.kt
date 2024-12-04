package com.example.sharkstyle.data.repository

import android.util.Log
import com.example.sharkstyle.data.local.dao.UsuarioDao
import com.example.sharkstyle.data.local.entities.UsuarioEntity
import com.example.sharkstyle.data.remote.RemoteDataSource
import com.example.sharkstyle.data.remote.dto.UsuarioDto
import com.example.sharkstyle.data.remote.dto.toEntity
import com.example.sharkstyle.data.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class UsuarioRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val usuarioDao: UsuarioDao
) {
    suspend fun getUsuarioByCorreo(correo: String) = usuarioDao.getUsuarioByCorreo(correo)

    fun getUsuarios(): Flow<Resource<List<UsuarioEntity>>> = flow {
        try {
            emit(Resource.Loading())

            val usuariosRemotos = remoteDataSource.getUsuarios()
            val usuariosLocales = usuariosRemotos.map { it.toEntity() }

            usuariosLocales.forEach { usuarioDao.save(it) }
            emit(Resource.Success(usuariosLocales))
        } catch (e: HttpException) {
            Log.e("UsuarioRepository", "getUsuarios: ${e.message}")
            val usuariosLocales = usuarioDao.getAll().firstOrNull()
            if (usuariosLocales.isNullOrEmpty()) {
                emit(Resource.Error("Error de conexión y no hay datos locales disponibles"))
            } else {
                emit(Resource.Success(usuariosLocales))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Error desconocido: ${e.message}"))
        }
    }

    fun getUsuario(id: Int): Flow<Resource<UsuarioEntity>> = flow {
        try {
            emit(Resource.Loading())
            val usuarioLocal = usuarioDao.find(id)
            if (usuarioLocal != null) {
                emit(Resource.Success(usuarioLocal))
            } else {
                emit(Resource.Error("Usuario no encontrado localmente"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Error desconocido: ${e.message}"))
        }
    }

    fun save(usuario: UsuarioDto): Flow<Resource<UsuarioEntity>> = flow {
        try {
            emit(Resource.Loading())
            val usuarioApi = remoteDataSource.addUsuario(usuario)
            val usuarioRoom = usuarioApi.toEntity()
            usuarioDao.save(usuarioRoom)

            emit(Resource.Success(usuarioRoom))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión al guardar usuario: ${e.message()}"))
        } catch (e: Exception) {
            emit(Resource.Error("Error desconocido al guardar usuario: ${e.message}"))
        }
    }

    suspend fun delete(id: Int): Resource<Unit> {
        return try {
            remoteDataSource.deleteUsuario(id)
            usuarioDao.find(id)?.let { usuarioDao.delete(it) }
            Resource.Success(Unit)
        } catch (e: HttpException) {
            Log.e("UsuarioRepository", "delete: ${e.message}")
            Resource.Error("Error de conexión al eliminar usuario")
        } catch (e: Exception) {
            usuarioDao.find(id)?.let { usuarioDao.delete(it) }
            Resource.Success(Unit)
        }
    }
}

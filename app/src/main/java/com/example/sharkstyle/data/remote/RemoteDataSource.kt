package com.example.sharkstyle.data.remote

import com.example.sharkstyle.data.remote.dto.CarritoDto
import com.example.sharkstyle.data.remote.dto.CategoriaDto
import com.example.sharkstyle.data.remote.dto.CompraDto
import com.example.sharkstyle.data.remote.dto.ProductoDto
import com.example.sharkstyle.data.remote.dto.TallaDto
import com.example.sharkstyle.data.remote.dto.UsuarioDto
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
 private val sharkStyleApi: SharkStyleApi
){

    suspend fun getCarritos() = sharkStyleApi.getCarritos()
    suspend fun getCarrito(id: Int) = sharkStyleApi.getCarrito(id)
    suspend fun addCarrito(carrito: CarritoDto) = sharkStyleApi.addCarrito(carrito)
    suspend fun deleteCarrito(id: Int) = sharkStyleApi.deleteCarrito(id)


    suspend fun getCategorias() = sharkStyleApi.getCategorias()
    suspend fun getCategoria(id: Int) = sharkStyleApi.getCategoria(id)
    suspend fun addCategoria(categoria: CategoriaDto) = sharkStyleApi.addCategoria(categoria)
    suspend fun deleteCategoria(id: Int) = sharkStyleApi.deleteCategoria(id)


    suspend fun getCompras() = sharkStyleApi.getCompras()
    suspend fun getCompra(id: Int) = sharkStyleApi.getCompra(id)
    suspend fun addCompra(compra: CompraDto) = sharkStyleApi.addCompra(compra)
    suspend fun deleteCompra(id: Int) = sharkStyleApi.deleteCompra(id)


    suspend fun getProductos() = sharkStyleApi.getProductos()
    suspend fun getProducto(id: Int) = sharkStyleApi.getProducto(id)
    suspend fun addProducto(producto: ProductoDto) = sharkStyleApi.addProducto(producto)
    suspend fun deleteProducto(id: Int) = sharkStyleApi.deleteProducto(id)


    suspend fun getUsuarios() = sharkStyleApi.getUsuarios()
    suspend fun getUsuario(id: Int): UsuarioDto = sharkStyleApi.getUsuario(id)
    suspend fun addUsuario(usuario: UsuarioDto) = sharkStyleApi.addUsuario(usuario)
    suspend fun deleteUsuario(id: Int) = sharkStyleApi.deleteUsuario(id)


    suspend fun getTallas() = sharkStyleApi.getTallas()
    suspend fun getTalla(id: Int) = sharkStyleApi.getTalla(id)
    suspend fun addTallas(talla: TallaDto) = sharkStyleApi.addTalla(talla)
    suspend fun deleteTalla(id: Int) = sharkStyleApi.deleteTalla(id)
}
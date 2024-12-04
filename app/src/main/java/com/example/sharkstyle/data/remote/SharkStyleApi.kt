package com.example.sharkstyle.data.remote

import com.example.sharkstyle.data.remote.dto.CarritoDto
import com.example.sharkstyle.data.remote.dto.CategoriaDto
import com.example.sharkstyle.data.remote.dto.CompraDto
import com.example.sharkstyle.data.remote.dto.ProductoDto
import com.example.sharkstyle.data.remote.dto.TallaDto
import com.example.sharkstyle.data.remote.dto.UsuarioDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SharkStyleApi {
    @GET("api/Carritos")
    suspend fun getCarritos(): List<CarritoDto>
    @GET("api/Carritos/{id}")
    suspend fun getCarrito(@Path("id") id: Int): CarritoDto
    @POST("api/Carritos")
    suspend fun addCarrito(@Body carrito: CarritoDto): CarritoDto
    @DELETE("api/Carritos/{id}")
    suspend fun deleteCarrito(@Path("id") id: Int) : Response<Void?>

    @GET("api/Categorias")
    suspend fun getCategorias(): List<CategoriaDto>
    @GET("api/Categorias/{id}")
    suspend fun getCategoria(@Path("id") id: Int): CategoriaDto
    @POST("api/Categorias")
    suspend fun addCategoria(@Body categoria: CategoriaDto): CategoriaDto
    @DELETE("api/Categorias/{id}")
    suspend fun deleteCategoria(@Path("id") id: Int) : Response<Void?>

    @GET("api/Compras")
    suspend fun getCompras(): List<CompraDto>
    @GET("api/Compras/{id}")
    suspend fun getCompra(@Path("id") id: Int): CompraDto
    @POST("api/Compras")
    suspend fun addCompra(@Body compra: CompraDto): CompraDto
    @DELETE("api/Compras/{id}")
    suspend fun deleteCompra(@Path("id") id: Int) : Response<Void?>


    @GET("api/Productos")
    suspend fun getProductos(): List<ProductoDto>
    @GET("api/Productos/{id}")
    suspend fun getProducto(@Path("id") id: Int): ProductoDto
    @POST("api/Productos")
    suspend fun addProducto(@Body producto: ProductoDto): ProductoDto
    @DELETE("api/Productos/{id}")
    suspend fun deleteProducto(@Path("id") id: Int) : Response<Void?>

    @GET("api/Usuarios")
    suspend fun getUsuarios(): List<UsuarioDto>
    @GET("api/Usuarios/{id}")
    suspend fun getUsuario(@Path("id") id: Int): UsuarioDto
    @POST("api/Usuarios")
    suspend fun addUsuario(@Body usuario: UsuarioDto): UsuarioDto
    @DELETE("api/Usuarios/{id}")
    suspend fun deleteUsuario(@Path("id") id: Int) : Response<Void?>


    @GET("api/Tallas")
    suspend fun getTallas(): List<TallaDto>
    @GET("api/Tallas/{id}")
    suspend fun getTalla(@Path("id") id: Int): TallaDto
    @POST("api/Tallas")
    suspend fun addTalla(@Body talla: TallaDto): TallaDto
    @DELETE("api/Tallas/{id}")
    suspend fun deleteTalla(@Path("id") id: Int) : Response<Void?>

}
package com.example.sharkstyle.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.sharkstyle.data.converter.DetalleConverter
import com.example.sharkstyle.data.local.dao.CarritoDao
import com.example.sharkstyle.data.local.dao.CategoriaDao
import com.example.sharkstyle.data.local.dao.CompraDao
import com.example.sharkstyle.data.local.dao.DetalleCarritoDao
import com.example.sharkstyle.data.local.dao.DetalleCompraDao
import com.example.sharkstyle.data.local.dao.DetalleProductoDao
import com.example.sharkstyle.data.local.dao.ProductoDao
import com.example.sharkstyle.data.local.dao.UsuarioDao
import com.example.sharkstyle.data.local.dao.TallaDao
import com.example.sharkstyle.data.local.entities.CarritoEntity
import com.example.sharkstyle.data.local.entities.CategoriaEntity
import com.example.sharkstyle.data.local.entities.CompraEntity
import com.example.sharkstyle.data.local.entities.DetalleCarritoEntity
import com.example.sharkstyle.data.local.entities.DetalleCompraEntity
import com.example.sharkstyle.data.local.entities.DetalleProductoEntity
import com.example.sharkstyle.data.local.entities.ProductoEntity
import com.example.sharkstyle.data.local.entities.TallaEntity
import com.example.sharkstyle.data.local.entities.UsuarioEntity


@Database(
    entities = [
        ProductoEntity::class,
        CategoriaEntity::class,
        UsuarioEntity::class,
        CompraEntity::class,
        CarritoEntity::class,
        TallaEntity::class,
        DetalleCarritoEntity::class,
        DetalleCompraEntity::class,
        DetalleProductoEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DetalleConverter::class)
abstract class SharkStyleDb : RoomDatabase() {
    abstract fun productoDao(): ProductoDao
    abstract fun categoriaDao(): CategoriaDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun compraDao(): CompraDao
    abstract fun carritoDao(): CarritoDao
    abstract fun detalleCarritoDao(): DetalleCarritoDao
    abstract fun detalleCompraDao(): DetalleCompraDao
    abstract fun detalleProductoDao(): DetalleProductoDao
    abstract fun tallaDao(): TallaDao
}
package com.example.sharkstyle.di

import android.content.Context
import androidx.room.Room
import com.example.sharkstyle.data.local.database.SharkStyleDb
import com.example.sharkstyle.data.remote.SharkStyleApi
import com.google.firebase.auth.FirebaseAuth
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    private const val BASE_URL = "https://sharkstyleapi.azurewebsites.net/"
    @Provides
    @Singleton
    fun providesMoshi(): Moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    @Provides
    @Singleton
    fun providesSharkStyleApi(moshi: Moshi): SharkStyleApi {
       return Retrofit.Builder()
           .baseUrl(BASE_URL)
           .addConverterFactory(MoshiConverterFactory.create(moshi))
           .build()
           .create(SharkStyleApi::class.java)
    }

    @Provides
    @Singleton
    fun providefirebaseAuth() = FirebaseAuth.getInstance()


    @Provides
    @Singleton
    fun provideTareaDb(@ApplicationContext appContext: Context) =
        Room.databaseBuilder(
            appContext,
            SharkStyleDb::class.java,
            "SharkStyle.db"
        ).fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideProductoDao(sharkdb: SharkStyleDb) = sharkdb.productoDao()

    @Provides
    @Singleton
    fun provideCategoriaDao(sharkdb: SharkStyleDb) = sharkdb.categoriaDao()

    @Provides
    @Singleton
    fun provideUsuarioDao(sharkdb: SharkStyleDb) = sharkdb.usuarioDao()

    @Provides
    @Singleton
    fun provideCompraDao(sharkdb: SharkStyleDb) = sharkdb.compraDao()

    @Provides
    @Singleton
    fun provideCarritoDao(sharkdb: SharkStyleDb) = sharkdb.carritoDao()

    @Provides
    @Singleton
    fun provideDetalleCarritoDao(sharkdb: SharkStyleDb) = sharkdb.detalleCarritoDao()

    @Provides
    @Singleton
    fun provideDetalleCompraDao(sharkdb: SharkStyleDb) = sharkdb.detalleCompraDao()

    @Provides
    @Singleton
    fun provideDetalleProductoDao(sharkdb: SharkStyleDb) = sharkdb.detalleProductoDao()

    @Provides
    @Singleton
    fun provideTallaDao(sharkdb: SharkStyleDb) = sharkdb.tallaDao()

}
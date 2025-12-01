package com.example.loopieapp.Data

import com.example.loopieapp.Model.Producto
import com.example.loopieapp.Model.Usuario
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.Response

interface ApiService{
    // --- ENDPOINTS DE USUARIOS ---
    // GET /api/usuarios -> Obtiene todos los usuarios
    @GET("api/v1/users")
    suspend fun getAllUsers(): Response<List<Usuario>>

    // GET /api/usuarios/{id} -> Obtiene un usuario por su ID
    @GET("api/v1/users/{id}")
    suspend fun getUserById(@Path("id") id: Int): Response<Usuario>

    // GET /api/usuarios/correo/{correo} -> Obtiene un usuario por su CORREO
    @GET("api/v1/users/correo/{correo}")
    suspend fun obtenerUsuarioPorCorreo(@Path("correo") correo: String): Response<Usuario>

    // POST /api/usuarios -> Crea un nuevo usuario
    @POST("api/v1/users")
    suspend fun createUser(@Body user: Usuario): Response<Usuario>

    // PUT /api/usuarios/{id} -> Actualiza un usuario existente
    @PUT("api/v1/users/{id}")
    suspend fun updateUser(@Path("id") id: Int, @Body user: Usuario): Response<Usuario>

    // DELETE /api/usuarios/{id} -> Elimina un usuario
    @DELETE("api/v1/users/{id}")
    suspend fun deleteUser(@Path("id") id: Int): Response<Unit>


    // --- ENDPOINTS DE PRODUCTOS ---
    // GET /api/productos -> Obtiene todos los productos
    @GET("api/v1/products")
    suspend fun getAllProducts(): Response<List<Producto>>

    // GET /api/productos/{idProducto} -> Obtiene un producto por su ID
    @GET("api/v1/products/{idProducto}")
    suspend fun getProductById(@Path("idProducto") idProducto: Int): Response<Producto>

    // POST /api/productos -> Crea un nuevo producto
    @POST("api/v1/products")
    suspend fun createProduct(@Body product: Producto): Response<Producto>

    // PUT /api/productos/{idProducto} -> Actualiza un producto existente
    @PUT("api/v1/products/{idProducto}")
    suspend fun updateProduct(@Path("idProducto") idProducto: Int, @Body product: Producto): Response<Producto>

    @PUT("api/v1/users/{id}/change-password")
    suspend fun changePassword(
        @Path("id") id: Int,
        @Body request: ChangePasswordRequest): Response<Unit> // No esperamos que devuelva un cuerpo, solo un 200 OK

    // DELETE /api/productos/{idProducto} -> Elimina un producto
    @DELETE("api/v1/products/{idProducto}")
    suspend fun deleteProduct(@Path("idProducto") idProducto: Int): Response<Unit>

}
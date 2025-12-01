package com.example.loopieapp.Repository

import com.example.loopieapp.DAO.UsuarioDAO
import com.example.loopieapp.Model.Usuario
import com.example.loopieapp.Data.ApiService
import com.example.loopieapp.Data.ChangePasswordRequest
import com.example.loopieapp.Data.RetrofitClient
import com.example.loopieapp.Data.LoginRequest

class UsuarioRepository (
    //private val usuarioDAO: UsuarioDAO, //ya no es necesario pero se deja para logica mixta
    private val apiService: ApiService = RetrofitClient.instance
){
    //suspend fun obtenerUsuarios(): List<Usuario> = usuarioDAO.obtenerUsuarios()
    //suspend fun actualizarFotoPerfil(usuarioId: Int, uri: String?) = usuarioDAO.actualizarFotoPerfil(usuarioId, uri)
    //suspend fun insertar(usuario: Usuario) = usuarioDAO.insertar(usuario)
    //suspend fun eliminar(usuario: Usuario) = usuarioDAO.eliminar(usuario)

    // Ahora esta función llama a la API
    suspend fun iniciarSesion(loginRequest: LoginRequest): Usuario? {
        try {
            val response = apiService.getAllUsers()
            if (response.isSuccessful) {
                val todosLosUsuarios = response.body()
                return todosLosUsuarios?.find { it.correo == loginRequest.correo && it.clave == loginRequest.clave}
            }
        } catch (e: Exception) {
            // Manejar error de red
            e.printStackTrace()        }
        return null
    }

    // Llama a la API para registrar
    suspend fun registrarUsuario(usuario: Usuario): Usuario? {
        try {
            val response = apiService.createUser(usuario)
            if (response.isSuccessful) {
                return response.body()
            }
        } catch (e: Exception) {
            e.printStackTrace() //Maneja el error de red
        }
        return null
    }

    // Llama al endpoint GET /usuarios/{correo}
    suspend fun obtenerUsuarioPorCorreo(correo: String): Usuario? {
        try {
            // Si usas ID, sería apiService.getUserById(id).
            val response = apiService.obtenerUsuarioPorCorreo(correo)
            if (response.isSuccessful) {
                return response.body()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    // Llama al endpoint PUT /usuarios/{id}
    suspend fun actualizarUsuario(id: Int, usuario: Usuario): Usuario? {
        try {
            val response = apiService.updateUser(id, usuario)
            if (response.isSuccessful) {
                return response.body()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    // Llama al endpoint PUT /usuarios/{id}/change-password
    suspend fun cambiarContrasena(
        usuarioId: Int,
        claveActual: String,
        nuevaClave: String
    ): Boolean {
        return try {
            val request = ChangePasswordRequest(claveActual, nuevaClave)
            val response = apiService.changePassword(usuarioId, request)
            // La operación fue exitosa si el backend devuelve un código 2xx
            response.isSuccessful
        } catch (e: Exception) {
            e.printStackTrace()
            false // Si hay un error de red, asumimos que falló
        }
    }
}
package com.example.loopieapp.Repository

import com.example.loopieapp.DAO.UsuarioDAO
import com.example.loopieapp.Model.Usuario

class UsuarioRepository (private val usuarioDAO: UsuarioDAO){
    suspend fun obtenerUsuarios(): List<Usuario> = usuarioDAO.obtenerUsuarios()
    suspend fun actualizarFotoPerfil(usuarioId: Int, uri: String?) = usuarioDAO.actualizarFotoPerfil(usuarioId, uri)
    suspend fun insertar(usuario: Usuario) = usuarioDAO.insertar(usuario)
    suspend fun eliminar(usuario: Usuario) = usuarioDAO.eliminar(usuario)
}
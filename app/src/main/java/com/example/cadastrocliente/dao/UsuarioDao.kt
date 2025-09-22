package com.example.cadastrocliente.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cadastrocliente.models.Usuario

@Dao
interface UsuarioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserir(usuario: Usuario)

    @Query("SELECT * FROM usuario ORDER BY id ASC")
    suspend fun buscarTodos(): List<Usuario>

    @Query("SELECT * FROM usuario WHERE nome = :nome AND email = :email")
    suspend fun buscarPorNomeEmail(nome: String, email: String): Usuario?
}

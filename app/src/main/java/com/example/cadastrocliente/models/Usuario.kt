package com.example.cadastrocliente.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuario")
data class Usuario(
    @PrimaryKey val id: Int,
    val nome: String,
    val email: String
)

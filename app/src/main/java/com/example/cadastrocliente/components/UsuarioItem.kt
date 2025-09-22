package com.example.cadastrocliente.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cadastrocliente.models.Usuario

@Composable
fun UsuarioItem(usuario: Usuario) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text("ID: ${usuario.id}")
        Text("Nome: ${usuario.nome}")
        Text("Email: ${usuario.email}")
        Spacer(modifier = Modifier.height(8.dp))
    }
}

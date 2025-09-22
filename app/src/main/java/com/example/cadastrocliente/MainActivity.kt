package com.example.cadastrocliente

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.cadastrocliente.components.UsuarioItem
import com.example.cadastrocliente.db.AppDatabase
import com.example.cadastrocliente.models.Usuario
import com.example.cadastrocliente.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CadastroUsuariosScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CadastroUsuariosScreen() {
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var usuarios by remember { mutableStateOf<List<Usuario>>(emptyList()) }
    var erro by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val usuarioDao = db.usuarioDao()

    val coroutineScope = rememberCoroutineScope()

    // Carrega usuários da API + DB ao iniciar
    LaunchedEffect(Unit) {
        try {
            val usuariosDB = usuarioDao.buscarTodos()
            if (usuariosDB.isEmpty()) {
                val listaApi = RetrofitInstance.api.getUsuarios().take(10).map { apiUsuario ->
                    Usuario(apiUsuario.id, apiUsuario.name, apiUsuario.email)
                }
                listaApi.forEach { usuarioDao.inserir(it) }
            }
            usuarios = usuarioDao.buscarTodos()
        } catch (e: Exception) {
            erro = "Falha na conexão: ${e.message}"
            usuarios = usuarioDao.buscarTodos()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        TextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nome") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (nome.isNotBlank() && email.isNotBlank()) {
                    coroutineScope.launch(Dispatchers.IO) {
                        // Pega o próximo ID disponível
                        val novoId = (usuarioDao.buscarTodos().maxOfOrNull { it.id } ?: 0) + 1
                        val novoUsuario = Usuario(novoId, nome, email)
                        usuarioDao.inserir(novoUsuario)

                        val listaAtualizada = usuarioDao.buscarTodos()
                        // Atualiza a lista na UI
                        withContext(Dispatchers.Main) {
                            usuarios = listaAtualizada
                            nome = ""
                            email = ""
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cadastrar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (erro != null) {
            Text(erro!!, color = MaterialTheme.colorScheme.error)
        }

        LazyColumn {
            items(usuarios) { usuario ->
                UsuarioItem(usuario)
            }
        }
    }
}

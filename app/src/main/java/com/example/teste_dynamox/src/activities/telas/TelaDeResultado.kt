package com.example.teste_dynamox.src.activities.telas

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TelaDeResultado() {
    Column {
        Text(
            text = "Bem vindo à tela de resultado",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(25.dp)
        )
        Row() {
            Text("Respostas certas: ")
            Text("$contadorRespostasCertas")
        }
    }
}
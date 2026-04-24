package com.example.gentecomoagente

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.gentecomoagente.ui.navigation.AppNavigation
import com.example.gentecomoagente.ui.theme.GenteComoAgenteTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppNavigation()

        }
//        FirebaseApp.initializeApp(this)
//        val auth = FirebaseAuth.getInstance()
//
//        auth.createUserWithEmailAndPassword(
//            "teste@gentecomoagente.com",
//            "123456"
//        ).addOnCompleteListener {
//            if (it.isSuccessful) {
//                println("Usuário criado!")
//            } else {
//                println("Erro: ${it.exception?.message}")
//            }
//        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GenteComoAgenteTheme {
        Greeting("Android")
    }
}
package com.example.gentecomoagente

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import android.util.Log
import com.example.gentecomoagente.ui.navigation.AppNavigation
import com.example.gentecomoagente.ui.theme.GenteComoAgenteTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize Firestore metadata if missing
        initializeFirestoreMetadata()
        
        enableEdgeToEdge()
        setContent {
            AppNavigation()
        }
    }

    private fun initializeFirestoreMetadata() {
        val db = FirebaseFirestore.getInstance()
        val counterRef = db.collection("metadata").document("ticket_counter")
        
        counterRef.get().addOnSuccessListener { snapshot ->
            if (!snapshot.exists()) {
                Log.d("MainActivity", "Initializing ticket_counter metadata")
                counterRef.set(mapOf("lastId" to 0L))
            }
        }.addOnFailureListener { e ->
            Log.e("MainActivity", "Error checking metadata", e)
        }
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
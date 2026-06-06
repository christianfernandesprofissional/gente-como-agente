package com.example.gentecomoagente.ui.screens

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gentecomoagente.repository.AgentRepository
import com.example.gentecomoagente.repository.AuthRepository
import com.example.gentecomoagente.ui.components.CustomButton
import com.example.gentecomoagente.ui.components.CustomTextField
import com.example.gentecomoagente.ui.components.CustomTopHeader
import com.example.gentecomoagente.ui.components.showToast
import com.example.gentecomoagente.ui.navigation.Routes
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.example.gentecomoagente.R


@Composable
fun LoginGoogleScreen(navController: NavController) {

    val context = LocalContext.current

    val authRepository = remember { AuthRepository(context) }

    var isLoading by remember { mutableStateOf(false) }

    val googleSignInOptions = GoogleSignInOptions.Builder(

        GoogleSignInOptions.DEFAULT_SIGN_IN
    )
        .requestIdToken(
            context.getString(R.string.default_web_client_id)
        )
        .requestEmail()
        .build()

    val googleSignInClient = GoogleSignIn.getClient(
        context,
        googleSignInOptions
    )

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->

        Log.d("GOOGLE_LOGIN", "Launcher executado")

        try {

            val task = GoogleSignIn.getSignedInAccountFromIntent(
                result.data
            )

            Log.d("GOOGLE_LOGIN", "Task criada")

            val account = task.getResult(
                ApiException::class.java
            )

            Log.d(
                "GOOGLE_LOGIN",
                "Conta: ${account.email}"
            )

            val idToken = account.idToken

            Log.d(
                "GOOGLE_LOGIN",
                "Token nulo? ${idToken == null}"
            )

            if (idToken == null) {
                return@rememberLauncherForActivityResult
            }

            authRepository.loginWithGoogle(
                idToken = idToken,
                onSuccess = {
                    Log.d("GOOGLE_LOGIN", "Firebase OK")
                    navController.navigate(Routes.CLIENT_HOME)
                },
                onError = {
                    Log.e("GOOGLE_LOGIN", it)
                }
            )

        } catch (e: Exception) {

            Log.e(
                "GOOGLE_LOGIN",
                e.stackTraceToString()
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {



        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {


            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                CustomButton(
                    text = "Acessar como Funcionário",
                    onClick = { navController.navigate(Routes.LOGIN) }
                )

                // 🔥 BOTÃO LOGIN
                CustomButton(
                    text = if (isLoading)
                        "Entrando..."
                    else
                        "Login com Google",

                    iconPainter = painterResource(
                        id = R.drawable.icon_google
                    ),
                    onClick = {

                        isLoading = true

                        launcher.launch(
                            googleSignInClient.signInIntent
                        )
                    },

                    modifier = Modifier.fillMaxWidth(0.6f),

                    containerColor = Color(0xFFBDBDBD)
                )
            }
        }
    }
}
package com.example.sharkstyle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.sharkstyle.presentation.login.AuthViewModel
import com.example.sharkstyle.presentation.navigation.PruebaSharkStyleNavHost
import com.example.sharkstyle.ui.theme.SharkStyleTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val authViewModel : AuthViewModel by viewModels()
        setContent {
            SharkStyleTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PruebaSharkStyleNavHost(modifier = Modifier.padding(innerPadding), authViewModel = authViewModel , navController = navController)

                }
            }
        }
    }
}


package com.example.paycheck2paycheck

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.paycheck2paycheck.ui.presentation.screens.DashboardScreen
import com.example.paycheck2paycheck.ui.presentation.theme.Paycheck2PaycheckTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Paycheck2PaycheckTheme {
                DashboardScreen()
            }
        }
    }
}

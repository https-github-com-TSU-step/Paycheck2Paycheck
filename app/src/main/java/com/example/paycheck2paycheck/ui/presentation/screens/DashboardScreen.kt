package com.example.paycheck2paycheck.ui.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.paycheck2paycheck.ui.presentation.components.BottomMenu
import com.example.paycheck2paycheck.ui.presentation.components.DailyBudget
import com.example.paycheck2paycheck.ui.presentation.components.MainTopBar

@Composable
fun DashboardScreen() {
    var currentTab by remember { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            BottomMenu(
                currentTab = currentTab,
                onMainClick = { currentTab = 0 },
                onHistoryClick = { currentTab = 1 }
            )
        },
        floatingActionButton = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                FloatingActionButton(
                    onClick = { },
                    containerColor = Color.White,
                    contentColor = Color(0xFF1A73E8),
                    shape = CircleShape,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(Icons.Default.Mic, contentDescription = "Voice")
                }
                FloatingActionButton(
                    onClick = { },
                    containerColor = Color(0xFF1A73E8),
                    contentColor = Color.White,
                    shape = CircleShape,
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.White)
        ) {
            MainTopBar(
                currentDate = "24 Февраля",
                onSettingsClick = { }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                DailyBudget(
                    dailyBudget = "5600,00 ₽",
                    remainingAmount = "5189,5 ₽",
                    spentToday = "410,50 ₽"
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Stats Cards (Row)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    StatCard("СРЕДНИЙ НА ДЕНЬ", "5600,00 ₽", Modifier.weight(1f), Color(0xFFE3F2FD))
                    StatCard("УДАРНЫЕ ДНИ", "12 ДНЕЙ", Modifier.weight(1f), Color(0xFFFFF3E0))
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Recent Transactions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Недавние", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color(0xFF1F2937))
                    TextButton(onClick = { }) {
                        Text("См. все", color = Color(0xFF1A73E8))
                    }
                }

                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    item { TransactionItem("Шаурма", "Сегодня, 13:15", "350 ₽") }
                    item { TransactionItem("Молоко", "Сегодня, 8:30", "60,50 ₽") }
                }
            }
        }
    }
}

@Composable
fun StatCard(label: String, value: String, modifier: Modifier, iconBgColor: Color) {
    Surface(
        modifier = modifier.height(140.dp),
        color = Color(0xFFF9FAFB),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(iconBgColor, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                // Placeholder for icon
            }
            Column {
                Text(label, color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Text(value, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1F2937))
            }
        }
    }
}

@Composable
fun TransactionItem(name: String, time: String, amount: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFFF9FAFB),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(name, fontWeight = FontWeight.SemiBold, color = Color(0xFF1F2937))
                Text(time, color = Color.Gray, fontSize = 12.sp)
            }
            Text(amount, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
        }
    }
}

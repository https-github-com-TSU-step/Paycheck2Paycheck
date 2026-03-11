package com.example.paycheck2paycheck.ui.presentation.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.paycheck2paycheck.ui.presentation.components.BottomMenu
import com.example.paycheck2paycheck.ui.presentation.components.DailyBudget
import com.example.paycheck2paycheck.ui.presentation.components.MainTopBar
import com.example.paycheck2paycheck.ui.presentation.components.StatCard
import com.example.paycheck2paycheck.ui.presentation.components.StreakCard
import com.example.paycheck2paycheck.ui.presentation.components.TransactionItem
import com.example.paycheck2paycheck.ui.presentation.theme.Paycheck2PaycheckTheme

@Composable
fun DashboardScreen(
    state: DashboardState = DashboardState(),
    onAddExpenseClick: () -> Unit = {},
    onVoiceExpenseClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
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
                // Кнопка микрофона
                FloatingActionButton(
                    onClick = onVoiceExpenseClick,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.primary,
                    shape = CircleShape,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(Icons.Default.Mic, contentDescription = "Голосовой ввод")
                }
                // Кнопка добавления
                FloatingActionButton(
                    onClick = onAddExpenseClick,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = CircleShape,
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Добавить трату")
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            MainTopBar(
                currentDate = "24 Февраля",
                onSettingsClick = onSettingsClick
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .weight(1f)
            ) {
                DailyBudget(
                    dailyBudget = state.dailyBudget,
                    remainingAmount = state.remainingAmount,
                    spentToday = state.spentToday
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Карточки статистики
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    StatCard(
                        value = state.averageDaily,
                        modifier = Modifier.weight(1f)
                    )
                    StreakCard(
                        currentStreak = state.currentStreak,
                        bestStreak = state.bestStreak,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Заголовок "Недавние"
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Недавние",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    TextButton(onClick = { }) {
                        Text("См. все", color = MaterialTheme.colorScheme.primary)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))


                if (state.recentTransactions.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "НЕТ ДАННЫХ",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(state.recentTransactions.size) { index ->
                            val tx = state.recentTransactions[index]
                            TransactionItem(
                                name = tx.first,
                                time = "Сегодня", // Хардкод
                                amount = tx.second
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashboardScreenPreview() {

    val previewState = DashboardState(
        dailyBudget = "1500",
        remainingAmount = "900",
        spentToday = "600",
        averageDaily = "750",
        currentStreak = 5,
        bestStreak = 12,
        recentTransactions = listOf(
            "Кофе" to "250",
            "Продукты" to "1200",
            "Такси" to "430"
        )
    )

    Paycheck2PaycheckTheme {
        DashboardScreen(
            state = previewState
        )
    }
}


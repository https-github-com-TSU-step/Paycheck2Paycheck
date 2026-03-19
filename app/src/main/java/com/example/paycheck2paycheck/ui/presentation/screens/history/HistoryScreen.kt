package com.example.paycheck2paycheck.ui.presentation.screens.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.paycheck2paycheck.ui.presentation.components.BottomMenu
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    onMainClick: () -> Unit = {},
    onCreateBudgetClick: () -> Unit = {},
    viewModel: HistoryViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()

    val groupedExpenses = remember(state.expenses) {
        state.expenses
            .groupBy { it.date.toLocalDate() }
            .toSortedMap(compareByDescending { it })
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("История") },
                navigationIcon = {
                    IconButton(onClick = onMainClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Search, contentDescription = null)
                    }
                }
            )
        },
        bottomBar = {
            BottomMenu(
                currentTab = 1,
                onMainClick = onMainClick,
                onHistoryClick = {}
            )
        }
    ) { innerPadding ->
        if (state.hasLoadedOnce &&
            state.totalRemaining == "0,00 ₽" &&
            state.expenses.isEmpty() &&
            state.scheduledPayments.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "Бюджет не создан",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Создайте бюджет, чтобы\nначать отслеживать расходы",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = onCreateBudgetClick) {
                        Text("Создать бюджет")
                    }
                }
            }
        } else {

            Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    "ОСТАЛОСЬ ВСЕГО",
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    state.totalRemaining,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    textDecoration = TextDecoration.Underline,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    "ДНЕЙ ОСТАЛОСЬ",
                                    style = MaterialTheme.typography.labelSmall,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Text(
                                    state.daysLeft.toString(),
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        item {
                            Text(
                                "ЗАПЛАНИРОВАННЫЕ ПЛАТЕЖИ",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(vertical = 12.dp)
                            )
                        }

                        items(state.scheduledPayments) { payment ->
                            val formattedAmount = "- ${"%.2f ₽".format(payment.amount).replace(".", ",")}"
                            val formattedDate = payment.date.format(DateTimeFormatter.ofPattern("d MMM", Locale("ru")))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(payment.name, fontSize = 16.sp)
                                    Text(
                                        formattedDate,
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Text(
                                    formattedAmount,
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.error,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        groupedExpenses.forEach { (date, dayExpenses) ->
                            val title = when {
                                date == LocalDate.now() -> "СЕГОДНЯ"
                                date == LocalDate.now().minusDays(1) -> "ВЧЕРА"
                                else -> date.format(DateTimeFormatter.ofPattern("d MMMM", Locale("ru"))).uppercase()
                            }

                            item {
                                Text(
                                    title,
                                    style = MaterialTheme.typography.titleSmall,
                                    modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
                                )
                            }

                            items(dayExpenses) { expense ->
                                val formattedAmount = "- ${"%.2f ₽".format(expense.amount).replace(".", ",")}"
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(expense.name, fontSize = 16.sp)
                                    Text(
                                        formattedAmount,
                                        fontSize = 16.sp,
                                        color = MaterialTheme.colorScheme.error,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

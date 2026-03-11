package com.example.paycheck2paycheck.ui.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.paycheck2paycheck.ui.presentation.theme.Paycheck2PaycheckTheme

@Composable
fun DailyBudget(
    dailyBudget: String,
    remainingAmount: String,
    spentToday: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Бюджет на сегодня",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant // Серый цвет
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = dailyBudget,
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "ОСТАЛОСЬ",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = remainingAmount,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }


            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "ПОТРАЧЕНО",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = spentToday,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

// Превью для удобной верстки
@Preview(showBackground = true, backgroundColor = 0xFFF8FAFC)
@Composable
fun DailyBudgetPreview() {
    Paycheck2PaycheckTheme {
        DailyBudget(
            dailyBudget = "0,00 ₽",
            remainingAmount = "0,00 ₽",
            spentToday = "0,00 ₽"
        )
    }
}
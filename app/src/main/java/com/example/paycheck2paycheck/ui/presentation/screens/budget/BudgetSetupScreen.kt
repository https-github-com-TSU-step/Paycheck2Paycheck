package com.example.paycheck2paycheck.ui.presentation.screens.budget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.paycheck2paycheck.ui.presentation.components.AmountInputSection
import com.example.paycheck2paycheck.ui.presentation.components.CalculationInfoBanner
import com.example.paycheck2paycheck.ui.presentation.components.DatePickerField
import com.example.paycheck2paycheck.ui.presentation.components.PrimaryButton
import com.example.paycheck2paycheck.ui.presentation.components.SetupTopBar
import com.example.paycheck2paycheck.ui.presentation.theme.Paycheck2PaycheckTheme
import java.time.Instant
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetSetupScreen(
    onBackClick: () -> Unit,
    onSaveClick: (amount: String, startDate: String, endDate: String) -> Unit,
    viewModel: BudgetSetupViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    BudgetSetupContent(
        uiState = uiState,
        onAmountChange = viewModel::onAmountChanged,
        onStartDateClick = { showStartDatePicker = true },
        onEndDateClick = { showEndDatePicker = true },
        onBackClick = onBackClick,
        onSaveClick = {
            viewModel.saveBudget()
            onSaveClick(uiState.amount, uiState.startDate, uiState.endDate)
        }
    )

    if (showStartDatePicker) {
        BudgetDatePickerDialog(
            onDismiss = { showStartDatePicker = false },
            onConfirm = { millis ->
                val date = Instant.ofEpochMilli(millis)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                viewModel.onStartDateChanged(date)
                showStartDatePicker = false
            }
        )
    }

    if (showEndDatePicker) {
        BudgetDatePickerDialog(
            onDismiss = { showEndDatePicker = false },
            onConfirm = { millis ->
                val date = Instant.ofEpochMilli(millis)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                viewModel.onEndDateChanged(date)
                showEndDatePicker = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BudgetDatePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: (Long) -> Unit
) {
    val datePickerState = rememberDatePickerState()
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                datePickerState.selectedDateMillis?.let { onConfirm(it) }
            }) { Text("OK") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Отмена") }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
fun BudgetSetupContent(
    uiState: BudgetSetupUiState,
    onAmountChange: (String) -> Unit,
    onStartDateClick: () -> Unit,
    onEndDateClick: () -> Unit,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            SetupTopBar(onBackClick = onBackClick)
        },
        bottomBar = {
            PrimaryButton(
                text = "Сохранить бюджет",
                onClick = onSaveClick,
                modifier = Modifier.padding(16.dp),
                enabled = uiState.isSaveEnabled
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            AmountInputSection(
                amount = uiState.amount,
                onAmountChange = onAmountChange
            )

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "Срок",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                DatePickerField(
                    label = "Дата начала",
                    date = uiState.startDate,
                    onClick = onStartDateClick,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(16.dp))

                DatePickerField(
                    label = "Дата окончания",
                    date = uiState.endDate,
                    onClick = onEndDateClick,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            CalculationInfoBanner(
                days = uiState.days,
                dailyAmount = uiState.dailyAmount,
                error = uiState.amountError ?: uiState.dateError
            )
        }
    }
}

@Preview(showBackground = true, name = "Normal State")
@Composable
fun BudgetSetupScreenNormalPreview() {
    Paycheck2PaycheckTheme {
        BudgetSetupContent(
            uiState = BudgetSetupUiState(
                amount = "50 000",
                startDate = "21 Фев, 2026",
                endDate = "2 Мар, 2026",
                days = 7,
                dailyAmount = "7 142"
            ),
            onAmountChange = {},
            onStartDateClick = {},
            onEndDateClick = {},
            onBackClick = {},
            onSaveClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Empty State")
@Composable
fun BudgetSetupScreenEmptyPreview() {
    Paycheck2PaycheckTheme {
        BudgetSetupContent(
            uiState = BudgetSetupUiState(
                amount = "",
                startDate = "",
                endDate = "",
                days = 0,
                dailyAmount = ""
            ),
            onAmountChange = {},
            onStartDateClick = {},
            onEndDateClick = {},
            onBackClick = {},
            onSaveClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Amount Error State")
@Composable
fun BudgetSetupScreenAmountErrorPreview() {
    Paycheck2PaycheckTheme {
        BudgetSetupContent(
            uiState = BudgetSetupUiState(
                amount = "-2 000",
                startDate = "21 Фев, 2026",
                endDate = "2 Мар, 2026",
                amountError = "Ошибка в значении суммы, для расчета введите правильную сумму"
            ),
            onAmountChange = {},
            onStartDateClick = {},
            onEndDateClick = {},
            onBackClick = {},
            onSaveClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Date Error State")
@Composable
fun BudgetSetupScreenDateErrorPreview() {
    Paycheck2PaycheckTheme {
        BudgetSetupContent(
            uiState = BudgetSetupUiState(
                amount = "50 000",
                startDate = "2 Мар, 2026",
                endDate = "21 Фев, 2026",
                dateError = "Ошибка в значении срока, дата окончания не может быть равна или меньше даты начала"
            ),
            onAmountChange = {},
            onStartDateClick = {},
            onEndDateClick = {},
            onBackClick = {},
            onSaveClick = {}
        )
    }
}

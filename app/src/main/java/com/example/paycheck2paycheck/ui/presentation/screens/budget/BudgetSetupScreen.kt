package com.example.paycheck2paycheck.ui.presentation.screens.budget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.paycheck2paycheck.ui.presentation.components.AmountInputSection
import com.example.paycheck2paycheck.ui.presentation.components.CalculationInfoBanner
import com.example.paycheck2paycheck.ui.presentation.components.DatePickerField
import com.example.paycheck2paycheck.ui.presentation.components.PrimaryButton
import com.example.paycheck2paycheck.ui.presentation.components.SetupTopBar
import com.example.paycheck2paycheck.ui.presentation.theme.Paycheck2PaycheckTheme

@Composable
fun BudgetSetupScreen(
    onBackClick: () -> Unit,
    onSaveClick: (amount: String, startDate: String, endDate: String) -> Unit
) {
    // В реальном приложении это состояние будет в ViewModel
    var amount by remember { mutableStateOf("50 000") }
    var startDate by remember { mutableStateOf("21 Фев, 2026") }
    var endDate by remember { mutableStateOf("2 Мар, 2026") }
    
    // Простая логика для демонстрации состояний
    val isNegative = amount.contains("-")
    val amountError = if (isNegative) "Ошибка в значении суммы, для расчета введите правильную сумму" else null
    
    // Временная заглушка для ошибки даты (для демонстрации в превью)
    val isDateError = startDate == "2 Мар, 2026" && endDate == "21 Фев, 2026"
    val dateError = if (isDateError) "Ошибка в значении срока, дата окончания не может быть равна или меньше даты начала" else null

    val days = if (isDateError) 0 else 7
    val dailyAmount = try {
        val cleanAmount = amount.replace(" ", "").replace("₽", "")
        if (cleanAmount.isEmpty() || isNegative) "0" 
        else (cleanAmount.toLong() / 7).toString()
    } catch (e: Exception) {
        "0"
    }

    val uiState = BudgetSetupUiState(
        amount = amount,
        startDate = startDate,
        endDate = endDate,
        days = days,
        dailyAmount = dailyAmount,
        amountError = amountError,
        dateError = dateError
    )

    BudgetSetupContent(
        uiState = uiState,
        onAmountChange = { amount = it },
        onStartDateClick = { /* */ },
        onEndDateClick = { /* */ },
        onBackClick = onBackClick,
        onSaveClick = { onSaveClick(uiState.amount, uiState.startDate, uiState.endDate) }
    )
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
        containerColor = Color.White,
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
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
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

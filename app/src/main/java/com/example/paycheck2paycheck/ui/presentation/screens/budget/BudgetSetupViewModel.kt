package com.example.paycheck2paycheck.ui.presentation.screens.budget

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class BudgetSetupViewModel @Inject constructor() : ViewModel() {

    private val dateFormatter = DateTimeFormatter.ofPattern("d MMM, yyyy", Locale("ru"))

    private var startLocalDate: LocalDate = LocalDate.now()
    private var endLocalDate: LocalDate = LocalDate.now().plusDays(7)

    private val _uiState = MutableStateFlow(
        BudgetSetupUiState(
            startDate = LocalDate.now().format(DateTimeFormatter.ofPattern("d MMM, yyyy", Locale("ru"))),
            endDate = LocalDate.now().plusDays(7).format(DateTimeFormatter.ofPattern("d MMM, yyyy", Locale("ru"))),
            days = 7
        )
    )
    val uiState: StateFlow<BudgetSetupUiState> = _uiState.asStateFlow()

    fun onAmountChanged(value: String) {
        val error = validateAmount(value)
        val days = ChronoUnit.DAYS.between(startLocalDate, endLocalDate).toInt()
        val dailyAmount = if (error == null && value.isNotBlank() && days > 0) {
            val clean = value.replace(" ", "").replace(",", ".").toDoubleOrNull() ?: 0.0
            formatAmount(clean / days)
        } else ""
        _uiState.update { it.copy(amount = value, amountError = error, dailyAmount = dailyAmount) }
    }

    fun onStartDateChanged(date: LocalDate) {
        startLocalDate = date
        recalculate()
    }

    fun onEndDateChanged(date: LocalDate) {
        endLocalDate = date
        recalculate()
    }

    private fun recalculate() {
        val dateError = validateDates(startLocalDate, endLocalDate)
        val days = if (dateError == null) {
            ChronoUnit.DAYS.between(startLocalDate, endLocalDate).toInt()
        } else 0

        val amount = _uiState.value.amount
        val amountError = _uiState.value.amountError
        val dailyAmount = if (dateError == null && amountError == null && amount.isNotBlank() && days > 0) {
            val clean = amount.replace(" ", "").replace(",", ".").toDoubleOrNull() ?: 0.0
            formatAmount(clean / days)
        } else ""

        _uiState.update {
            it.copy(
                startDate = startLocalDate.format(dateFormatter),
                endDate = endLocalDate.format(dateFormatter),
                days = days,
                dailyAmount = dailyAmount,
                dateError = dateError
            )
        }
    }

    private fun validateAmount(value: String): String? {
        if (value.isBlank()) return null
        val clean = value.replace(" ", "").replace(",", ".")
        val number = clean.toDoubleOrNull()
            ?: return "Ошибка в значении суммы, для расчета введите правильную сумму"
        if (number <= 0) return "Ошибка в значении суммы, для расчета введите правильную сумму"
        return null
    }

    private fun validateDates(start: LocalDate, end: LocalDate): String? {
        if (!end.isAfter(start)) {
            return "Ошибка в значении срока, дата окончания не может быть равна или меньше даты начала"
        }
        return null
    }

    private fun formatAmount(amount: Double): String = String.format("%.0f", amount)
}

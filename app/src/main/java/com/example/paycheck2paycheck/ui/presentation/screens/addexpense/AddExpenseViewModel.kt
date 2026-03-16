package com.example.paycheck2paycheck.ui.presentation.screens.addexpense

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paycheck2paycheck.domain.model.ScheduledPayment
import com.example.paycheck2paycheck.domain.repository.BudgetRepository
import com.example.paycheck2paycheck.domain.repository.ScheduledPaymentRepository
import com.example.paycheck2paycheck.domain.usecase.AddExpenseManually
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddExpenseViewModel @Inject constructor(
    private val addExpenseManually: AddExpenseManually,
    private val scheduledPaymentRepository: ScheduledPaymentRepository, // ← добавили
    private val budgetRepository: BudgetRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AddExpenseState())
    val state: StateFlow<AddExpenseState> = _state.asStateFlow()

    private val dateFormatter = DateTimeFormatter.ofPattern("d MMM, yyyy", Locale("ru"))

    init {
        updateCurrentDateTime()
    }

    private fun updateCurrentDateTime() {
        val now = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("d MMMM, HH:mm", Locale("ru"))
        _state.update { it.copy(currentDateTime = now.format(formatter)) }
    }

    fun onAmountChanged(amount: String) {
        val filtered = amount.filter { it.isDigit() || it == '.' }
        _state.update { it.copy(amount = filtered) }
    }

    fun onDescriptionChanged(description: String) {
        _state.update { it.copy(description = description) }
    }

    fun onScheduledToggled(isScheduled: Boolean) {
        _state.update {
            val tomorrow = LocalDate.now().plusDays(1)
            it.copy(
                isScheduled = isScheduled,
                scheduledDate = if (isScheduled && it.scheduledDate == null) tomorrow else it.scheduledDate,
                scheduledDateFormatted = if (isScheduled && it.scheduledDate == null) {
                    tomorrow.format(dateFormatter)
                } else it.scheduledDateFormatted
            )
        }
    }

    fun onDateSelected(date: LocalDate) {
        _state.update {
            it.copy(
                scheduledDate = date,
                scheduledDateFormatted = date.format(dateFormatter)
            )
        }
    }

    fun addExpense() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                val amount = _state.value.amount.toDoubleOrNull()
                    ?: throw Exception("Введите корректную сумму")

                if (amount <= 0) throw Exception("Сумма должна быть больше 0")

                val description = _state.value.description.ifBlank {
                    throw Exception("Введите описание")
                }

                val budget = budgetRepository.getLatestBudget()
                    ?: throw Exception("Бюджет не найден. Создайте бюджет в настройках.")

                if (_state.value.isScheduled) {
                    val scheduledDate = _state.value.scheduledDate
                        ?: throw Exception("Выберите дату запланированного платежа")

                    val payment = ScheduledPayment(
                        id = UUID.randomUUID().toString(),
                        name = description,
                        amount = amount,
                        date = scheduledDate.atStartOfDay(), // можно .atTime(10, 0) если нужно время
                        budgetId = budget.id,
                        isPaid = false
                    )
                    scheduledPaymentRepository.save(payment)
                } else {
                    addExpenseManually.execute(amount, description, budget.id)
                }

                _state.update { it.copy(isLoading = false, isSuccess = true) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun reset() {
        _state.value = AddExpenseState()
        updateCurrentDateTime()
    }

    private fun formatDate(date: LocalDateTime): String {
        return date.format(DateTimeFormatter.ofPattern("d MMM, yyyy", Locale("ru")))
    }
}
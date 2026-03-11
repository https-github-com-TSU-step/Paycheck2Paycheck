import com.example.paycheck2paycheck.domain.repository.BudgetRepository
import com.example.paycheck2paycheck.domain.repository.ScheduledPaymentRepository
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import kotlin.math.max

class CalculateDailyLimitUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository,
    private val scheduledPaymentRepository: ScheduledPaymentRepository
) {
    suspend operator fun invoke(budgetId: String): Double {
        val budget = budgetRepository.getBudgetById(budgetId)
            ?: throw Exception("Бюджет не найден")

        val scheduledPayments = scheduledPaymentRepository.getByBudgetId(budgetId)

        val daysLeft = getRemainingDays(budget.startDate, budget.endDate)

        val unpaidAmount = scheduledPayments
            .filter { !it.isPaid }
            .sumOf { it.amount }

        val availableFunds = budget.remainingAmount - unpaidAmount

        return if (daysLeft > 0) availableFunds / daysLeft else availableFunds
    }

    private fun getRemainingDays(startDate: LocalDateTime, endDate: LocalDateTime): Int {
        val now = LocalDateTime.now()
        if (now.isAfter(endDate)) return 0
        if (now.isBefore(startDate)) {
            return ChronoUnit.DAYS.between(startDate.toLocalDate(), endDate.toLocalDate()).toInt()
        }
        return max(1, ChronoUnit.DAYS.between(now.toLocalDate(), endDate.toLocalDate()).toInt())
    }
}
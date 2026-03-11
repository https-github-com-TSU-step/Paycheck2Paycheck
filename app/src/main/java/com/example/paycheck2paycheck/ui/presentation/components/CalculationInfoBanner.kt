package com.example.paycheck2paycheck.ui.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CalculationInfoBanner(
    days: Int,
    dailyAmount: String,
    error: String? = null,
    modifier: Modifier = Modifier
) {
    val isError = error != null
    val backgroundColor = if (isError) Color(0xFFFEF2F2) else Color(0xFFF0F9FF)
    val iconTint = if (isError) Color.Red else Color(0xFF3B82F6)
    val icon = if (isError) Icons.Outlined.ErrorOutline else Icons.Outlined.Info

    val infoText = when {
        isError -> AnnotatedString(error!!)
        days > 0 && dailyAmount.isNotEmpty() -> {
            buildAnnotatedString {
                append("Выбранный период: ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("$days дней")
                }
                append(". Бюджет на день составит ")
                withStyle(style = SpanStyle(color = Color(0xFF3B82F6), fontWeight = FontWeight.Bold)) {
                    append("$dailyAmount ₽")
                }
                append(".")
            }
        }
        else -> AnnotatedString("Введите сумму и период для расчета ежедневного бюджета")
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor, shape = RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = iconTint
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = infoText,
            fontSize = 14.sp,
            color = if (isError) Color.Gray else Color.Gray, // В макете текст серый даже в ошибке
            lineHeight = 20.sp
        )
    }
}

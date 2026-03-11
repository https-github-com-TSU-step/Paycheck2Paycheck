package com.example.paycheck2paycheck.ui.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DatePickerField(
    label: String,
    date: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
        )
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .border(1.dp, Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = date.ifEmpty { "Выберите дату" },
                    fontSize = 14.sp,
                    color = if (date.isEmpty()) Color.LightGray else Color.Black,
                    fontWeight = if (date.isEmpty()) FontWeight.Normal else FontWeight.Medium
                )
            }
        }
    }
}

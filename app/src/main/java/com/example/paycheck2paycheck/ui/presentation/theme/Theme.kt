package com.example.paycheck2paycheck.ui.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val LightColorScheme = lightColorScheme(
    primary = ColorBtn,             // Главный цвет(синий)
    onPrimary = TextBtnColor,       // Цвет текста на главном цвете
    background = BackGroundColor,   // Цвет фона всего экрана
    onBackground = TextColor,       // Цвет основного текста на фоне
    surface = Color.White,          // Цвет поверхности карточек
    onSurface = TextColor,          // Цвет текста на карточках
    onSurfaceVariant = TextColor2,  // Вторичный текст, сервые подписи
    error = ColorError,             // Цвет для ошибок
    onError = Color.White           // Текст на фоне ошибки
)

@Composable
fun Paycheck2PaycheckTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
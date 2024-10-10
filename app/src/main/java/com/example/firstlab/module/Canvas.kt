package com.example.firstlab.module

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp

@Composable
fun SimpleGraph(states: List<String>, transitions: Map<Pair<String, Char>, Set<String>>) {
    val circleRadius = 50f
    val spacing = 200f

    Canvas(modifier = Modifier.fillMaxSize()) {
        // Определяем позиции состояний на экране
        val positions = calculatePositions(states.size, spacing)

        // Рисуем состояния (круги)
        states.forEachIndexed { index, state ->
            val position = positions[index]
            drawCircle(
                color = Color.Blue,
                radius = circleRadius,
                center = position
            )
            // Рисуем название состояния
            drawStateLabel(state, position)
        }

        // Рисуем переходы (линии между состояниями)
        transitions.forEach { (key, destinations) ->
            val fromState = key.first
            val symbol = key.second
            val fromIndex = states.indexOf(fromState)

            destinations.forEach { destination ->
                val toIndex = states.indexOf(destination)
                if (fromIndex != -1 && toIndex != -1) {
                    drawTransition(
                        positions[fromIndex],
                        positions[toIndex],
                        symbol.toString()
                    )
                }
            }
        }
    }
}

// Рассчитываем позиции для состояний
private fun calculatePositions(count: Int, spacing: Float): List<Offset> {
    val positions = mutableListOf<Offset>()
    for (i in 0 until count) {
        val x = spacing * (i % 3 + 1) // Для выравнивания по горизонтали
        val y = spacing * (i / 3 + 1) // Для выравнивания по вертикали
        positions.add(Offset(x, y))
    }
    return positions
}

// Функция для рисования меток состояний (названий)
private fun DrawScope.drawStateLabel(state: String, position: Offset) {
    drawContext.canvas.nativeCanvas.apply {
        drawText(
            state,
            position.x - 20f, // Чтобы текст был по центру
            position.y + 10f,
            android.graphics.Paint().apply {
                color = android.graphics.Color.WHITE
                textSize = 40f
            }
        )
    }
}

// Рисование линии перехода
private fun DrawScope.drawTransition(from: Offset, to: Offset, label: String) {
    drawLine(
        color = Color.Black,
        start = from,
        end = to,
        strokeWidth = 5f
    )

    // Рисуем символ перехода рядом с линией
    val midX = (from.x + to.x) / 2
    val midY = (from.y + to.y) / 2

    drawContext.canvas.nativeCanvas.apply {
        drawText(
            label,
            midX,
            midY,
            android.graphics.Paint().apply {
                color = android.graphics.Color.RED
                textSize = 40f
            }
        )
    }
}

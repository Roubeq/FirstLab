package com.example.firstlab.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firstlab.module.DKA
import com.example.firstlab.module.NKA
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {

    private var nka: NKA? = null
    private var dka: DKA? = null

    // LiveData для обновления UI
    val nkaState = mutableStateOf<NKA?>(null)
    val dkaState = mutableStateOf<DKA?>(null)

    // Функция для создания НКА
    fun createNKA(states: Set<String>, alphabet: Set<Char>, transitionsInput: String) {
        // Парсим переходы
        val transitions = parseTransitionsFromString(transitionsInput)

        // Проверка, что переходы не пустые
        if (transitions.isNotEmpty()) {
            nka = NKA(states, alphabet, transitions, "K", setOf("I")) // Начальное состояние "K", конечное состояние "I"
            nkaState.value = nka  // Обновляем состояние для UI
        }
    }

    // Метод для парсинга строк переходов
    private fun parseTransitionsFromString(input: String): Map<Pair<String, Char>, Set<String>> {
        val transitions = mutableMapOf<Pair<String, Char>, Set<String>>()
        val transitionLines = input.split(";").map { it.trim() }

        for (line in transitionLines) {
            val parts = line.split("->").map { it.trim() }
            if (parts.size == 2) {
                val left = parts[0]
                val right = parts[1].split(",").map { it.trim() }

                val leftParts = left.split(",").map { it.trim() }
                if (leftParts.size == 2) {
                    val state = leftParts[0]
                    val symbol = leftParts[1].singleOrNull() // Получаем единственный символ
                    if (symbol != null) {
                        transitions[Pair(state, symbol)] = right.toSet()
                    }
                }
            }
        }
        return transitions
    }

    // Функция преобразования НКА в ДКА
    fun convertNKAtoDKA() {
        nka?.let {
            viewModelScope.launch {
                withContext(Dispatchers.Default) {
                    val dkaResult = it.toDKA()
                    withContext(Dispatchers.Main) {
                        dka = dkaResult
                        dkaState.value = dka  // Обновляем состояние для UI
                    }
                }
            }
        }
    }

    // Функция для получения информации о НКА
    fun getNKAInfo(): String {
        return nka?.let {
            "States: ${it.states}\n" +
                    "Alphabet: ${it.alphabet}\n" +
                    "Transitions: ${it.transitions}\n" +
                    "Start State: ${it.startState}\n" +
                    "Final States: ${it.finalStates}"
        } ?: "NKA is not created yet."
    }

    // Функция для получения информации о ДКА
    fun getDKAInfo(): String {
        return dka?.let {
            "States: ${it.states}\n" +
                    "Alphabet: ${it.alphabet}\n" +
                    "Transitions: ${it.transitions}\n" +
                    "Start State: ${it.startState}\n" +
                    "Final States: ${it.finalStates}"
        } ?: "DKA is not created yet."
    }
}

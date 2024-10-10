package com.example.firstlab.module

data class DKA(
    val states: Set<String>,                              // Множество состояний ДКА
    val alphabet: Set<Char>,                              // Входной алфавит
    val transitions: Map<String, Map<Char, String>>,      // Функция переходов ДКА
    val startState: String,                               // Начальное состояние ДКА
    val finalStates: Set<String>                          // Заключительные состояния ДКА
)
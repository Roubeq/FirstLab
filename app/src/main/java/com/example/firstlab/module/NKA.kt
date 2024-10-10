package com.example.firstlab.module

class NKA(
    val states: Set<String>,                        // Множество состояний
    val alphabet: Set<Char>,                        // Алфавит (терминалы)
    val transitions: Map<Pair<String, Char>, Set<String>>, // Переходы
    val startState: String,                         // Начальное состояние (одна строка)
    val finalStates: Set<String>                    // Конечные состояния
) {

    fun transition(state: String, symbol: Char): Set<String> {
        return transitions[Pair(state, symbol)] ?: emptySet()  // Если перехода нет, вернуть пустое множество
    }

    fun isFinalState(state: String): Boolean {
        return finalStates.contains(state)
    }

    fun transition(states: Set<String>, symbol: Char): Set<String> {
        return states.flatMap { transition(it, symbol) }.toSet() // Преобразуем и собираем все возможные переходы
    }

    fun accepts(input: String): Boolean {
        var currentState = setOf(startState)  // Начальное состояние как множество
        for (symbol in input) {
            currentState = transition(currentState, symbol)
            if (currentState.isEmpty()) {
                return false  // Если нет переходов, автомат останавливается
            }
        }
        return currentState.any { isFinalState(it) }  // Проверка, есть ли среди текущих состояний хотя бы одно конечное
    }

    fun printTransitions() {
        for ((key, value) in transitions) {
            println("From state ${key.first} with symbol '${key.second}' -> ${value}")
        }
    }

    // Функция для преобразования НКА в ДКА
    fun toDKA(): DKA {
        // Шаг 1: Инициализация множеств состояний, переходов и очереди
        val dkaStates = mutableSetOf<Set<String>>()  // Множество состояний ДКА (множества состояний НКА)
        val dkaTransitions = mutableMapOf<String, MutableMap<Char, String>>()  // Переходы для ДКА
        val dkaFinalStates = mutableSetOf<String>() // Заключительные состояния ДКА
        val queue = mutableListOf<Set<String>>()    // Очередь для обработки состояний

        // Начальное состояние ДКА - это множество, содержащее начальное состояние НКА
        val startStateSet = setOf(startState)  // Множество начальных состояний
        queue.add(startStateSet)
        dkaStates.add(startStateSet)

        // Шаг 2: Обработка каждого состояния из очереди
        while (queue.isNotEmpty()) {
            val currentSet = queue.removeAt(0)  // Извлекаем текущее состояние
            val currentSetName = currentSet.joinToString(",")  // Преобразуем в строку

            // Инициализируем таблицу переходов для текущего состояния
            if (!dkaTransitions.containsKey(currentSetName)) {
                dkaTransitions[currentSetName] = mutableMapOf()
            }

            // Шаг 3: Обрабатываем каждый символ алфавита для текущего состояния
            for (symbol in alphabet) {
                val newSet = mutableSetOf<String>()  // Новое состояние как множество

                // Для каждого состояния в текущем множестве находим возможные переходы
                for (state in currentSet) {
                    val nextStates = transitions[Pair(state, symbol)]  // Теперь используем правильный ключ для переходов
                    if (nextStates != null) {
                        newSet.addAll(nextStates)
                    }
                }

                // Если есть переходы для данного символа
                if (newSet.isNotEmpty()) {
                    val newStateName = newSet.joinToString(",")
                    dkaTransitions[currentSetName]!![symbol] = newStateName  // Добавляем переход

                    // Если новое состояние еще не обработано, добавляем его в очередь
                    if (!dkaStates.contains(newSet)) {
                        dkaStates.add(newSet)
                        queue.add(newSet)
                    }

                    // Проверяем, является ли новое состояние заключительным
                    if (newSet.any { finalStates.contains(it) }) {
                        dkaFinalStates.add(newStateName)
                    }
                }
            }
        }

        // Шаг 4: Возвращаем ДКА
        val dkaStateNames = dkaStates.map { it.joinToString(",") }.toSet()  // Преобразуем в строку
        return DKA(
            states = dkaStateNames,
            alphabet = alphabet,
            transitions = dkaTransitions,
            startState = startStateSet.joinToString(","),  // Начальное состояние
            finalStates = dkaFinalStates  // Заключительные состояния
        )
    }

}

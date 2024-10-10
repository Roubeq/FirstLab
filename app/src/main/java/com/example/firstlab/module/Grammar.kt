package com.example.firstlab.module

class Grammar(
    val terminals: Set<Char>, //терминалы
    val nonTerminals: Set<String>, //нетерминалы
    val rules: Set<Rule>, //правила
    val startSymbol: String //начальный символ
) {
    fun isRegular(): Boolean {
        // Правила регулярной грамматики могут быть следующими:
        // 1. A -> aB (где A - нетерминал, a - терминал, B - нетерминал)
        // 2. A -> a (где A - нетерминал, a - терминал)
        // 3. A -> ε (где A - нетерминал) - допускается, если A - начальный символ
        for (rule in rules) {
            if (isValidRule(rule))
                return true
        }
        return false
    }

    private fun isValidRule(rule: Rule): Boolean {
        val left = rule.left
        val right = rule.right

        if (left.length != 1 || !nonTerminals.contains(left)) {
            return false
        }
        return when {
            // Если правило вида A -> aB
            right.length > 1 -> {
                terminals.contains(right[0]) && nonTerminals.contains(right[1].toString())
            }
            // Если правило вида A -> a
            right.length == 1 -> {
                terminals.contains(right[0])
            }
            // Если правило вида A -> ε
            right.isEmpty() && left == startSymbol -> {
                true // допускается только для начального символа
            }
            else -> false
        }
    }
}
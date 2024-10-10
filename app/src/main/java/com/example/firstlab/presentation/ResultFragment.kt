package com.example.firstlab.presentation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.ComposeView
import androidx.navigation.fragment.findNavController
import com.example.firstlab.R

class ResultFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_result, container, false)

        // Получаем параметры из аргументов
        val param1 = arguments?.getString("param1") ?: ""
        val param2 = arguments?.getString("param2") ?: ""

        Log.d("ResultFragment", "Received param1: $param1")
        Log.d("ResultFragment", "Received param2: $param2")

        // Устанавливаем содержимое ComposeView
        val composeView = view.findViewById<ComposeView>(R.id.composeView)
        composeView.setContent {
            ResultFragmentScreen(param1, param2)
        }

        return view
    }

    @Composable
    fun ResultFragmentScreen(states: String, transitionsInput: String) {
        val statesList = states.split(",").map { it.trim() }
        val transitions = parseTransitionsFromString(transitionsInput)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "NKA States:")
            statesList.forEach { state ->
                Text(text = state)
            }

            Text(text = "Transitions:")
            transitions.forEach { (key, value) ->
                Text(text = "${key.first} --> ${key.second} --> ${value.joinToString(", ")}")
            }
        }
    }

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
}
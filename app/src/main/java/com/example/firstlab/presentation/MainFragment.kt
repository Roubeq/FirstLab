package com.example.firstlab.presentation

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.ComposeView
import androidx.navigation.fragment.findNavController
import com.example.firstlab.R
import com.example.firstlab.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        // Установка содержимого ComposeView
        binding.composeView.setContent {
            MainFragmentScreen(onSubmit = { states, alphabet, transitions ->
                viewModel.createNKA(states, alphabet, transitions)

                // Переход на ResultFragment
                val action = Bundle().apply {
                    putString("param1", states.joinToString(","))
                    putString("param2", transitions) // Передаем переходы как строку
                }
                findNavController().navigate(R.id.resultFragment, action)
            })
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @Composable
    fun MainFragmentScreen(onSubmit: (Set<String>, Set<Char>, String) -> Unit) {
        var statesInput by remember { mutableStateOf("") }
        var alphabetInput by remember { mutableStateOf("") }
        var transitionsInput by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "Enter Grammar for NKA")

            // Поля для ввода данных пользователем
            TextField(
                value = statesInput,
                onValueChange = { statesInput = it },
                label = { Text("States (comma separated)") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = alphabetInput,
                onValueChange = { alphabetInput = it },
                label = { Text("Alphabet (comma separated)") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = transitionsInput,
                onValueChange = { transitionsInput = it },
                label = { Text("Transitions (format: state, symbol -> state; ...") },
                modifier = Modifier.fillMaxWidth()
            )

            // Кнопка для создания НКА
            Button(
                onClick = {
                    val states = statesInput.split(",").map { it.trim() }.toSet()
                    val alphabet = alphabetInput.split(",").map { it.trim().single() }.toSet()
                    // Передаем переходы в виде строки
                    onSubmit(states, alphabet, transitionsInput) // Передаем transitionsInput
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Create NKA")
            }
        }
    }
}

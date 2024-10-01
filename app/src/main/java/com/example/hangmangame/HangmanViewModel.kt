package com.example.hangmangame
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class HangmanViewModel : ViewModel() {
    private val wordHints = mapOf(
        "APPLE" to "Food",
        "DOG" to "Animal",
        "PENCIL" to "Stationery"
    )

    var currentWord = mutableStateOf(wordHints.keys.random()) // Initialize with a random word if needed
    var currentHint = mutableStateOf(currentWord.value.let { wordHints[it] }!!)
    var hintLeft = mutableStateOf(3)
    var remainingTurns = mutableStateOf(6)
    var hintMessage = mutableStateOf("")
    var disabledLetters = mutableStateOf<List<Char>>(emptyList())
    var correctLetters = mutableStateOf<List<Char>>(emptyList())

    fun resetGame() {
        currentWord.value = wordHints.keys.random()
        currentHint.value = wordHints[currentWord.value]!!
        hintLeft.value = 3
        remainingTurns.value = 6
        correctLetters.value = emptyList()
        disabledLetters.value = emptyList()
        hintMessage.value = ""
    }
}

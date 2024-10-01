package com.example.hangmangame

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hangmangame.ui.theme.HangmanGameTheme
import kotlin.reflect.KProperty

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HangmanGameTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Hangman(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Hangman(modifier: Modifier = Modifier) {
    // Game state variables
    val wordHints = mapOf(
        "APPLE" to "Food",
        "DOG" to "Animal",
        "PENCIL" to "Stationery"
    )
    var currentWord by remember { mutableStateOf(wordHints.keys.random()) } // Random word
    var currentHint by remember { mutableStateOf(wordHints[currentWord]!!) }
    var hintLeft by remember { mutableStateOf(3) }
    var remainingTurns by remember { mutableStateOf(6) }
    var hintMessage by remember { mutableStateOf("") }
    var disabledLetters by remember { mutableStateOf<List<Char>>(emptyList()) }
    var correctLetters by remember { mutableStateOf<List<Char>>(emptyList()) }
    val context = LocalContext.current

    // This function handles letter selection
    val onLetterSelected: (Char) -> Unit = { selectedLetter ->
        if (selectedLetter in currentWord) {
            correctLetters = correctLetters + selectedLetter
        } else {
            remainingTurns--
        }
        // Add the selected letter to disabledLetters
        disabledLetters = disabledLetters + selectedLetter
    }

    // Hint button logic
    // Make sure to specify the return type explicitly
    val onHintClicked: () -> Unit = {
        when (hintLeft) {
            3 -> {
                hintMessage = currentHint
                hintLeft--
            }
            2 -> {
                if (remainingTurns > 1) {       //half of remaining incorrect letters
                    disabledLetters = disabledLetters + disableHalfIncorrectLetters(('A'..'Z').toList(), currentWord)
                    remainingTurns--
                    hintLeft--
                } else {
                    Toast.makeText(context, "Sorry, a hint would result in a loss", Toast.LENGTH_SHORT).show()
                }
            }
            1 -> {
                if (remainingTurns > 1) {       //vowels
                    // Show vowels and add them to correct letters
                    val vowels = showVowels(('A'..'Z').toList())
                    val vowelsInWord = vowels.filter { it in currentWord }

                    // Add the vowels that are in the word to correctLetters
                    correctLetters = correctLetters + vowelsInWord

                    // Disable these vowels
                    disabledLetters = disabledLetters + vowels

                    remainingTurns--
                    hintLeft--
                } else {
                    Toast.makeText(context, "Sorry, a hint would result in a loss", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                Toast.makeText(context, "No hints left", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Reset game logic
    val resetGame = {
        remainingTurns = 6
        hintLeft = 3
        correctLetters = emptyList()
        disabledLetters = emptyList()
        currentWord = wordHints.keys.random()
        currentHint = wordHints[currentWord]!!
        hintMessage = ""
    }

    // Detect current orientation
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT

    if (isPortrait) {
        Column(
            modifier = modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Portrait Mode - Guess the Word!")
            Spacer(modifier = Modifier.padding(16.dp))
            Button(onClick = resetGame) {
                Text(text = "Reset Game")
            }
            Panel1(
                remainingLetters = ('A'..'Z').toList(),
                disabledLetters = disabledLetters,
                onLetterSelected = onLetterSelected
            )
            Panel3(
                remainingTurns = remainingTurns,
                currentWord = currentWord,
                correctLetters = correctLetters,
                disabledLetters = disabledLetters
            )
        }
    } else {
        Row(
            modifier = modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Panel1(
                    remainingLetters = ('A'..'Z').toList(),
                    disabledLetters = disabledLetters,
                    onLetterSelected = onLetterSelected
                )
                Panel2(
                    hintLeft = hintLeft,
                    remainingTurns = remainingTurns, // Keep this passed in Panel2
                    onHintClicked = onHintClicked,
                    hintMessage = hintMessage
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.width(16.dp)) // Space between panels
                Panel3(
                    remainingTurns = remainingTurns,
                    currentWord = currentWord,
                    correctLetters = correctLetters,
                    disabledLetters = disabledLetters
                )
            }
        }
    }
}

//private operator fun Any.getValue(nothing: Nothing?, property: KProperty<*>): Any {
//    TODO("Not yet implemented")
//}

@Composable
fun Panel1(
    remainingLetters: List<Char>, // This will be a list of all letters A-Z
    disabledLetters: List<Char>, // This will be a list of letters that are disabled after being selected
    onLetterSelected: (Char) -> Unit // This will be a function to handle letter selection
) {
    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "CHOOSE A LETTER", fontSize = 18.sp, modifier = Modifier.padding(bottom = 8.dp))

        // First row: A-F
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            remainingLetters.subList(0, 6).forEach { letter ->
                LetterButton(letter = letter, disabledLetters = disabledLetters, onLetterSelected = onLetterSelected)
            }
        }

        Spacer(modifier = Modifier.height(3.dp)) // Space between rows

        // Second row: G-L
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            remainingLetters.subList(6, 12).forEach { letter ->
                LetterButton(letter = letter, disabledLetters = disabledLetters, onLetterSelected = onLetterSelected)
            }
        }

        Spacer(modifier = Modifier.height(3.dp)) // Space between rows

        // Third row: M-R
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            remainingLetters.subList(12, 18).forEach { letter ->
                LetterButton(letter = letter, disabledLetters = disabledLetters, onLetterSelected = onLetterSelected)
            }
        }

        Spacer(modifier = Modifier.height(3.dp)) // Space between rows

        // Fourth row: S-X
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            remainingLetters.subList(18, 24).forEach { letter ->
                LetterButton(letter = letter, disabledLetters = disabledLetters, onLetterSelected = onLetterSelected)
            }
        }

        Spacer(modifier = Modifier.height(3.dp)) // Space between rows

        // Fifth row: Y-Z
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            remainingLetters.subList(24, 26).forEach { letter ->
                LetterButton(letter = letter, disabledLetters = disabledLetters, onLetterSelected = onLetterSelected)
            }
        }
    }
}

// Helper function for Panel1
@Composable
fun LetterButton(letter: Char, disabledLetters: List<Char>, onLetterSelected: (Char) -> Unit) {
    Button(
        onClick = { onLetterSelected(letter) }, // Handle letter selection
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier
            .size(38.dp) // Size of the button
            .padding(2.dp), // Padding around the button
        contentPadding = PaddingValues(0.dp), // Remove default content padding
        enabled = !disabledLetters.contains(letter) // Disable the button if the letter has been selected
    ) {
        Text(text = letter.toString(), fontSize = 16.sp)
    }
}

// Helper function for Panel2
fun showVowels(remainingLetters: List<Char>): List<Char> {
    val vowels = listOf('A', 'E', 'I', 'O', 'U')
    val vowelsToShow = remainingLetters.filter { it in vowels }
    return vowelsToShow
}

fun disableHalfIncorrectLetters(remainingLetters: List<Char>, currentWord: String): List<Char> {
    val incorrectLetters = remainingLetters.filter { it !in currentWord }
    val lettersToDisableCount = incorrectLetters.size / 2
    val lettersToDisable = incorrectLetters.shuffled().take(lettersToDisableCount)
    return lettersToDisable
}

@Composable
fun Panel2(
    hintLeft: Int,
    remainingTurns: Int,
    onHintClicked: () -> Unit,
    hintMessage: String
) {
    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1.5f)
        ) {
            Text(text = "Hints left: $hintLeft", fontSize = 16.sp)
            Text(text = "Remaining Turns: $remainingTurns", fontSize = 16.sp)  // Display remainingTurns
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            Button(onClick = onHintClicked) {
                Text(text = "Use Hint")
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1.5f)
        ) {
            Text(text = "Hint: $hintMessage", fontSize = 16.sp)
        }
    }
}


@Composable
fun Panel3(
    remainingTurns: Int,
    currentWord: String,
    correctLetters: List<Char>, // Pass correct letters guessed
    disabledLetters: List<Char> // Pass disabled letters
) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Hangman Drawing Here")
        val imageResId = when (remainingTurns) {
            6 -> R.drawable.hangman6
            5 -> R.drawable.hangman5
            4 -> R.drawable.hangman4
            3 -> R.drawable.hangman3
            2 -> R.drawable.hangman2
            1 -> R.drawable.hangman1
            0 -> R.drawable.hangman0
            else -> R.drawable.hangman0 // Default case for when the player loses
        }

        Image(
            painter = painterResource(id = imageResId),
            contentDescription = null,
            modifier = Modifier.size(150.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        WordDisplay(currentWord = currentWord, correctLetters = correctLetters)
    }
}

// Helper function for Panel3
@Composable
fun WordDisplay(currentWord: String, correctLetters: List<Char>) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        for (char in currentWord) {
            Text(
                text = if (correctLetters.contains(char)) char.toString() else "_", // Display correct letters
                fontSize = 32.sp,
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}

@Preview(
    showSystemUi = true,
    showBackground = true,
    device = "spec:width=411dp,height=891dp,dpi=420, isRound=false,chinSize=0dp,orientation=landscape"
)
@Composable
fun HangmanPreview() {
    HangmanGameTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Hangman(
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

//@Preview(
//    showSystemUi = true,
//    showBackground = true,
//    device = "spec:width=411dp,height=891dp,dpi=420, isRound=false,chinSize=0dp,orientation=portrait"
//)
//@Composable
//fun HangmanPreviewPortrait() {
//    HangmanGameTheme {
//        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//            Hangman(
//                modifier = Modifier.padding(innerPadding)
//            )
//        }
//    }
//}

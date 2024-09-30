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
    // Game state variable
    val wordHints = mapOf(
        "APPLE" to "Food",
        "DOG" to "Animal",
        "PENCIL" to "Stationery",
    )
    var currentWord by remember { mutableStateOf(wordHints.keys.random()) } // Select a random word
    var currentHint by remember { mutableStateOf(wordHints[currentWord]!!) } // Get corresponding hint
    var hintLeft by remember { mutableStateOf(3) }
    var remainingTurns by remember { mutableStateOf(11) }
    var hintMessage by remember { mutableStateOf("") } // State variable for the hint message
    var showHint by remember { mutableStateOf(false) } // Flag to show hint
    val context = LocalContext.current

    // Temporary placeholder for remaining letters until Panel 1 is complete
    var remainingLetters by remember { mutableStateOf(('A'..'Z').toList()) }
    var disabledLetters by remember { mutableStateOf<List<Char>>(emptyList()) }

    // Detect the current orientation using LocalConfiguration
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT

    if (isPortrait) {
        // Portrait layout
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Buttons and layout for Portrait Mode
            Text(text = "Portrait Mode - Guess the Word!")
            Spacer(modifier = Modifier.padding(16.dp))
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Reset Game")
            }
        }
    } else {
        // Landscape layout
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Layout for Landscape Modd

            Panel1() // Letter selection panel
            Spacer(modifier = Modifier.width(16.dp)) // Space between panels
            Panel2(
                hintLeft = hintLeft,
                remainingTurns = remainingTurns,
                onHintClicked = {
                    when (hintLeft) {
                        3 -> {
                            hintMessage = currentHint // Set the hint message
                            showHint = true // Show the hint
                            hintLeft--
                        } // Show the hint on the first click
                        2 -> {
                            if (remainingTurns > 1) {
                                disabledLetters = disableHalfIncorrectLetters(remainingLetters, currentWord)
                                remainingTurns--
                                hintLeft--
                            }
                        }
                        1 -> {
                            if (remainingTurns > 1) {
                                showVowels(remainingLetters) // Implement this function
                                remainingTurns--
                                hintLeft--
                            }
                        }
                        else -> {
                            Toast.makeText(context, "Hint not available", Toast.LENGTH_SHORT).show()
                        }
                    }

                },
                remainingLetters = remainingLetters, // Pass the temporary remaining letters
                hintMessage = hintMessage,
                disabledLetters = disabledLetters // Pass the disabled letters
            ) // Hint button panel
            Column (
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.width(16.dp)) // Space between panels
                Panel3(
                    remainingTurns = remainingTurns,
                ) // Main game play panel
            }
        }
    }
}

private operator fun Any.getValue(nothing: Nothing?, property: KProperty<*>): Any {
    TODO("Not yet implemented")
}

@Composable
fun Panel1() {
    // Letter selection panel: A - J buttons in a grid with two rows
    Column(
        modifier = Modifier
            .padding(8.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "CHOOSE A LETTER", fontSize = 18.sp, modifier = Modifier.padding(bottom = 8.dp))

        // First row: A-f
        Row(
            modifier = Modifier,
//                .fillMaxWidth()
//                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            LetterButton("A")
            LetterButton("B")
            LetterButton("C")
            LetterButton("D")
            LetterButton("E")
            LetterButton("F")
        }

        Spacer(modifier = Modifier.height(3.dp)) // Space between rows

        // Second row: h-l
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            LetterButton("G")
            LetterButton("H")
            LetterButton("I")
            LetterButton("J")
            LetterButton("K")
            LetterButton("L")
        }

        Spacer(modifier = Modifier.height(3.dp)) // Space between rows

        // Third row: m-r
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            LetterButton(
                letter = "M",
                modifier = Modifier
                    .weight(1f)
            )
            LetterButton("N")
            LetterButton("O")
            LetterButton("P")
            LetterButton("Q")
            LetterButton("R")

        }

        Spacer(modifier = Modifier.height(3.dp)) // Space between rows

        // Fourth row: s-x
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            LetterButton("S")
            LetterButton("T")
            LetterButton("U")
            LetterButton("V")
            LetterButton("W")
            LetterButton("X")
        }

        Spacer(modifier = Modifier.height(3.dp)) // Space between rows

        // Fifth row: y-z
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            LetterButton("Y")
            LetterButton("Z")
        }
    }
}

// Helper funcition for Panel1

@Composable
fun LetterButton(letter: String, modifier: Modifier = Modifier) {
    Button(
        onClick = { /* Handle letter selection */ },
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier
            .size(38.dp) // Slightly smaller button size for compact and visibility
            .padding(2.dp), // Reduce padding for a tighter layout
        contentPadding = PaddingValues(0.dp) // Remove default content padding
    ) {
        Text(text = letter, fontSize = 16.sp) // Adjust font size for better fit
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
    remainingLetters: List<Char>,
    disabledLetters: List<Char>, // Add this parameter
    hintMessage: String
) {
    Column(
        modifier = Modifier
            .padding(8.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = onHintClicked) {
            Text("Hint")
        } // Hint button
        Text(text = "$hintLeft hints left")
        Text(text = "Hint: $hintMessage")
    }
    // Create buttons for each remaining letter


    
}



@Composable
fun Panel3(
    remainingTurns: Int
) {
    // Main game play panel (Hangman and word display placeholder)
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Hangman Drawing Here")
        val imageResId = when (remainingTurns) {
            11 -> R.drawable.hangman11
            10 -> R.drawable.hangman10
            9 -> R.drawable.hangman9
            8 -> R.drawable.hangman8
            7 -> R.drawable.hangman7
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
        Text(text = "_ _ _ _ _")  // Placeholder for the word
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

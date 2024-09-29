package com.example.hangmangame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hangmangame.ui.theme.HangmanGameTheme

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
            // Layout for Landscape Mode
            Column (
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Panel1() // Letter selection panel
                Spacer(modifier = Modifier.width(16.dp)) // Space between panels
                Panel2() // Hint button panel
            }
            Column (
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.width(16.dp)) // Space between panels
                Panel3() // Main game play panel
            }
        }
    }
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

@Composable
fun Panel2() {
    // Hint button panel
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "HINT: FOOD")
        Button(
            onClick = { /*TODO*/ },
            contentPadding = PaddingValues(0.dp) // Remove default content padding
        ) {
            Text(text = "Use Hint")
        }
    }
}

@Composable
fun Panel3() {
    // Main game play panel (Hangman and word display placeholder)
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Hangman Drawing Here")
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

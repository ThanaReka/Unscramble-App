package com.example.unscramble.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.unscramble.data.MAX_NO_OF_WORDS
import com.example.unscramble.data.SCORE_INCREASE
import com.example.unscramble.data.allWords
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/*
 * StateFlow is a data holder observable flow that emits the current and new state updates.
 * A StateFlow can be exposed from the GameUiState so that the composables can listen for UI state
 * updates and make the screen state survive configuration changes.
 */

class GameViewModel: ViewModel() {
    // Game UI state

    private val _uiState = MutableStateFlow(GameUiState())

    // Backing property to avoid state updates from other classes
    //    val uiState: StateFlow<GameUiState>
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()
    // The asStateFlow() makes this mutable state flow a read-only state flow.

    //  property to save the current scrambled word
    private lateinit var currentWord: String

    // property to serve as a mutable set to store used words in the game.
    private var usedWords: MutableSet<String> = mutableSetOf()

    var userGuess by mutableStateOf("")
        private set

    // helper method to pick a random word from the list and shuffle it.
    private fun pickRandomWordAndShuffle(): String {
        // Continue picking up a new random word until you get one that hasn't been used before
        currentWord = allWords.random()
        if (usedWords.contains(currentWord)) {
            return pickRandomWordAndShuffle()
        } else {
            usedWords.add(currentWord)
            return shuffleCurrentWord(currentWord)
        }
    }

    //  helper method to shuffle the current word (takes a String and returns the shuffled String)
    private fun shuffleCurrentWord(word: String): String {
        val tempWord = word.toCharArray()
        // Scramble the word
        tempWord.shuffle()
        while (String(tempWord).equals(word)) {
            tempWord.shuffle()
        }
        return String(tempWord)
    }

    // helper function to initialize the game by clearing all the words in the usedWords set,
    // and initializing the _uiState by picking a new word for currentScrambledWord using pickRandomWordAndShuffle().
    fun resetGame() {
        usedWords.clear()
        _uiState.value = GameUiState(currentScrambledWord = pickRandomWordAndShuffle())
    }

    init {
        resetGame()
    }

    /*
     * updateUserGuess() takes the user's guess word as a String argument and updates the userGuess
     * with the passed in guessedWord
     */
    fun updateUserGuess(guessedWord: String) {
        userGuess = guessedWord
    }

    // add an if else block to verify if the user's guess is the same as the currentWord.
    fun checkUserGuess() {

        if (userGuess.equals(currentWord, ignoreCase = true)) {
            // User's guess is correct, increase the score
            // and call updateGameState() to prepare the game for next round
            val updatedScore = _uiState.value.score.plus(SCORE_INCREASE)
            updateGameState(updatedScore)
        } else {
            // User's guess is wrong, show an error
            _uiState.update { currentState ->
                currentState.copy(isGuessedWordWrong = true)
            }
            // Reset user guess
            updateUserGuess("")
        }
    }

    // update the score, increment the current word count and pick a new word from the WordsData.kt file.
    private fun updateGameState(updatedScore: Int) {
        if (usedWords.size == MAX_NO_OF_WORDS) {
            //Last round in the game
            _uiState.update { currentState ->
                currentState.copy(
                    isGuessedWordWrong = false,
                    score = updatedScore,
                    isGameOver = true
                )
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(
                    isGuessedWordWrong = false,
                    currentScrambledWord = pickRandomWordAndShuffle(),
                    score = updatedScore,
                    currentWordCount = currentState.currentWordCount.inc(),
                )
            }
        }
    }

    fun skipWord() {
        updateGameState(_uiState.value.score)
        // Reset user guess
        updateUserGuess("")
    }


}
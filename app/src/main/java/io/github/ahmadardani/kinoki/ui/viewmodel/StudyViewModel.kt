package io.github.ahmadardani.kinoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.ahmadardani.kinoki.data.model.Card
import io.github.ahmadardani.kinoki.data.repository.DeckRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class CardFace { FRONT, CENTER, BACK }
enum class DrillStatus { IDLE, CORRECT, WRONG }

class StudyViewModel(private val repository: DeckRepository) : ViewModel() {

    private val _cards = MutableStateFlow<List<Card>>(emptyList())
    val cards: StateFlow<List<Card>> = _cards.asStateFlow()

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()

    private val _currentFace = MutableStateFlow(CardFace.FRONT)
    val currentFace: StateFlow<CardFace> = _currentFace.asStateFlow()

    private val _quizScore = MutableStateFlow(0)
    val quizScore: StateFlow<Int> = _quizScore.asStateFlow()

    private val _quizOptions = MutableStateFlow<List<String>>(emptyList())
    val quizOptions: StateFlow<List<String>> = _quizOptions.asStateFlow()

    private val _isFinished = MutableStateFlow(false)
    val isFinished: StateFlow<Boolean> = _isFinished.asStateFlow()

    private val _userDrillAnswer = MutableStateFlow("")
    val userDrillAnswer: StateFlow<String> = _userDrillAnswer.asStateFlow()

    private val _drillStatus = MutableStateFlow(DrillStatus.IDLE)
    val drillStatus: StateFlow<DrillStatus> = _drillStatus.asStateFlow()

    private val _wrongCardIds = mutableSetOf<String>()

    private val _wrongCardCount = MutableStateFlow(0)
    val wrongCardCount: StateFlow<Int> = _wrongCardCount.asStateFlow()

    fun loadSession(deckId: String, isShuffle: Boolean) {
        viewModelScope.launch {
            val allDecks = repository.getAllDecks()
            val deck = allDecks.find { it.id == deckId }
            if (deck != null) {
                _cards.value = if (isShuffle) deck.cards.shuffled() else deck.cards

                _wrongCardIds.clear()
                _wrongCardCount.value = 0

                resetSessionState()

                if (isShuffle && _cards.value.isNotEmpty()) {
                    generateQuizOptions()
                }
            }
        }
    }


    fun retryWrongCards() {

        val wrongCardsList = _cards.value.filter { it.id in _wrongCardIds }

        if (wrongCardsList.isNotEmpty()) {
            _cards.value = wrongCardsList

            _wrongCardIds.clear()
            _wrongCardCount.value = 0

            resetSessionState()
        }
    }

    private fun resetSessionState() {
        _currentIndex.value = 0
        _currentFace.value = CardFace.FRONT
        _isFinished.value = false
        _quizScore.value = 0

        _userDrillAnswer.value = ""
        _drillStatus.value = DrillStatus.IDLE
    }

    fun onCardTap() {
        val currentCard = _cards.value.getOrNull(_currentIndex.value) ?: return

        when (_currentFace.value) {
            CardFace.FRONT -> {
                if (!currentCard.center.isNullOrBlank()) {
                    _currentFace.value = CardFace.CENTER
                } else {
                    _currentFace.value = CardFace.BACK
                }
            }
            CardFace.CENTER -> {
                _currentFace.value = CardFace.BACK
            }
            CardFace.BACK -> {
            }
        }
    }

    fun nextCard() {
        if (_currentIndex.value < _cards.value.size - 1) {
            _currentIndex.value += 1
            _currentFace.value = CardFace.FRONT

            _userDrillAnswer.value = ""
            _drillStatus.value = DrillStatus.IDLE

            if (_quizOptions.value.isNotEmpty()) {
                generateQuizOptions()
            }
        } else {
            _isFinished.value = true
        }
    }

    fun prevCard() {
        if (_currentIndex.value > 0) {
            _currentIndex.value -= 1
            _currentFace.value = CardFace.FRONT
            _userDrillAnswer.value = ""
            _drillStatus.value = DrillStatus.IDLE
        }
    }

    fun submitAnswer(answer: String) {
        val correctAnswer = _cards.value[_currentIndex.value].back
        if (answer == correctAnswer) {
            _quizScore.value += 1
        }
        nextCard()
    }

    fun updateDrillInput(input: String) {
        _userDrillAnswer.value = input
        if (_drillStatus.value != DrillStatus.IDLE) {
            _drillStatus.value = DrillStatus.IDLE
        }
    }

    fun checkDrillAnswer() {
        val currentCard = _cards.value.getOrNull(_currentIndex.value) ?: return

        val targetAnswer = if (!currentCard.center.isNullOrBlank()) currentCard.center else currentCard.back
        val input = _userDrillAnswer.value.trim()

        if (input.isBlank()) return

        if (input.equals(targetAnswer, ignoreCase = true)) {
            _drillStatus.value = DrillStatus.CORRECT

        } else {
            _drillStatus.value = DrillStatus.WRONG

            if (!_wrongCardIds.contains(currentCard.id)) {
                _wrongCardIds.add(currentCard.id)
                _wrongCardCount.value = _wrongCardIds.size
            }
        }
    }

    private fun generateQuizOptions() {
        val currentCard = _cards.value[_currentIndex.value]
        val correctAnswer = currentCard.back

        val distractors = _cards.value
            .filter { it.id != currentCard.id }
            .map { it.back }
            .shuffled()
            .take(3)

        _quizOptions.value = (distractors + correctAnswer).shuffled()
    }
}
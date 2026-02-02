package io.github.ahmadardani.kinoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.ahmadardani.kinoki.data.model.Deck
import io.github.ahmadardani.kinoki.data.repository.DeckRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DeckDetailViewModel(private val repository: DeckRepository) : ViewModel() {

    private val _deck = MutableStateFlow<Deck?>(null)
    val deck: StateFlow<Deck?> = _deck.asStateFlow()

    fun loadDeck(deckId: String) {
        viewModelScope.launch {
            val allDecks = repository.getAllDecks()
            _deck.value = allDecks.find { it.id == deckId }
        }
    }
}
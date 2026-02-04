package io.github.ahmadardani.kinoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.ahmadardani.kinoki.data.model.Deck
import io.github.ahmadardani.kinoki.data.repository.DeckRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: DeckRepository) : ViewModel() {

    private val _decks = MutableStateFlow<List<Deck>>(emptyList())
    val decks: StateFlow<List<Deck>> = _decks.asStateFlow()

    init {
        loadDecks()
    }

    fun loadDecks() {
        viewModelScope.launch {
            _decks.value = repository.getAllDecks()
        }
    }

    fun importDeck(jsonString: String) {
        viewModelScope.launch {
            repository.importDeckFromJson(jsonString)
            loadDecks()
        }
    }

    fun createDeck(title: String) {
        viewModelScope.launch {
            repository.createNewDeck(title)
            loadDecks()
        }
    }

    fun deleteDeck(deckId: String) {
        viewModelScope.launch {
            repository.deleteDeck(deckId)
            loadDecks()
        }
    }
}
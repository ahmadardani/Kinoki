package io.github.ahmadardani.kinoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.ahmadardani.kinoki.data.repository.DeckRepository
import kotlinx.coroutines.launch

class AddCardViewModel(private val repository: DeckRepository) : ViewModel() {

    fun addCard(deckId: String, front: String, back: String, center: String?) {
        viewModelScope.launch {
            repository.addCardToDeck(deckId, front, back, center)
        }
    }
}
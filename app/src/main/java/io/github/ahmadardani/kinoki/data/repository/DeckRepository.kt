package io.github.ahmadardani.kinoki.data.repository

import android.content.Context
import io.github.ahmadardani.kinoki.data.model.Card
import io.github.ahmadardani.kinoki.data.model.Deck
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

@kotlin.OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
class DeckRepository(private val context: Context) {

    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    suspend fun getAllDecks(): List<Deck> = withContext(Dispatchers.IO) {
        val files = context.filesDir.listFiles { _, name -> name.endsWith(".json") }

        files?.mapNotNull { file ->
            try {
                val content = file.readText()
                json.decodeFromString<Deck>(content)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }?.sortedByDescending { it.createdAt } ?: emptyList()
    }

    suspend fun saveDeck(deck: Deck) = withContext(Dispatchers.IO) {
        val filename = "${deck.id}.json"
        val file = File(context.filesDir, filename)

        val jsonString = json.encodeToString(deck)
        file.writeText(jsonString)
    }

    suspend fun importDeckFromJson(jsonString: String) = withContext(Dispatchers.IO) {
        try {
            val deck = json.decodeFromString<Deck>(jsonString)
            saveDeck(deck)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun deleteDeck(deckId: String) = withContext(Dispatchers.IO) {
        val filename = "$deckId.json"
        val file = File(context.filesDir, filename)
        if (file.exists()) {
            file.delete()
        }
    }

    suspend fun addCardToDeck(deckId: String, front: String, back: String, center: String?) = withContext(Dispatchers.IO) {
        val filename = "$deckId.json"
        val file = File(context.filesDir, filename)

        if (file.exists()) {
            try {
                val content = file.readText()
                val targetDeck = json.decodeFromString<Deck>(content)

                val newCard = Card(
                    front = front,
                    back = back,
                    center = if (center.isNullOrBlank()) null else center
                )

                targetDeck.cards.add(0, newCard)
                val jsonString = json.encodeToString(targetDeck)
                file.writeText(jsonString)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun createNewDeck(title: String) = withContext(Dispatchers.IO) {
        val newDeck = Deck(title = title)
        saveDeck(newDeck)
    }

    suspend fun updateDeckTitle(deckId: String, newTitle: String) = withContext(Dispatchers.IO) {
        val filename = "$deckId.json"
        val file = File(context.filesDir, filename)

        if (file.exists()) {
            try {
                val content = file.readText()
                val targetDeck = json.decodeFromString<Deck>(content)
                val updatedDeck = targetDeck.copy(title = newTitle)

                val jsonString = json.encodeToString(updatedDeck)
                file.writeText(jsonString)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun editCardInDeck(deckId: String, updatedCard: Card) = withContext(Dispatchers.IO) {
        val filename = "$deckId.json"
        val file = File(context.filesDir, filename)

        if (file.exists()) {
            try {
                val content = file.readText()
                val targetDeck = json.decodeFromString<Deck>(content)

                val cardIndex = targetDeck.cards.indexOfFirst { it.id == updatedCard.id }

                if (cardIndex != -1) {
                    targetDeck.cards[cardIndex] = updatedCard

                    val jsonString = json.encodeToString(targetDeck)
                    file.writeText(jsonString)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun deleteCardFromDeck(deckId: String, cardId: String) = withContext(Dispatchers.IO) {
        val filename = "$deckId.json"
        val file = File(context.filesDir, filename)

        if (file.exists()) {
            try {
                val content = file.readText()
                val targetDeck = json.decodeFromString<Deck>(content)

                val wasRemoved = targetDeck.cards.removeAll { it.id == cardId }

                if (wasRemoved) {
                    val jsonString = json.encodeToString(targetDeck)
                    file.writeText(jsonString)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
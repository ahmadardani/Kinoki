package io.github.ahmadardani.kinoki.data.repository

import android.content.Context
import io.github.ahmadardani.kinoki.data.model.Card
import io.github.ahmadardani.kinoki.data.model.Deck
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

    fun getAllDecks(): List<Deck> {
        val files = context.filesDir.listFiles { _, name -> name.endsWith(".json") }

        return files?.mapNotNull { file ->
            try {
                val content = file.readText()
                json.decodeFromString<Deck>(content)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }?.sortedByDescending { it.createdAt } ?: emptyList()
    }

    fun saveDeck(deck: Deck) {
        val filename = "${deck.id}.json"
        val file = File(context.filesDir, filename)

        val jsonString = json.encodeToString(deck)
        file.writeText(jsonString)
    }

    fun deleteDeck(deckId: String) {
        val filename = "$deckId.json"
        val file = File(context.filesDir, filename)
        if (file.exists()) {
            file.delete()
        }
    }

    fun addCardToDeck(deckId: String, front: String, back: String, center: String?) {
        val decks = getAllDecks()
        val targetDeck = decks.find { it.id == deckId }

        if (targetDeck != null) {
            val newCard = Card(
                front = front,
                back = back,
                center = if (center.isNullOrBlank()) null else center
            )

            targetDeck.cards.add(0, newCard)
            saveDeck(targetDeck)
        }
    }

    fun createNewDeck(title: String) {
        val newDeck = Deck(title = title)
        saveDeck(newDeck)
    }
}
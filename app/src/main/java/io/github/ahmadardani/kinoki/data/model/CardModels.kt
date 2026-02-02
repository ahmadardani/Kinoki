package io.github.ahmadardani.kinoki.data.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable
import java.util.UUID

@OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
@Serializable
data class Card(
    val id: String = UUID.randomUUID().toString(),
    val front: String,
    val back: String,
    val center: String? = null
)

@OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
@Serializable
data class Deck(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val cards: MutableList<Card> = mutableListOf(),
    val createdAt: Long = System.currentTimeMillis()
)
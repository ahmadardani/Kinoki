package io.github.ahmadardani.kinoki.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.ahmadardani.kinoki.data.repository.DeckRepository

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(DeckRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
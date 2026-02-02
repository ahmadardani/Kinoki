package io.github.ahmadardani.kinoki.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.ahmadardani.kinoki.data.repository.DeckRepository

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    private val repository = DeckRepository(context)

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(AddCardViewModel::class.java)) {
            return AddCardViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(DeckDetailViewModel::class.java)) {
            return DeckDetailViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(StudyViewModel::class.java)) {
            return StudyViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
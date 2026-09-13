package com.example.shopitemsdemo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ShoppingUiState(
    val items: List<ShoppingItem> = emptyList(),
    val filter: ItemFilter = ItemFilter.ALL,
) {
    val visibleItems: List<ShoppingItem>
        get() = when (filter) {
            ItemFilter.ALL -> items
            ItemFilter.TO_BUY -> items.filterNot(ShoppingItem::purchased)
            ItemFilter.DONE -> items.filter(ShoppingItem::purchased)
        }
    val remainingCount: Int get() = items.count { !it.purchased }
    val purchasedCount: Int get() = items.count(ShoppingItem::purchased)
}

class ShoppingViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ShoppingRepository(application)
    private val _uiState = MutableStateFlow(ShoppingUiState(items = repository.load()))
    val uiState: StateFlow<ShoppingUiState> = _uiState.asStateFlow()

    fun addItem(rawName: String): Boolean {
        val name = rawName.trim().replace(Regex("\\s+"), " ")
        if (name.isBlank()) return false
        changeItems { current ->
            current + ShoppingItem(
                id = (current.maxOfOrNull(ShoppingItem::id) ?: 0L) + 1L,
                name = name,
            )
        }
        return true
    }

    fun toggleItem(id: Long) = changeItems { items ->
        items.map { if (it.id == id) it.copy(purchased = !it.purchased) else it }
    }

    fun deleteItem(id: Long): ShoppingItem? {
        val deleted = _uiState.value.items.firstOrNull { it.id == id } ?: return null
        changeItems { items -> items.filterNot { it.id == id } }
        return deleted
    }

    fun restoreItem(item: ShoppingItem) = changeItems { items ->
        if (items.any { it.id == item.id }) items else items + item
    }

    fun clearPurchased(): List<ShoppingItem> {
        val deleted = _uiState.value.items.filter(ShoppingItem::purchased)
        changeItems { items -> items.filterNot(ShoppingItem::purchased) }
        return deleted
    }

    fun restoreItems(items: List<ShoppingItem>) = changeItems { current ->
        (current + items).distinctBy(ShoppingItem::id).sortedBy(ShoppingItem::id)
    }

    fun setFilter(filter: ItemFilter) = _uiState.update { it.copy(filter = filter) }

    private fun changeItems(transform: (List<ShoppingItem>) -> List<ShoppingItem>) {
        _uiState.update { state -> state.copy(items = transform(state.items)) }
        repository.save(_uiState.value.items)
    }
}

package com.example.shopitemsdemo

import org.junit.Assert.assertEquals
import org.junit.Test

class ShoppingUiStateTest {
    private val items = listOf(
        ShoppingItem(1, "Milk"),
        ShoppingItem(2, "Bread", purchased = true),
    )

    @Test
    fun countsReflectPurchasedState() {
        val state = ShoppingUiState(items = items)

        assertEquals(1, state.remainingCount)
        assertEquals(1, state.purchasedCount)
    }

    @Test
    fun filtersOnlyReturnMatchingItems() {
        assertEquals(listOf("Milk"), ShoppingUiState(items, ItemFilter.TO_BUY).visibleItems.map { it.name })
        assertEquals(listOf("Bread"), ShoppingUiState(items, ItemFilter.DONE).visibleItems.map { it.name })
        assertEquals(items, ShoppingUiState(items, ItemFilter.ALL).visibleItems)
    }

    @Test
    fun itemNamesAreTrimmedAndWhitespaceIsCollapsed() {
        assertEquals("Green apples", normalizeItemName("  Green   apples  "))
    }
}

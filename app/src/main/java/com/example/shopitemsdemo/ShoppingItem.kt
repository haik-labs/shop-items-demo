package com.example.shopitemsdemo

data class ShoppingItem(
    val id: Long,
    val name: String,
    val purchased: Boolean = false,
)

enum class ItemFilter(val label: String) {
    ALL("All"),
    TO_BUY("To buy"),
    DONE("Done"),
}

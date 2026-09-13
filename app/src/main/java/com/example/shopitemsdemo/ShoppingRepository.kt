package com.example.shopitemsdemo

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

class ShoppingRepository(context: Context) {
    private val preferences = context.getSharedPreferences("shopping_items", Context.MODE_PRIVATE)

    fun load(): List<ShoppingItem> = runCatching {
        val array = JSONArray(preferences.getString(ITEMS_KEY, "[]"))
        List(array.length()) { index ->
            val item = array.getJSONObject(index)
            ShoppingItem(
                id = item.getLong("id"),
                name = item.getString("name"),
                purchased = item.optBoolean("purchased"),
            )
        }
    }.getOrDefault(emptyList())

    fun save(items: List<ShoppingItem>) {
        val array = JSONArray()
        items.forEach { item ->
            array.put(
                JSONObject()
                    .put("id", item.id)
                    .put("name", item.name)
                    .put("purchased", item.purchased),
            )
        }
        preferences.edit().putString(ITEMS_KEY, array.toString()).apply()
    }

    private companion object {
        const val ITEMS_KEY = "items"
    }
}

package com.coding.core.domain.model.account_models

enum class Currency(
    val code: String,
    val displayName: String,
    val symbol: String
) {
    RUB("RUB", "Российский рубль", "₽"),
    USD("USD", "Американский доллар", "$"),
    EUR("EUR", "Евро", "€");

    companion object {
        fun fromCode(code: String): Currency {
            return values().find { it.code == code } ?: RUB
        }
    }
}
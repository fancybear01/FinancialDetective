package com.coding.financialdetective.mappers

import com.coding.financialdetective.models.data_models.AccountBriefDto
import com.coding.financialdetective.models.domain_models.AccountBrief

fun AccountBriefDto.toDomain(): AccountBrief {
    return AccountBrief(
        id = this.id,
        name = this.name,
        balance = this.balance.toDouble(),
        currency = this.currency.replace("RUB", "₽").replace("USD", "$").replace("EUR", "€"),
    )
}
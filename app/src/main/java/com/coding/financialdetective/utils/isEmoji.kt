package com.coding.financialdetective.utils

fun Char.isEmoji(): Boolean {
    val type = Character.getType(this)
    return type == Character.SURROGATE.toInt() ||
            type == Character.OTHER_SYMBOL.toInt() ||
            (this.code in 0x1F600..0x1F64F) ||
            (this.code in 0x1F300..0x1F5FF) ||
            (this.code in 0x1F680..0x1F6FF) ||
            (this.code in 0x2600..0x26FF) ||
            (this.code in 0x2700..0x27BF) ||
            (this.code in 0xFE00..0xFE0F) ||
            (this.code in 0x1F900..0x1F9FF)
}
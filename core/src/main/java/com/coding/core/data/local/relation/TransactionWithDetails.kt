package com.coding.core.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.coding.core.data.local.entity.AccountEntity
import com.coding.core.data.local.entity.CategoryEntity
import com.coding.core.data.local.entity.TransactionEntity

data class TransactionWithDetails(
    @Embedded val transaction: TransactionEntity,
    @Relation(
        parentColumn = "accountId",
        entityColumn = "accountId"
    )
    val account: AccountEntity?,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "categoryId"
    )
    val category: CategoryEntity?
)
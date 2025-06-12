package com.coding.financialdetective.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.coding.financialdetective.ui.theme.GrayBorder
import com.coding.financialdetective.ui.theme.MintGreen
import com.coding.financialdetective.utils.formatNumberWithSpaces

@Composable
fun SummaryCard(
    label: String,
    amount: Double,
    modifier: Modifier = Modifier,
    currencySymbol: String = "₽"
) {
    Column {

        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(MintGreen)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = formatNumberWithSpaces(amount) + " " + currencySymbol,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        HorizontalDivider(
            color = GrayBorder,
            thickness = 1.dp
        )
    }
}
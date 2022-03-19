package com.baboaisystem.marketkit.models

data class ChartInfoKey(
        val coin: Coin,
        val currencyCode: String,
        val chartType: ChartType
)

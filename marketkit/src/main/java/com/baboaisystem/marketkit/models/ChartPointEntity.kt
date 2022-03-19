package com.baboaisystem.marketkit.models

import androidx.room.Entity
import java.math.BigDecimal

@Entity(primaryKeys = ["coinUid", "currencyCode", "chartType", "timestamp"])
data class ChartPointEntity(
    val coinUid: String,
    val currencyCode: String,
    val chartType: ChartType,
    val value: BigDecimal,
    val volume: BigDecimal?,
    val timestamp: Long
)

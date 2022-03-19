package com.baboaisystem.marketkit.providers

import com.baboaisystem.marketkit.managers.CoinPriceManager
import com.baboaisystem.marketkit.managers.ICoinPriceCoinUidDataSource
import com.baboaisystem.marketkit.Scheduler

class CoinPriceSchedulerFactory(
    private val manager: CoinPriceManager,
    private val provider: HsProvider
) {
    fun scheduler(currencyCode: String, coinUidDataSource: ICoinPriceCoinUidDataSource): Scheduler {
        val schedulerProvider = CoinPriceSchedulerProvider(currencyCode, manager, provider)
        schedulerProvider.dataSource = coinUidDataSource
        return Scheduler(schedulerProvider)
    }
}

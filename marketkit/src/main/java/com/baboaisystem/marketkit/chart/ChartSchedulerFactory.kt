package com.baboaisystem.marketkit.chart

import com.baboaisystem.marketkit.models.ChartInfoKey
import com.baboaisystem.marketkit.providers.CoinGeckoProvider
import com.baboaisystem.marketkit.chart.scheduler.ChartScheduler

class ChartSchedulerFactory(
        private val manager: ChartManager,
        private val provider: CoinGeckoProvider) {

    private val retryInterval: Long = 30

    fun getScheduler(key: ChartInfoKey): ChartScheduler {
        return ChartScheduler(ChartSchedulerProvider(retryInterval, key, provider, manager))
    }
}

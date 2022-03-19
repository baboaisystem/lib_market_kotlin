package com.baboaisystem.marketkit.chart

import com.baboaisystem.marketkit.ProviderError
import com.baboaisystem.marketkit.models.ChartInfoKey
import com.baboaisystem.marketkit.providers.CoinGeckoProvider
import com.baboaisystem.marketkit.chart.scheduler.IChartSchedulerProvider
import io.reactivex.Single

class ChartSchedulerProvider(
    override val retryInterval: Long,
    private val key: ChartInfoKey,
    private val provider: CoinGeckoProvider,
    private val manager: ChartManager
) : IChartSchedulerProvider {

    override val id = key.toString()

    override val lastSyncTimestamp: Long?
        get() = manager.getLastSyncTimestamp(key)

    override val expirationInterval: Long
        get() = key.chartType.seconds

    override val syncSingle: Single<Unit>
        get() = provider.chartPointsSingle(key)
            .doOnSuccess { points ->
                manager.update(points, key)
            }
            .doOnError {
                if (it is ProviderError.NoDataForCoin) {
                    manager.handleNoChartPoints(key)
                }
            }
            .map { }

    override fun notifyExpired() = Unit

}

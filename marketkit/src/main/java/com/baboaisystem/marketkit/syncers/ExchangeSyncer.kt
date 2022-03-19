package com.baboaisystem.marketkit.syncers

import android.util.Log
import com.baboaisystem.marketkit.managers.ExchangeManager
import com.baboaisystem.marketkit.providers.CoinGeckoProvider
import com.baboaisystem.marketkit.storage.SyncerStateDao
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ExchangeSyncer(
    private val exchangeManager: ExchangeManager,
    private val coinGeckoProvider: CoinGeckoProvider,
    private val syncerStateDao: SyncerStateDao
) {
    private val keyLastSyncTimestamp = "exchange-syncer-last-sync-timestamp"
    private val syncPeriod = 7 * 24 * 60 * 60 // 7 days

    private var disposable: Disposable? = null

    fun sync() {
        val currentTimestamp = System.currentTimeMillis() / 1000
        val lastSyncTimestamp = syncerStateDao.get(keyLastSyncTimestamp)?.toLong() ?: 0

        if (currentTimestamp - lastSyncTimestamp < syncPeriod) return

        disposable = coinGeckoProvider.exchangesSingle(250, 0)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe({ exchanges ->
                exchangeManager.handleFetched(exchanges)
                syncerStateDao.save(keyLastSyncTimestamp, currentTimestamp.toString())
            }, {
                Log.e("ExchangeSyncer", "Fetch error", it)
            })
    }

    fun stop() {
        disposable?.dispose()
        disposable = null
    }
}

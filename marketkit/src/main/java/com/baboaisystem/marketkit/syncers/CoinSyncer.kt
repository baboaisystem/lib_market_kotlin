package com.baboaisystem.marketkit.syncers

import android.util.Log
import com.baboaisystem.marketkit.managers.CoinManager
import com.baboaisystem.marketkit.models.FullCoin
import com.baboaisystem.marketkit.providers.HsProvider
import com.baboaisystem.marketkit.storage.SyncerStateDao
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class CoinSyncer(
    private val hsProvider: HsProvider,
    private val coinManager: CoinManager,
    private val syncerStateDao: SyncerStateDao
) {
    private val keyLastSyncTimestamp = "coin-syncer-last-sync-timestamp"
    private val syncPeriod = 24 * 60 * 60 // 1 day
    private val limit = 1000

    private var disposable: Disposable? = null

    fun sync() {
        val currentTimestamp = System.currentTimeMillis() / 1000
        val lastSyncTimestamp = syncerStateDao.get(keyLastSyncTimestamp)?.toLong() ?: 0

        if (currentTimestamp - lastSyncTimestamp < syncPeriod) return

        disposable = getFullCoins()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe({ fullCoins ->
                coinManager.handleFetched(fullCoins)
                syncerStateDao.save(keyLastSyncTimestamp, currentTimestamp.toString())
            }, {
                Log.e("AAA", "CoinSyncer error", it)
            })
    }

    private fun getFullCoins(page: Int = 1, stack: List<FullCoin> = listOf()): Single<List<FullCoin>> {
        return hsProvider.getFullCoins(page, limit)
            .flatMap { currentPageItems ->
                val allItems = stack + currentPageItems
                if (currentPageItems.size < limit) {
                    Single.just(allItems)
                } else {
                    getFullCoins(page + 1, allItems)
                }
            }
    }

    fun stop() {
        disposable?.dispose()
        disposable = null
    }

}

package com.baboaisystem.marketkit.managers

import com.baboaisystem.marketkit.models.GlobalMarketInfo
import com.baboaisystem.marketkit.models.GlobalMarketPoint
import com.baboaisystem.marketkit.models.TimePeriod
import com.baboaisystem.marketkit.providers.HsProvider
import com.baboaisystem.marketkit.storage.GlobalMarketInfoStorage
import io.reactivex.Single

class GlobalMarketInfoManager(
    private val provider: HsProvider,
    private val storage: GlobalMarketInfoStorage
) {
    private val expirationInterval = 600 // 10 minutes

    fun globalMarketInfoSingle(currencyCode: String, timePeriod: TimePeriod): Single<List<GlobalMarketPoint>> {
        val currentTimestamp = System.currentTimeMillis() / 1000

        storage.globalMarketInfo(currencyCode, timePeriod)?.let { data ->
            if (currentTimestamp - data.timestamp <= expirationInterval)
                return Single.just(data.points)
        }

        return provider.getGlobalMarketPointsSingle(currencyCode, timePeriod)
            .map { globalMarketPoints ->
                storage.save(GlobalMarketInfo(currencyCode, timePeriod, globalMarketPoints, currentTimestamp))

                globalMarketPoints
            }
    }
}

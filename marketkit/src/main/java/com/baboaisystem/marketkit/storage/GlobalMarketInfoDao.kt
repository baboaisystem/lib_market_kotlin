package com.baboaisystem.marketkit.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.baboaisystem.marketkit.models.GlobalMarketInfo
import com.baboaisystem.marketkit.models.TimePeriod

@Dao
interface GlobalMarketInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(globalMarketInfo: GlobalMarketInfo)

    @Query("SELECT * FROM GlobalMarketInfo WHERE currencyCode=:currencyCode AND timePeriod=:timePeriod")
    fun getGlobalMarketInfo(currencyCode: String, timePeriod: TimePeriod): GlobalMarketInfo?
}

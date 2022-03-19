package com.baboaisystem.marketkit.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.baboaisystem.marketkit.models.ChartPointEntity
import com.baboaisystem.marketkit.models.ChartType

@Dao
interface ChartPointDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(stats: List<ChartPointEntity>)

    @Query("DELETE FROM ChartPointEntity WHERE coinUid = :coinUid AND currencyCode = :currencyCode AND chartType = :chartType")
    fun delete(coinUid: String, currencyCode: String, chartType: ChartType)

    @Query("SELECT * FROM ChartPointEntity WHERE coinUid = :coinUid AND currencyCode = :currencyCode AND chartType = :chartType ORDER BY timestamp ASC")
    fun getList(coinUid: String, currencyCode: String, chartType: ChartType): List<ChartPointEntity>

}

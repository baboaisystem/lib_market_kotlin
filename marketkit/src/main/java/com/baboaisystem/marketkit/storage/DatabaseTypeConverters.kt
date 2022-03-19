package com.baboaisystem.marketkit.storage

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.baboaisystem.marketkit.models.CoinType
import com.baboaisystem.marketkit.models.GlobalMarketPoint
import com.baboaisystem.marketkit.models.TimePeriod
import java.math.BigDecimal

class DatabaseTypeConverters {
    private val gson by lazy { Gson() }

    @TypeConverter
    fun fromCoinType(coinType: CoinType?): String {
        return coinType?.id ?: ""
    }

    @TypeConverter
    fun toCoinType(value: String): CoinType {
        return CoinType.fromId(value)
    }

    @TypeConverter
    fun fromMap(map: Map<String, String>): String {
        return gson.toJson(map)
    }

    @TypeConverter
    fun toMap(value: String): Map<String, String> {
        return gson.fromJson(value, object : TypeToken<Map<String, String>>() {}.type)
    }

    @TypeConverter
    fun fromBigDecimal(bigDecimal: BigDecimal?): String? {
        return bigDecimal?.toPlainString()
    }

    @TypeConverter
    fun toBigDecimal(value: String?): BigDecimal? {
        return value?.let { BigDecimal(value) }
    }

    @TypeConverter
    fun fromTimePeriod(timePeriod: TimePeriod): String {
        return timePeriod.name
    }

    @TypeConverter
    fun toTimePeriod(value: String): TimePeriod {
        return TimePeriod.valueOf(value)
    }

    @TypeConverter
    fun fromGlobalMarketPointList(list: List<GlobalMarketPoint>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toGlobalMarketPointList(value: String): List<GlobalMarketPoint> {
        return gson.fromJson(value, object : TypeToken<List<GlobalMarketPoint>>() {}.type)
    }
}

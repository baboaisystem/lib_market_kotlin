package com.baboaisystem.marketkit.models

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

data class CoinGeckoCoinResponse(
    val id: String,
    val symbol: String,
    val name: String,
    val platforms: Map<String, String>,
    val tickers: List<MarketTickerRaw>
) {

    private fun isSmartContractAddress(v: String): Boolean {
        if (v.length != 42) return false

        return v.matches("^0[xX][A-z0-9]+$".toRegex())
    }

    private fun filterTicker(ticker: MarketTickerRaw) = when {
        ticker.lastRate.compareTo(BigDecimal.ZERO) == 0 -> false
        ticker.volume.compareTo(BigDecimal.ZERO) == 0 -> false
        isSmartContractAddress(ticker.base) -> false
        isSmartContractAddress(ticker.target) -> false
        isSymbolNotTargetNorBase(ticker.target, ticker.base) -> false
        else -> true
    }

    private fun isSymbolNotTargetNorBase(target: String, base: String): Boolean {
        return (symbol.lowercase() != target.lowercase() && symbol.lowercase() != base.lowercase())
    }

    val exchangeIds: List<String>
        get() = tickers.map { it.market.id }

    fun marketTickers(imageUrls: Map<String, String>): List<MarketTicker> {
        val contractAddresses = platforms.mapNotNull { (platformName, contractAddress) ->
            if (smartContractPlatforms.contains(platformName)) {
                contractAddress.lowercase()
            } else {
                null
            }
        }

        return tickers.map { raw ->
            val base = if (contractAddresses.contains(raw.base.lowercase(Locale.ENGLISH))) {
                symbol.uppercase()
            } else {
                raw.base
            }

            val target = if (contractAddresses.contains(raw.target.lowercase(Locale.ENGLISH))) {
                symbol.uppercase()
            } else {
                raw.target
            }

            MarketTickerRaw(
                base,
                target,
                raw.market,
                raw.lastRate,
                raw.volume,
            )
        }
            .filter { filterTicker(it) }
            .map {
                val imageUrl = imageUrls[it.market.id]
                var target = it.target
                var base = it.base
                var volume = it.volume
                var lastRate = it.lastRate
                if (it.target.lowercase() == symbol.lowercase()) {
                    base = symbol.uppercase()
                    target = it.base
                    volume *= lastRate
                    lastRate = BigDecimal.ONE.divide(lastRate, 4, RoundingMode.HALF_EVEN)
                }
                MarketTicker(base, target, it.market.name, imageUrl, lastRate, volume)
            }
    }

    companion object {
        private val smartContractPlatforms: List<String> =
            listOf("tron", "ethereum", "eos", "binance-smart-chain", "binancecoin")
    }
}

data class MarketTickerRaw(
    val base: String,
    val target: String,
    val market: TickerMarketRaw,
    @SerializedName("last")
    val lastRate: BigDecimal,
    val volume: BigDecimal,
)

data class TickerMarketRaw(
    @SerializedName("identifier")
    val id: String,
    val name: String,
)

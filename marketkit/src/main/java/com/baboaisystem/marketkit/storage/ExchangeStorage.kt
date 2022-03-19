package com.baboaisystem.marketkit.storage

import com.baboaisystem.marketkit.models.Exchange

class ExchangeStorage(marketDatabase: MarketDatabase) {
    private val exchangeDao = marketDatabase.exchangeDao()

    fun exchanges(ids: List<String>): List<Exchange> {
        return exchangeDao.getItems(ids)
    }

    fun save(items: List<Exchange>) {
        exchangeDao.insert(items)
    }

    fun deleteAll() {
        exchangeDao.deleteAll()
    }

    fun update(exchanges: List<Exchange>) {
        deleteAll()
        save(exchanges)
    }
}

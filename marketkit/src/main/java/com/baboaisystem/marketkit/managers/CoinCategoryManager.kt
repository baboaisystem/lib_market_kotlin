package com.baboaisystem.marketkit.managers

import com.baboaisystem.marketkit.models.CoinCategory
import com.baboaisystem.marketkit.storage.CoinCategoryStorage
import io.reactivex.subjects.PublishSubject

class CoinCategoryManager(
    private val storage: CoinCategoryStorage
) {
    val coinCategoriesObservable = PublishSubject.create<List<CoinCategory>>()

    fun coinCategories(): List<CoinCategory> {
        return storage.coinCategories()
    }

    fun coinCategories(uids: List<String>): List<CoinCategory> {
        return storage.coinCategories(uids)
    }

    fun coinCategory(uid: String): CoinCategory? {
        return storage.coinCategory(uid)
    }

    fun handleFetched(coinCategories: List<CoinCategory>) {
        storage.save(coinCategories)
        coinCategoriesObservable.onNext(coinCategories)
    }
}

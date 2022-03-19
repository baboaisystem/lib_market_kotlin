package com.baboaisystem.marketkit.managers

import com.baboaisystem.marketkit.models.Post
import com.baboaisystem.marketkit.providers.CryptoCompareProvider
import io.reactivex.Single

class PostManager(
    private val provider: CryptoCompareProvider
) {
    fun postsSingle(): Single<List<Post>> {
        return provider.postsSingle()
    }
}

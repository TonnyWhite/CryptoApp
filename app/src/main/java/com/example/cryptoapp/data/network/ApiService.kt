package com.example.cryptoapp.data.network

import com.example.cryptoapp.data.network.model.CoinNamesListDto
import com.example.cryptoapp.data.network.model.CoinInfoJsonContainerDto
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {

    @GET("top/totalvolfull")
    suspend fun getTopCoinsInfo(
        @Query(QUERY_PARAM_API_KEY) apiKey: String = "6c6fb07ea4dd20e5eed1ac291acb285040d9e0cac4a76f84fa458d0b81530370",
        @Query(QUERY_PARAM_LIMIT) limit: Int = 10,
        @Query(QUERY_PARAM_TO_SYMBOL) tSym: String = CURRENCY
    ): CoinNamesListDto

    @GET("pricemultifull")
    suspend fun getFullPriceList(
        @Query(QUERY_PARAM_API_KEY) apiKey: String = "6c6fb07ea4dd20e5eed1ac291acb285040d9e0cac4a76f84fa458d0b81530370",
        @Query(QUERY_FROM_SYMBOLS) fromSyms: String,
        @Query(QUERY_TO_SYMBOLS) toSyms: String = CURRENCY
    ): CoinInfoJsonContainerDto



    companion object{
        private const val QUERY_PARAM_API_KEY = "api_key"
        private const val QUERY_PARAM_LIMIT = "limit"
        private const val QUERY_PARAM_TO_SYMBOL = "tsym"

        private const val QUERY_FROM_SYMBOLS ="fsyms"
        private const val QUERY_TO_SYMBOLS ="tsyms"


        private const val CURRENCY = "USD"
    }
}
package com.example.cryptoapp.data.worker

import android.content.Context
import androidx.work.*
import com.example.cryptoapp.data.database.AppDatabase
import com.example.cryptoapp.data.mapper.CoinMapper
import com.example.cryptoapp.data.network.ApiFactory
import kotlinx.coroutines.delay

class RefreshDataWorker(
    context: Context,
    workerParameters: WorkerParameters
): CoroutineWorker(context, workerParameters) {
    private val coinInfoDao = AppDatabase.getInstance(context).coinPriceInfoDao()
    private val apiService = ApiFactory.apiService

    private val mapper = CoinMapper()
    override suspend fun doWork(): Result {
        while (true) {
            try {
                val topCoins = apiService.getTopCoinsInfo(limit = 50)
                val fSyms = mapper.mapNamesListToString(topCoins)
                val jsonContainer = apiService.getFullPriceList(fromSyms = fSyms)
                val coinInfoListDto = mapper.mapJsonContainerToListCoinInfo(jsonContainer)
                val dbModelList = coinInfoListDto.map { mapper.mapDtoToDbModel(it) }
                coinInfoDao.insertPriceList(dbModelList)
            } catch (e: Exception) {
            }
            delay(10000)
        }
    }

    companion object{
        const val NAME = "RefreshDataWorker"
        fun makeRequest(): OneTimeWorkRequest{
            return OneTimeWorkRequestBuilder<RefreshDataWorker>().build()
        }
    }
}
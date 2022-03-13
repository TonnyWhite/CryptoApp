package com.example.cryptoapp.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.cryptoapp.data.network.ApiFactory
import com.example.cryptoapp.data.database.AppDatabase
import com.example.cryptoapp.data.network.model.CoinInfoDto
import com.example.cryptoapp.data.network.model.CoinInfoJsonContainerDto
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class CoinViewModel(application: Application): AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)

    private val compositeDisposable = CompositeDisposable()

    val priceList = db.coinPriceInfoDao().getPriceList()

    fun getDetailInfo(fSym: String) : LiveData<CoinInfoDto>{
        return db.coinPriceInfoDao().getPriceInfoAboutCoin(fSym)
    }

    init {
        loadData()
    }

    private fun loadData(){
        val disposable = ApiFactory.apiService.getTopCoinsInfo(limit = 50)
            .map {it.coinNameContainers?.map { it?.coinName?.name }?.joinToString(",") }
            .flatMap { ApiFactory.apiService.getFullPriceList(fromSyms = it) }
            .map { getPriceListFromRawData(it) }
            .delaySubscription(10, TimeUnit.SECONDS)
            .repeat()
            .retry()
            .subscribeOn(Schedulers.io())
            .subscribe({
                it?.let { it1 -> db.coinPriceInfoDao().insertPriceList(it1) }
                Log.d("TEST_OF_LOADING_DATA", "Success: ${it.toString()}")

            }, {
                Log.d("TEST_OF_LOADING_DATA","Faillure: ${ it.message.toString()}")
            })

        compositeDisposable.add(disposable)

    }

     private fun getPriceListFromRawData(
         coinInfoJsonContainerDto: CoinInfoJsonContainerDto
     ): List<CoinInfoDto>? {
         val result = ArrayList<CoinInfoDto>()
         val jsonObject = coinInfoJsonContainerDto.json ?: return null
         val coinKeySet = jsonObject.keySet()
         for (coinKey in coinKeySet) {
             val currencyJson = jsonObject.getAsJsonObject(coinKey)
             val currencyKeySet = currencyJson.keySet()
             for (currencyKey in currencyKeySet){
                 val priceInfo = Gson().fromJson(
                     currencyJson.getAsJsonObject(currencyKey),
                     CoinInfoDto::class.java
                 )
                 result.add(priceInfo)
             }
         }
         return result
     }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}

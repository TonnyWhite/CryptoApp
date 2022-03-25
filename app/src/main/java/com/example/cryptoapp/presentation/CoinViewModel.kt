package com.example.cryptoapp.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoapp.data.repository.CoinRepositoryImpl
import com.example.cryptoapp.domain.GetCoinInfoListUseCase
import com.example.cryptoapp.domain.GetCoinInfoUseCase
import com.example.cryptoapp.domain.LoadDataUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class CoinViewModel @Inject constructor(
        private val getCoinInfoListUseCase: GetCoinInfoListUseCase,
        private val getCoinInfoUseCase: GetCoinInfoUseCase,
        private val loadDataUseCase: LoadDataUseCase) : ViewModel() {


//    private val repository = CoinRepositoryImpl(application)



    val coinInfoList = getCoinInfoListUseCase()

    fun getDetailInfo(fSym: String) = getCoinInfoUseCase(fSym)


    init {
            loadDataUseCase()
    }


}

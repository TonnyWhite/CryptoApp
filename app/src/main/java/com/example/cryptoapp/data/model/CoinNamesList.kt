package com.example.cryptoapp.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CoinNamesList(
    @SerializedName("Data")
    @Expose
    val coinNameContainers: List<CoinNameContainer?>? = null

)
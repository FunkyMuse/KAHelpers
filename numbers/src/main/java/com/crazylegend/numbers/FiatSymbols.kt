package com.crazylegend.numbers

import android.util.ArrayMap


/**
 * Created by Hristijan on 20/2/19 to long live and prosper !
 */


object FiatSymbols {
    var symbolsMap: ArrayMap<String, String> = ArrayMap()

    init {
        symbolsMap["AUD"] = "$"
        symbolsMap["AED"] = "د.إ"
        symbolsMap["ARS"] = "$"
        symbolsMap["BCH"] = "BCH"
        symbolsMap["BDT"] = "৳"
        symbolsMap["BHD"] = ".د.ب"
        symbolsMap["BMD"] = "$"
        symbolsMap["USD"] = "$"
        symbolsMap["EUR"] = "€"
        symbolsMap["BRL"] = "R$"
        symbolsMap["CAD"] = "$"
        symbolsMap["CHF"] = "CHF"
        symbolsMap["CLP"] = "$"
        symbolsMap["CNY"] = "¥"
        symbolsMap["CZK"] = "Kč"
        symbolsMap["DKK"] = "kr"
        symbolsMap["GBP"] = "£"
        symbolsMap["HKD"] = "$"
        symbolsMap["HUF"] = "Ft"
        symbolsMap["IDR"] = "Rp"
        symbolsMap["ILS"] = "₪"
        symbolsMap["INR"] = "₹"
        symbolsMap["JPY"] = "¥"
        symbolsMap["KRW"] = "₩"
        symbolsMap["KWD"] = "د.ك"
        symbolsMap["MXN"] = "$"
        symbolsMap["VEF"] = "Bs."
        symbolsMap["MYR"] = "MR"
        symbolsMap["NOK"] = "kr"
        symbolsMap["NZD"] = "$"
        symbolsMap["PHP"] = "₱"
        symbolsMap["PKR"] = "₨"
        symbolsMap["PLN"] = "zł"
        symbolsMap["LKR"] = "රු"
        symbolsMap["RUB"] = "\u20BD"
        symbolsMap["XDR"] = "SDR"
        symbolsMap["XAG"] = "XAG/USD"
        symbolsMap["XAU"] = "XAU/USD"
        symbolsMap["SEK"] = "kr"
        symbolsMap["SAR"] = "ر.س"
        symbolsMap["SGD"] = "$"
        symbolsMap["THB"] = "฿"
        symbolsMap["TRY"] = "₺"
        symbolsMap["TWD"] = "NT$"
        symbolsMap["MMK"] = "K"
        symbolsMap["ZAR"] = "R"
        symbolsMap["BTC"] = "฿"
        symbolsMap["ETH"] = "Ξ"
        symbolsMap["LTC"] = "Ł"
        symbolsMap["BCH"] = "BCH"
    }


}
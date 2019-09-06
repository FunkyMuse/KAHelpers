package com.crazylegend.kotlinextensions.hideData


/**
 * Created by hristijan on 3/29/19 to long live and prosper !
 */


fun hideBankCard(cardNo: String): String {
    return hideData(cardNo, 16, 4, 4)
}


fun hideBankCardStrictly(cardNo: String): String {
    return hideData(cardNo, 16, 0, 4)
}


fun hideIdCard(cardNo: String): String {
    return hideData(cardNo, 18, 6, 4)
}


fun hideTel(tel: String): String {
    return hideData(tel, 11, 3, 4)
}



fun hideName(name: String): String {
    return if (name.length <= 3) {
        hideData(name, name.length, 1, 0)
    } else hideData(name, name.length, 2, 0)
}


private fun hideData(data: String, remainLength: Int, beforeLength: Int, afterLength: Int): String {
    var tempData = data
    val length = tempData.length
    tempData = if (length <= remainLength) {
        val dataBuilder = StringBuilder(tempData)
        for (i in 0 until remainLength - length) {
            dataBuilder.insert(0, "0")
        }
        dataBuilder.toString()
    } else {
        tempData.substring(0, remainLength - 4) + tempData.substring(tempData.length - 4)
    }
    val replaceSymbol = "*"
    val sb = StringBuilder()
    for (i in 0 until remainLength) {
        if (i < beforeLength || i >= remainLength - afterLength) {
            sb.append(tempData[i])
        } else {
            sb.append(replaceSymbol)
        }
    }
    return sb.toString()
}



fun splitStrWithSpace(data: String, spaceIndex: Int): String {
    var tempData = data
    val regex = "(.{$spaceIndex})"
    tempData = tempData.replace(regex.toRegex(), "$1 ")
    return tempData
}


fun hideAndSplitBankCard(cardNo: String): String {
    var cardTemp = cardNo
    cardTemp = hideBankCard(cardTemp)
    return splitStrWithSpace(cardTemp, 4)
}


fun hideAndSplitBankCardStrictly(cardNo: String): String {
    var cardNoTemp = cardNo
    cardNoTemp = hideBankCardStrictly(cardNoTemp)
    return splitStrWithSpace(cardNoTemp, 4)
}


fun hideAndSplitIdCard(cardNo: String): String {
    var cardNoTemp = cardNo
    cardNoTemp = hideIdCard(cardNoTemp)
    return cardNoTemp.substring(0, 6) + " " + cardNoTemp.substring(6, 14) + " " + cardNoTemp.substring(14, cardNoTemp.length)
}


fun hideAndSplitTel(tel: String): String {
    var telNum = tel
    telNum = hideTel(telNum)
    return telNum.substring(0, 3) + " " + telNum.substring(3, 7) + " " + telNum.substring(7, telNum.length)
}

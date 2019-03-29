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
    var data = data
    val length = data.length
    data = if (length <= remainLength) {
        // 实际位数小等于要保留的位数，补0
        val dataBuilder = StringBuilder(data)
        for (i in 0 until remainLength - length) {
            dataBuilder.insert(0, "0")
        }
        dataBuilder.toString()
    } else {
        // 实际位数大于要保留的位数。否则保留0-保留长度-4， 源数据最后4位
        data.substring(0, remainLength - 4) + data.substring(data.length - 4)
    }
    // 替换字符串，当前使用“*”
    val replaceSymbol = "*"
    val sb = StringBuilder()
    for (i in 0 until remainLength) {
        if (i < beforeLength || i >= remainLength - afterLength) {
            sb.append(data[i])
        } else {
            sb.append(replaceSymbol)
        }
    }
    return sb.toString()
}



fun splitStrWithSpace(data: String, spaceIndex: Int): String {
    var data = data
    val regex = "(.{$spaceIndex})"
    data = data.replace(regex.toRegex(), "$1 ")
    return data
}


fun hideAndSplitBankCard(cardNo: String): String {
    var cardNo = cardNo
    cardNo = hideBankCard(cardNo)
    return splitStrWithSpace(cardNo, 4)
}


fun hideAndSplitBankCardStrictly(cardNo: String): String {
    var cardNo = cardNo
    cardNo = hideBankCardStrictly(cardNo)
    return splitStrWithSpace(cardNo, 4)
}


fun hideAndSplitIdCard(cardNo: String): String {
    var cardNo = cardNo
    cardNo = hideIdCard(cardNo)
    return cardNo.substring(0, 6) + " " + cardNo.substring(6, 14) + " " + cardNo.substring(14, cardNo.length)
}


fun hideAndSplitTel(tel: String): String {
    var tel = tel
    tel = hideTel(tel)
    return tel.substring(0, 3) + " " + tel.substring(3, 7) + " " + tel.substring(7, tel.length)
}

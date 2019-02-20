package com.crazylegend.kotlinextensions.numbers


/**
 * Created by hristijan on 2/20/19 to long live and prosper !
 */


val Number.getSatoshi: Double
    get() {
        return this.toDouble().times(0.00000001)
    }

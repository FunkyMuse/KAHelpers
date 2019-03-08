package com.crazylegend.kotlinextensions.interfaces


/**
 * Created by hristijan on 3/5/19 to long live and prosper !
 */


interface OneParamInvocation<in A> {
    operator fun invoke(object1: A)
}

interface TwoParamsInvocation<in A, in B> {
    operator fun invoke(object1: A, object2: B)
}

interface ThreeParamsInvocation<in A, in B, in C> {
    operator fun invoke(object1: A, object2: B, object3: C)
}
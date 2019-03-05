package com.crazylegend.kotlinextensions.interfaces


/**
 * Created by hristijan on 3/5/19 to long live and prosper !
 */


interface F1<in A> {
    operator fun invoke(object1: A)
}

interface F2<in A, in B> {
    operator fun invoke(object1: A, object2: B)
}

interface F3<in A, in B, in C> {
    operator fun invoke(object1: A, object2: B, object3: C)
}
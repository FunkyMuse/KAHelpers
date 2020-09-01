package com.crazylegend.rx

import io.reactivex.rxjava3.core.Observable


/**
 * Created by Hristijan on 2/1/19 to long live and prosper !
 */
object RxBus {

    @PublishedApi
    internal val publisher = PublishSubject<Any>()

    fun publish(event: Any) {
        publisher.onNext(event)
    }

    inline fun <reified T> listen(): Observable<T> = publisher.ofType(T::class.java)

    /*RxBus.publish("Testing")*/

    /*
    RxBus.listen<String>().subscribe {
        println("Im a String $it")
    }*/
}
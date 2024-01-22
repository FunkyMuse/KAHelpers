package dev.funkymuse.rx

import io.reactivex.rxjava3.core.Observable



object RxBus {

    @PublishedApi
    internal val publisher = PublishSubject<Any>()

    fun publish(event: Any) {
        publisher.onNext(event)
    }

    inline fun <reified T : Any> listen(): Observable<T> = publisher.ofType(T::class.java)

    /*RxBus.publish("Testing")*/

    /*
    RxBus.listen<String>().subscribe {
        println("Im a String $it")
    }*/
}
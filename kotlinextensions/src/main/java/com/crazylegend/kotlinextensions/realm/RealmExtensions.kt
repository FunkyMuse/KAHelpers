package com.crazylegend.kotlinextensions.realm


/**
 * Created by Hristijan on 2/1/19 to long live and prosper !
 */

/*

fun executeTransaction(action: (realmInstance: Realm) -> Unit) {
    Realm.getDefaultInstance().use {
        it.executeTransaction { realmInstance ->
            action(realmInstance)
        }
    }
}

fun executeTransactionAsync(action: (realmInstance: Realm) -> Unit) {
    Realm.getDefaultInstance().use {
        it.executeTransactionAsync { realmInstance ->
            action(realmInstance)
        }
    }
}


fun useRealm(action: (realmInstance: Realm) -> Unit) {
    Realm.getDefaultInstance().use { realmInstance ->
        action(realmInstance)
    }
}

inline fun <reified T : RealmObject> createRealmObjectAndModify(nextID: Int, crossinline modifications: (obj: T) -> Unit) {
    Realm.getDefaultInstance().use {
        it.executeTransactionAsync {
            modifications(it.createObject(T::class.java, nextID))
        }
    }
}

inline fun <reified T : RealmObject> ids(fieldName: String): RealmID {
    val idHelper = RealmID()

    Realm.getDefaultInstance().use {
        val currentID = it.where(T::class.java).max(fieldName)

        if (currentID == null) {
            idHelper.nextID = 1
        } else {
            idHelper.nextID = currentID.toInt() + 1
        }

        currentID?.let {
            idHelper.currentID = it.toInt()
        }

        return idHelper
    }

}

inline fun <T> Realm.callTransaction(crossinline action: Realm.() -> T): T {
    val ref = AtomicReference<T>()
    executeTransaction {
        ref.set(action(it))
    }
    return ref.get()
}

inline fun <reified T : RealmModel> Realm.where(): RealmQuery<T> {
    return this.where(T::class.java)
}

inline fun <reified T : RealmModel> Realm.delete() {
    return this.delete(T::class.java)
}

inline fun <reified T : RealmModel> Realm.createObject(): T {
    return this.createObject(T::class.java)
}

inline fun <reified T : RealmModel> Realm.createObject(primaryKeyValue: Any?): T {
    return this.createObject(T::class.java, primaryKeyValue)
}


fun RealmObject.saveAsync() {
    Realm.getDefaultInstance().use { db ->
        db.executeTransactionAsync {
            db.insertOrUpdate(this)
        }
    }
}

fun RealmObject.save() {
    Realm.getDefaultInstance().use { db ->
        db.executeTransaction {
            db.insertOrUpdate(this)
        }
    }
}
*/

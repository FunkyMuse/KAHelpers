package com.crazylegend.kotlinextensions.realm


/**
 * Created by crazy on 2/2/19 to long live and prosper !
 */
/**
 * Created by Hristijan to live long and prosper.
 */

/*
class KeyHelper<E : RealmModel?>(var classFrom: Class<E>) {

    fun getIds(db: Realm, idName: String): IdModel {


        val idHelper = IdModel()

        val currentID = db.where(classFrom).max(idName)


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

data class IdModel(var currentID:Int=0, var nextID:Int=0)
*/

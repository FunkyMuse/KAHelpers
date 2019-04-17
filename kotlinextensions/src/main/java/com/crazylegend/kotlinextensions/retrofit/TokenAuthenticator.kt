package com.crazylegend.kotlinextensions.retrofit

import android.app.Activity
import android.content.Context
import android.content.Intent
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route


/**
 * Created by Hristijan on 1/25/19 to long live and prosper !
 */
class TokenAuthenticator<E>(private val context: Context, private val loginClass:Class<E>) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (!response.isSuccessful && response.header("Authorization") != null) {
            val intent = Intent(context, loginClass)
            context.startActivity(intent)
            (context as Activity).finish()
        }
        return null
    }

}
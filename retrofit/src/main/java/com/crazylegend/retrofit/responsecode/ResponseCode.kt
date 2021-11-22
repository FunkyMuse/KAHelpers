package com.crazylegend.retrofit.responsecode

/**
 * Created by funkymuse on 11/20/21 to long live and prosper !
 */
sealed interface ResponseCode {
    object MovedPermanently : ResponseCode
    object BadRequest : ResponseCode
    object Unauthorized : ResponseCode
    object Forbidden : ResponseCode
    object NotFound : ResponseCode
    object NotAllowed : ResponseCode
    object NotAcceptable : ResponseCode
    object ProxyAuthenticationRequired : ResponseCode
    object Timeout : ResponseCode
    object ConflictError : ResponseCode
    object RequestPermanentlyDeleted:ResponseCode
    object RequestTooLarge : ResponseCode
    object AccountExists : ResponseCode
    object ServerIsBusy : ResponseCode
    object TooManyRequests :ResponseCode
    object InternalServerError : ResponseCode
    object NotImplemented : ResponseCode
    object BadGateway : ResponseCode
    object GatewayTimeout : ResponseCode
    object AuthenticationRequired : ResponseCode

    object GenericError : ResponseCode
}


fun Int.asResponseCode(): ResponseCode =
        when (this) {
            301 -> ResponseCode.MovedPermanently
            400 -> ResponseCode.BadRequest
            401 -> ResponseCode.Unauthorized
            403 -> ResponseCode.Forbidden
            404 -> ResponseCode.NotFound
            405 -> ResponseCode.NotAllowed
            406 -> ResponseCode.NotAcceptable
            407 -> ResponseCode.ProxyAuthenticationRequired
            408 -> ResponseCode.Timeout
            409 -> ResponseCode.ConflictError
            410 -> ResponseCode.RequestPermanentlyDeleted
            413 -> ResponseCode.RequestTooLarge
            422 -> ResponseCode.AccountExists
            425 -> ResponseCode.ServerIsBusy
            429 -> ResponseCode.TooManyRequests
            500 -> ResponseCode.InternalServerError
            501 -> ResponseCode.NotImplemented
            502 -> ResponseCode.BadGateway
            504 -> ResponseCode.GatewayTimeout
            511 -> ResponseCode.AuthenticationRequired

            else -> ResponseCode.GenericError
        }
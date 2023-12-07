package com.crazylegend.retrofit.responsecode

/**
 * Created by funkymuse on 11/20/21 to long live and prosper !
 */
sealed interface ResponseCode {
    data object MovedPermanently : ResponseCode
    data object BadRequest : ResponseCode
    data object Unauthorized : ResponseCode
    data object Forbidden : ResponseCode
    data object NotFound : ResponseCode
    data object NotAllowed : ResponseCode
    data object NotAcceptable : ResponseCode
    data object ProxyAuthenticationRequired : ResponseCode
    data object Timeout : ResponseCode
    data object ConflictError : ResponseCode
    data object RequestPermanentlyDeleted:ResponseCode
    data object RequestTooLarge : ResponseCode
    data object AccountExists : ResponseCode
    data object ServerIsBusy : ResponseCode
    data object TooManyRequests :ResponseCode
    data object InternalServerError : ResponseCode
    data object NotImplemented : ResponseCode
    data object BadGateway : ResponseCode
    data object GatewayTimeout : ResponseCode
    data object AuthenticationRequired : ResponseCode
    data object GenericError : ResponseCode
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
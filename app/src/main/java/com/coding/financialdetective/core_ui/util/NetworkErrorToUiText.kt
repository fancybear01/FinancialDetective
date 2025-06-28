package com.coding.financialdetective.core_ui.util

import com.coding.financialdetective.R
import com.coding.financialdetective.data.util.NetworkError

fun NetworkError.toUiText(): UiText {
    val resId = when(this) {
        NetworkError.REQUEST_TIMEOUT -> R.string.error_request_timeout
        NetworkError.TOO_MANY_REQUESTS -> R.string.error_too_many_requests
        NetworkError.NO_INTERNET -> R.string.error_no_internet
        NetworkError.SERVER_ERROR -> R.string.error_server_error
        NetworkError.SERIALIZATION -> R.string.error_serialization
        NetworkError.UNKNOWN -> R.string.error_unknown
        NetworkError.BAD_REQUEST -> R.string.error_bad_request
        NetworkError.UNAUTHORIZED -> R.string.error_unauthorized
        NetworkError.FORBIDDEN -> R.string.error_forbidden
        NetworkError.NOT_FOUND -> R.string.error_not_found
    }
    return UiText.StringResource(resId)
}
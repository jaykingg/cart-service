package io.hobbyful.cartService.core

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

abstract class AbstractErrorCodeException(
    status: HttpStatus,
    reason: String,
    val errorCode: String
) : ResponseStatusException(status, reason)

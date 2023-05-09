package io.hobbyful.cartService.core

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class BadRequestException : ResponseStatusException(HttpStatus.BAD_REQUEST)

class NotFoundException : ResponseStatusException(HttpStatus.NOT_FOUND)

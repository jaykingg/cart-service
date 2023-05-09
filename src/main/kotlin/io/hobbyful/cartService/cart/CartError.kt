package io.hobbyful.cartService.cart

import io.hobbyful.cartService.core.BaseError

enum class CartError(override val message: String) : BaseError {
    CART_NOT_FOUND("존재하지 않는 카트입니다"),
}
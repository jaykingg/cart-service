package io.hobbyful.cartService.cart

import io.hobbyful.cartService.core.ErrorCodeException
import org.springframework.stereotype.Service

@Service
class CartQueryService(
    private val cartRepository: CartRepository
) {
    suspend fun getByCustomerId(customerId: String) =
        cartRepository.findByCustomerId(customerId)
            ?: throw ErrorCodeException.of(CartError.CART_NOT_FOUND)
}

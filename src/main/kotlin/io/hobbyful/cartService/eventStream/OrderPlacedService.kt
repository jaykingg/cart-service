package io.hobbyful.cartService.eventStream

import io.hobbyful.cartService.cart.CartQueryService
import io.hobbyful.cartService.cart.CartRepository
import jakarta.validation.ConstraintViolationException
import jakarta.validation.Validator
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderPlacedService(
    private val cartQueryService: CartQueryService,
    private val cartRepository: CartRepository,
    private val validator: Validator
) {
    @Transactional
    suspend fun clearPurchasedProducts(payload: OrderPlacedPayload) {
        validateForPayload(payload)

        val cart = cartQueryService.getByCustomerId(payload.customerId)
        val clearProductIds = payload.lineItems.map { it.productId }
        val lineItems = cart.lineItems.filterNot { it.productId in clearProductIds }

        cartRepository.save(
            cart.copy(
                lineItems = lineItems
            )
        )
    }

    private fun validateForPayload(payload: OrderPlacedPayload) {
        val violations = validator.validate(payload)
        if (violations.isNotEmpty()) {
            throw ConstraintViolationException("Payload 가 올바르지 않습니다", violations)
        }
    }
}

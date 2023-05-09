package io.hobbyful.cartService.summary

import io.hobbyful.cartService.shippingRate.ShippingRate
import jakarta.validation.constraints.Min

/**
 * 정산 요약
 */
data class Summary(
    /**
     * 주문상품 총액
     */
    @field: Min(0)
    val subtotal: Int,

    /**
     * 배송비 총액
     */
    @field: Min(0)
    val shippingCost: Int,

    /**
     * 결제 총액
     */
    @field: Min(0)
    val total: Int = subtotal + shippingCost
) {
    companion object {
        fun of(subtotal: Int = 0, shippingRate: ShippingRate): Summary {
            return Summary(
                subtotal = subtotal,
                shippingCost = computeShippingCost(subtotal, shippingRate)
            )
        }

        private fun computeShippingCost(subtotal: Int, shippingRate: ShippingRate): Int {
            if (subtotal == 0) return 0
            if (subtotal >= shippingRate.minOrderAmount) return 0
            return shippingRate.baseCost
        }
    }
}

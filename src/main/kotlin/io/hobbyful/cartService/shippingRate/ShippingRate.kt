package io.hobbyful.cartService.shippingRate

import jakarta.validation.constraints.Min

/**
 * 배송비 정책 정보
 *
 * TODO: Store Service를 통해 일괄적인 배송비 정책 적용 필요
 *
 */
data class ShippingRate(
    /**
     * 기본 배송비
     */
    @field: Min(0)
    val baseCost: Int,

    /**
     * 제주도 추가 배송비
     */
    @field: Min(0)
    val jejuCost: Int,

    /**
     * 무료배송을 위한 최소 주문금액
     */
    @field: Min(0)
    val minOrderAmount: Int
) {
    companion object {
        fun ofHobbyful() = ShippingRate(
            baseCost = ShippingPolicy.BASE_COST,
            jejuCost = ShippingPolicy.JEJU_COST,
            minOrderAmount = ShippingPolicy.MIN_ORDER_AMOUNT
        )
    }
}

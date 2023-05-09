package io.hobbyful.cartService.shippingRate

/**
 * 배송 정책
 */
object ShippingPolicy {
    /**
     * 기본 배송비
     */
    const val BASE_COST = 3_000

    /**
     * 제주도 추가 배송비
     */
    const val JEJU_COST = 3_000

    /**
     * 무료 배송을 위한 최소 주문금액
     */
    const val MIN_ORDER_AMOUNT = 50_000
}

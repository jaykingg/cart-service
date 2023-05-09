package io.hobbyful.cartService.lineitem

import org.hibernate.validator.constraints.Range

/**
 * 주문 품목 Query Parameters
 */
data class LineItemQuery(
    /**
     * 구매 수량
     */
    @field: Range(min = 1, max = 999)
    val quantity: Int
)

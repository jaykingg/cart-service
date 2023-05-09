package io.hobbyful.cartService.eventStream

import io.hobbyful.cartService.lineitem.LineItem
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import org.bson.types.ObjectId

data class OrderPlacedPayload(
    /**
     * 주문 ID
     */
    val orderId: ObjectId,

    /**
     * 고객 ID
     */
    @field: NotBlank
    val customerId: String,

    /**
     * 장바구니 ID
     */
    val cartId: ObjectId? = null,

    /**
     * 주문품목 리스트
     */
    @field: NotEmpty
    val lineItems: List<LineItem>
)

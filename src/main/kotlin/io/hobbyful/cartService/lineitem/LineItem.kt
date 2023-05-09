package io.hobbyful.cartService.lineitem

import io.hobbyful.cartService.catalog.product.Product
import io.hobbyful.cartService.catalog.variant.Variant
import jakarta.validation.constraints.Min
import org.bson.types.ObjectId

/**
 * 주문 품목
 */
data class LineItem(
    /**
     * 준비물 정보
     */
    val product: Product,

    /**
     * 상품 정보
     */
    val variant: Variant,

    /**
     * 주문품목의 구매선택 여부
     */
    val picked: Boolean,

    /**
     * 구매 수량
     */
    @field: Min(1)
    val quantity: Int,

    /**
     * 주문 품목 총액
     */
    @field: Min(0)
    val total: Int
) {
    /**
     * 주문품목의 준비물 ID
     */
    val productId: ObjectId
        get() = product.id

    /**
     * 주문품목의 상품 ID
     */
    val variantId: ObjectId
        get() = variant.id

    companion object {
        fun of(
            product: Product,
            variant: Variant,
            quantity: Int,
            picked: Boolean
        ): LineItem = LineItem(
            product = product,
            variant = variant,
            quantity = quantity,
            total = variant.price * quantity,
            picked = picked
        )
    }
}

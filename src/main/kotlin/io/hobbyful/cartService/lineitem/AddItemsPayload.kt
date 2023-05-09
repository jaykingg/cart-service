package io.hobbyful.cartService.lineitem

import jakarta.validation.Valid
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotEmpty
import org.bson.types.ObjectId
import org.hibernate.validator.constraints.UniqueElements

/**
 * 주문상품 담기 Payload
 */
data class AddItemsPayload(
    /**
     * 주문상품의 준비물 ID
     */
    val productId: ObjectId,

    /**
     * 주문상품 리스트
     */
    @field: Valid
    @field: NotEmpty
    @field: UniqueElements
    val items: List<Item>
) {
    /**
     * 주문상품 정보
     */
    data class Item(
        /**
         * 상품 ID
         */
        val variantId: ObjectId,

        /**
         * 상품 수량
         */
        @field: Min(1)
        @field: Max(999)
        val quantity: Int
    ) {
        /**
         * UniqueElements 유효성 검사에서, variantId의 중복 여부만 확인하기 위해 추가
         */
        override fun equals(other: Any?): Boolean =
            other is Item && other.variantId == variantId

        override fun hashCode(): Int = variantId.hashCode()
    }
}

/**
 * variantId 기준 구매 수량 검색 최적화
 */
val AddItemsPayload.quantityByVariantId: Map<ObjectId, Int>
    get() = items.associate { it.variantId to it.quantity }

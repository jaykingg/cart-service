package io.hobbyful.cartService.catalog.product

import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import org.bson.types.ObjectId
import org.hibernate.validator.constraints.UniqueElements

/**
 * 준비물 선택정보 수정 Payload
 */
data class ProductPicksPayload(
    /**
     * 준비물 선택정보 리스트
     */
    @field: Valid
    @field: NotEmpty
    @field: UniqueElements
    val picks: List<Pick>
) {
    /**
     * 준비물 선택정보
     */
    data class Pick(
        /**
         * 준비물 ID
         */
        val productId: ObjectId,

        /**
         * 구매 선택 여부
         */
        val picked: Boolean
    ) {
        override fun equals(other: Any?): Boolean =
            other is Pick && productId == other.productId

        override fun hashCode(): Int = productId.hashCode()
    }
}

val ProductPicksPayload.productIds: List<ObjectId>
    get() = picks.map { it.productId }

val ProductPicksPayload.pickedByProductId: Map<ObjectId, Boolean>
    get() = picks.associate { it.productId to it.picked }

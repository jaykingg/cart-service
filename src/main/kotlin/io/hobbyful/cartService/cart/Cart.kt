package io.hobbyful.cartService.cart

import io.hobbyful.cartService.lineitem.LineItem
import io.hobbyful.cartService.shippingRate.ShippingRate
import io.hobbyful.cartService.summary.Summary
import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.MongoId
import java.time.Instant

/**
 * 장바구니
 */
@Document
data class Cart(
    /**
     * 장바구니 ID
     */
    @MongoId
    val id: ObjectId? = null,

    /**
     * 고객 ID
     */
    @Indexed
    val customerId: String,

    /**
     * 주문 상품 리스트
     */
    val lineItems: List<LineItem>,

    /**
     * 배송비 정책
     */
    val shippingRate: ShippingRate,

    /**
     * 정산 요약
     */
    val summary: Summary,

    /**
     * 최초 장바구니 생성일
     */
    @CreatedDate
    val createdAt: Instant? = null,

    /**
     * 최근 장바구니 수정일
     */
    @LastModifiedDate
    val updatedAt: Instant? = null
) {
    /**
     * 장바구니 Factory
     */
    companion object {
        /**
         * 고객 장바구니 entity 생성
         */
        fun of(customerId: String): Cart =
            ShippingRate.ofHobbyful().let { shippingRate ->
                Cart(
                    customerId = customerId,
                    lineItems = emptyList(),
                    shippingRate = shippingRate,
                    summary = Summary.of(shippingRate = shippingRate)
                )
            }
    }
}

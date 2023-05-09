package io.hobbyful.cartService.cart

import io.hobbyful.cartService.catalog.product.Product
import io.hobbyful.cartService.catalog.variant.Variant
import io.hobbyful.cartService.shippingRate.ShippingRate
import io.hobbyful.cartService.summary.Summary
import org.bson.types.ObjectId

/**
 * 장바구니 Response view
 */
data class CartView(
    /**
     * 장바구니 ID
     */
    val id: ObjectId,

    /**
     * 준비물 리스트
     */
    val products: List<ProductView>,

    /**
     * 주문 상품 리스트
     */
    val lineItems: List<LineItemView>,

    /**
     * 배송비 정책
     */
    val shippingRate: ShippingRate,

    /**
     * 장바구니 요약
     */
    val summary: Summary
) {
    data class ProductView(
        /**
         * 준비물 ID
         */
        val id: ObjectId,

        /**
         * 브랜드 정보
         */
        val brand: Product.Brand,

        /**
         * 준비물 이름
         */
        val name: String,

        /**
         * 메인 이미지
         */
        val featuredImage: String,

        /**
         * 구매 선택 여부
         */
        val picked: Boolean
    )

    data class LineItemView(
        /**
         * 준비물 ID
         */
        val productId: ObjectId,

        /**
         * 상품 정보
         */
        val variant: Variant,

        /**
         * 구매수량
         */
        val quantity: Int,

        /**
         * 주문 품목 총액
         */
        val total: Int,

        /**
         * 주문품목의 구매선택 여부
         */
        val picked: Boolean
    )
}

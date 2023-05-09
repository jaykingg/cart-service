package io.hobbyful.cartService.catalog.variant

import org.bson.types.ObjectId

/**
 * 상품 정보
 */
data class Variant(
    /**
     * 상품 ID
     */
    val id: ObjectId,

    /**
     * 브랜드 ID
     */
    val brandId: ObjectId?,

    /**
     * 준비물 ID
     */
    val productId: ObjectId?,

    /**
     * 상품 이름
     */
    val name: String,

    /**
     * 기본 가격
     */
    val basePrice: Int,

    /**
     * 판매 가격
     */
    val price: Int,

    /**
     * 준비물의 필수 상품 여부
     */
    val primary: Boolean,

    /**
     * 재고관리 번호
     */
    val sku: String,

    /**
     * 재고 보유 상태
     */
    val inStock: Boolean
)

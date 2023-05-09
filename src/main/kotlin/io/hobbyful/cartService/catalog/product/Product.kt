package io.hobbyful.cartService.catalog.product

import org.bson.types.ObjectId

/**
 * 준비물 정보
 */
data class Product(
    /**
     * 준비물 ID
     */
    val id: ObjectId,

    /**
     * 브랜드 정보
     */
    val brand: Brand,

    /**
     * 준비물 이름
     */
    val name: String,

    /**
     * 브랜드 정산율
     */
    val brandAdjustmentRate: Double,

    /**
     * 메인 이미지
     */
    val featuredImage: String,

    /**
     * 재생목록 URL
     */
    val videoPlaylist: String?
) {
    data class Brand(
        /**
         * 브랜드 ID
         */
        val id: ObjectId,

        /**
         * 브랜드 이름
         */
        val name: String,
    )
}

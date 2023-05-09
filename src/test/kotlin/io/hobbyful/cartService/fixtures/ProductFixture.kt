package io.hobbyful.cartService.fixtures

import io.hobbyful.cartService.catalog.product.Product
import org.bson.types.ObjectId

/**
 * 준비물 fixture
 */
inline fun product(block: ProductFixtureBuilder.() -> Unit = {}) =
    ProductFixtureBuilder().apply(block).build()

class ProductFixtureBuilder {
    var id: ObjectId = ObjectId.get()
    var brand: Product.Brand = brand()
    var name = "test-product-name"
    var brandAdjustmentRate: Double = 0.0
    var featuredImage = "https://picsum.photos/300"
    var videoPlaylist: String? = "https://www.youtube.com"

    fun build() = Product(
        id = id,
        brand = brand,
        name = name,
        brandAdjustmentRate = brandAdjustmentRate,
        featuredImage = featuredImage,
        videoPlaylist = videoPlaylist
    )
}

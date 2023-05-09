package io.hobbyful.cartService.fixtures

import io.hobbyful.cartService.catalog.product.Product
import org.bson.types.ObjectId

/**
 * 브랜드 fixture
 */
inline fun brand(block: BrandFixtureBuilder.() -> Unit = {}) = BrandFixtureBuilder().apply(block).build()

class BrandFixtureBuilder {
    var id: ObjectId = ObjectId.get()
    var name: String = "test-brand-name"

    fun build() = Product.Brand(
        id = id,
        name = name
    )
}

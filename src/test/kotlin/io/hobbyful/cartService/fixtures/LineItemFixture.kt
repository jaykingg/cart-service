package io.hobbyful.cartService.fixtures

import io.hobbyful.cartService.catalog.product.Product
import io.hobbyful.cartService.catalog.variant.Variant
import io.hobbyful.cartService.lineitem.LineItem

/**
 * 주문품목 fixture
 */
inline fun lineItem(block: LineItemFixtureBuilder.() -> Unit = {}) =
    LineItemFixtureBuilder().apply(block).build()

class LineItemFixtureBuilder {
    var product: Product = product()
    var variant: Variant = variant()
    var quantity: Int = 1
    var picked: Boolean = true

    fun build() = LineItem(
        product = product,
        variant = variant,
        quantity = quantity,
        picked = picked,
        total = variant.price * quantity
    )
}

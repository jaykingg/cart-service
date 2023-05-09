package io.hobbyful.cartService.fixtures

import io.hobbyful.cartService.cart.Cart
import io.hobbyful.cartService.lineitem.LineItem
import io.hobbyful.cartService.shippingRate.ShippingRate
import io.hobbyful.cartService.summary.Summary
import org.bson.types.ObjectId
import java.util.*

inline fun cart(block: CartFixture.() -> Unit = {}) = CartFixture().apply(block).build()
class CartFixture {
    var id: ObjectId = ObjectId.get()
    var customerId: String = UUID.randomUUID().toString()
    var lineItems: List<LineItem> = listOf(lineItem(), lineItem(), lineItem())
    var shippingRate: ShippingRate = shippingRateFixture()
    var summary: Summary = summary()

    fun build() = Cart(
        id = id,
        customerId = customerId,
        lineItems = lineItems,
        shippingRate = shippingRate,
        summary = summary
    )
}

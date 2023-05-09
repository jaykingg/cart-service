package io.hobbyful.cartService.fixtures

import io.hobbyful.cartService.shippingRate.ShippingRate

inline fun shippingRateFixture(block: ShippingRateFixture.() -> Unit = {}) = ShippingRateFixture().apply(block).build()
class ShippingRateFixture {
    var baseCost: Int = 0
    var jejuCost: Int = 0
    var minOrderAmount: Int = 0

    fun build() = ShippingRate(
        baseCost = baseCost,
        jejuCost = jejuCost,
        minOrderAmount = minOrderAmount
    )
}

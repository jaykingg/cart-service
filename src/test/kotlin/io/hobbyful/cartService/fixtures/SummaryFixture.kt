package io.hobbyful.cartService.fixtures

import io.hobbyful.cartService.summary.Summary

inline fun summary(block: SummaryFixture.() -> Unit = {}) = SummaryFixture().apply(block).build()

class SummaryFixture {
    var subtotal: Int = 0
    var shippingCost: Int = 0
    var total: Int = 0

    fun build() = Summary(
        subtotal = subtotal,
        shippingCost = shippingCost,
        total = total
    )
}

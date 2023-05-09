package io.hobbyful.cartService.fixtures

import io.hobbyful.cartService.eventStream.OrderPlacedPayload
import io.hobbyful.cartService.lineitem.LineItem
import org.bson.types.ObjectId
import java.util.*

inline fun orderPlacedPayload(block: OrderPlacedPayloadFixture.() -> Unit = {}) = OrderPlacedPayloadFixture().apply(block).build()

class OrderPlacedPayloadFixture {
    var orderId: ObjectId = ObjectId.get()
    var customerId: String = UUID.randomUUID().toString()
    var cartId: ObjectId = ObjectId.get()
    var lineItems: List<LineItem> = listOf(lineItem(), lineItem(), lineItem())

    fun build() = OrderPlacedPayload(
        orderId = orderId,
        customerId = customerId,
        cartId = cartId,
        lineItems = lineItems
    )
}
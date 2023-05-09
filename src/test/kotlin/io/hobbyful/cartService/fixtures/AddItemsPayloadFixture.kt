package io.hobbyful.cartService.fixtures

import io.hobbyful.cartService.lineitem.AddItemsPayload
import org.bson.types.ObjectId

inline fun addItemPayload(block: AddItemsPayloadFixture.() -> Unit = {}) = AddItemsPayloadFixture().apply(block).build()

class AddItemsPayloadFixture {
    var productId: ObjectId = ObjectId.get()
    var items: List<AddItemsPayload.Item> = listOf(item())

    fun build() = AddItemsPayload(
        productId = productId,
        items = items
    )

}

package io.hobbyful.cartService.fixtures

import io.hobbyful.cartService.lineitem.AddItemsPayload
import org.bson.types.ObjectId

inline fun item(block: ItemFixture.() -> Unit = {}) = ItemFixture().apply(block).build()
class ItemFixture {
    var variantId: ObjectId = ObjectId.get()
    var quantity: Int = 1

    fun build() = AddItemsPayload.Item(
        variantId = variantId,
        quantity = quantity
    )
}
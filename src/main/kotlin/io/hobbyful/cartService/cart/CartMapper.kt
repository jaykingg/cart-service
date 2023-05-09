package io.hobbyful.cartService.cart

import io.hobbyful.cartService.catalog.product.Product
import io.hobbyful.cartService.lineitem.LineItem
import org.mapstruct.Mapper

@Mapper
abstract class CartMapper {
    fun toView(cart: Cart): CartView = cart.run {
        CartView(
            id = id!!,
            products = lineItems.distinctBy(LineItem::productId).map { productView(it.product, it.picked) },
            lineItems = lineItemViews(lineItems),
            shippingRate = shippingRate,
            summary = summary
        )
    }

    protected abstract fun lineItemViews(lineItems: Collection<LineItem>): List<CartView.LineItemView>

    protected abstract fun productView(product: Product, picked: Boolean): CartView.ProductView
}

package io.hobbyful.cartService.cart

import io.hobbyful.cartService.catalog.product.ProductPicksPayload
import io.hobbyful.cartService.catalog.product.ProductService
import io.hobbyful.cartService.catalog.product.pickedByProductId
import io.hobbyful.cartService.catalog.product.productIds
import io.hobbyful.cartService.catalog.variant.VariantService
import io.hobbyful.cartService.core.BadRequestException
import io.hobbyful.cartService.lineitem.AddItemsPayload
import io.hobbyful.cartService.lineitem.LineItem
import io.hobbyful.cartService.lineitem.quantityByVariantId
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.bson.types.ObjectId
import org.springframework.stereotype.Service

@Service
class CartFacadeService(
    private val cartService: CartService,
    private val productService: ProductService,
    private val variantService: VariantService
) {
    /**
     * 고객 장바구니 조회
     *
     * @param customerId 고객 ID
     */
    suspend fun getCart(customerId: String): Cart =
        cartService.getByCustomerId(customerId) ?: cartService.createEmptyCart(customerId)

    /**
     * 고객 장바구니에 주문상품 추가
     *
     * @param customerId 고객 ID
     * @param payload Request Payload
     */
    suspend fun addLineItems(customerId: String, payload: AddItemsPayload): Cart {
        val product = productService.getById(payload.productId)
        val variants = variantService.getAllByIds(payload.quantityByVariantId.keys)
        val lineItems = variants.map { variant ->
            LineItem.of(
                product = product,
                variant = variant,
                quantity = payload.quantityByVariantId[variant.id]!!,
                picked = true
            )
        }

        return cartService.save(getCart(customerId).add(lineItems.toList()))
    }

    /**
     * 고객 장바구니에 포함된 준비물의 선택 여부 변경
     *
     * @param payload 준미불 선택 여부 변경 payload
     */
    suspend fun updateProductPicks(customerId: String, payload: ProductPicksPayload): Cart =
        cartService.getByProductIds(customerId, payload.productIds).let { cart ->
            cartService.save(cart.updatePickedProducts(payload.pickedByProductId))
        }

    /**
     * 고객 장바구니에 포함된 주문상품의 구매수량 수정
     *
     * @param customerId 고객 Id
     * @param variantId 주문상품 Id
     * @param quantity 수량
     */
    suspend fun updateLineItemQuantity(customerId: String, variantId: ObjectId, quantity: Int): Cart =
        cartService.getByVariantId(customerId, variantId).let {
            cartService.save(it.updateQuantity(variantId, quantity))
        }

    /**
     * 고객 장바구니에 포함된 주문상품 제거
     *
     * @param customerId 고객 Id
     * @param variantId 주문상품 Id
     */
    suspend fun removeLineItem(customerId: String, variantId: ObjectId): Cart =
        cartService.getByVariantId(customerId, variantId).let {
            cartService.save(it.removeLineItem(variantId))
        }

    /**
     * 고객 장바구니에서 선택된 준비물 제거
     *
     * @param customerId 고객 Id
     * @param productIds 제거할 준비물 Id 리스트
     */
    suspend fun removeProductsByIds(customerId: String, productIds: List<ObjectId>): Cart {
        if (productIds.isEmpty()) throw BadRequestException()
        return cartService.getByProductIds(customerId, productIds).let { cart ->
            cartService.save(cart.removeProducts(productIds))
        }
    }

    /**
     * 모든 준비물 제거
     *
     * @param customerId 고객 Id
     */
    suspend fun removeAllProducts(customerId: String): Cart =
        getCart(customerId).let {
            cartService.save(it.removeAllProducts())
        }

}

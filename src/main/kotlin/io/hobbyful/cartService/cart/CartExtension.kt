package io.hobbyful.cartService.cart

import io.hobbyful.cartService.lineitem.LineItem
import io.hobbyful.cartService.lineitem.setPicked
import io.hobbyful.cartService.lineitem.setQuantity
import io.hobbyful.cartService.summary.Summary
import org.bson.types.ObjectId

/**
 * 정산 요약 최신화
 */
fun Cart.updateSummary(): Cart = copy(
    summary = Summary.of(
        subtotal = lineItems.filter(LineItem::picked).sumOf(LineItem::total),
        shippingRate = shippingRate
    )
)

/**
 * 준비물 선택 여부 수정
 */
fun Cart.updatePickedProducts(pickedByProductId: Map<ObjectId, Boolean>): Cart = copy(
    lineItems = lineItems.map { item ->
        item.setPicked(pickedByProductId[item.productId] ?: item.picked)
    }
)

/**
 * 구매 품목의 수량 변경
 */
fun Cart.updateQuantity(variantId: ObjectId, quantity: Int): Cart = copy(
    lineItems = lineItems.map { lineItem ->
        if (lineItem.variantId != variantId) {
            lineItem
        } else {
            lineItem.setQuantity(quantity)
        }
    }
)

/**
 * 같은 주문 상품의 경우 `quantity` 및 `total`만 합산하고, 새로운 상품은 추가
 */
fun Cart.add(newItems: List<LineItem>): Cart = copy(
    lineItems = (lineItems + newItems).groupBy { it.variantId }.map { (_, items) ->
        items.first().setQuantity(items.sumOf { it.quantity })
    }
)

/**
 * 주문상품 제거
 *
 * @param variantId 주문상품 Id
 */
fun Cart.removeLineItem(variantId: ObjectId): Cart = copy(
    lineItems = lineItems.filterNot { it.variantId == variantId }
)

/**
 * 모든 준비물 제거
 *
 * NOTE: 모든 준비물 제거는 사실상 모든 주문상품 제거와 동일
 */
fun Cart.removeAllProducts(): Cart = copy(
    lineItems = emptyList()
)

/**
 * 준비물 Id 리스트 기반 선택된 준비물 제거
 *
 * @param productIds 제거할 준비물 Id 리스트
 */
fun Cart.removeProducts(productIds: List<ObjectId>): Cart = copy(
    lineItems = lineItems.filterNot { it.productId in productIds }
)

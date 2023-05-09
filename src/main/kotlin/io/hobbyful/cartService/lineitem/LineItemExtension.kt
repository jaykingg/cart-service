package io.hobbyful.cartService.lineitem

/**
 * 구매 선택 여부 수정
 */
fun LineItem.setPicked(picked: Boolean): LineItem = copy(
    picked = picked
)

/**
 * 구매 수량 변경 및 품목 총액 업데이트
 */
fun LineItem.setQuantity(quantity: Int): LineItem = copy(
    quantity = quantity,
    total = variant.price * quantity
)

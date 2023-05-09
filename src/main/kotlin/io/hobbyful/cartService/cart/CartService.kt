package io.hobbyful.cartService.cart

import io.hobbyful.cartService.core.NotFoundException
import org.bson.types.ObjectId
import org.springframework.stereotype.Service

@Service
class CartService(
    private val cartRepository: CartRepository
) {
    /**
     * 고객의 새로운 장바구니 생성
     *
     * @param customerId 구매자 ID
     */
    suspend fun createEmptyCart(customerId: String) = save(Cart.of(customerId))

    /**
     * 장바구니 저장
     *
     * @param cart 장바구니
     */
    suspend fun save(cart: Cart) = cartRepository.save(cart.updateSummary())

    /**
     * 고객 장바구니 조회
     *
     * @param customerId 구매자 ID
     */
    suspend fun getByCustomerId(customerId: String): Cart? = cartRepository.findByCustomerId(customerId)

    /**
     * 복수의 준비물 ID 기반 고객 장바구니 조회
     *
     * @param customerId 고객 ID
     * @param productIds 준비물 IDs
     * @exception [NotFoundException] 고객 장바구니에 요청한 준비물이 존재하지 않을때
     */
    suspend fun getByProductIds(customerId: String, productIds: Collection<ObjectId>): Cart =
        cartRepository.findByCustomerIdAndProductIds(customerId, productIds) ?: throw NotFoundException()

    /**
     * 상품 ID 기반 고객 장바구니 조회
     *
     * @param customerId 고객 ID
     * @param variantId 상품 ID
     * @exception [NotFoundException] 고객 장바구니에 요청한 상품이 존재하지 않을때
     */
    suspend fun getByVariantId(customerId: String, variantId: ObjectId): Cart =
        cartRepository.findByCustomerIdAndVariantId(customerId, variantId) ?: throw NotFoundException()
}

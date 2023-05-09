package io.hobbyful.cartService.catalog.variant

import io.hobbyful.cartService.core.NotFoundException
import kotlinx.coroutines.flow.Flow
import org.bson.types.ObjectId
import org.springframework.stereotype.Service

@Service
class VariantService(
    private val variantRepository: VariantRepository
) {
    /**
     * 준비물 ID + 주문상품 ID 리스트로 모든 상품 조회
     *
     * @param variantIds 주문상품 ID 리스트
     * @exception [NotFoundException] productId + variantId 조합으로 존재하지 않는 주문상품이 있는 경우
     */
    suspend fun getAllByIds(variantIds: Collection<ObjectId>): Flow<Variant> =
        variantRepository.getAllViewsByIdIn(variantIds)
}

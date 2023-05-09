package io.hobbyful.cartService.catalog.product

import io.hobbyful.cartService.core.NotFoundException
import org.bson.types.ObjectId
import org.springframework.stereotype.Service

@Service
class ProductService(
    private val productRepository: ProductRepository
) {
    suspend fun getById(productId: ObjectId): Product =
        productRepository.findById(productId) ?: throw NotFoundException()
}

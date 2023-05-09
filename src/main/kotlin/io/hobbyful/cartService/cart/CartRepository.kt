package io.hobbyful.cartService.cart

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface CartRepository : CoroutineCrudRepository<Cart, ObjectId> {
    suspend fun findByCustomerId(customerId: String): Cart?

    @Query("{ 'customerId': ?0, 'lineItems.product._id': { \$all: ?1 } }")
    suspend fun findByCustomerIdAndProductIds(customerId: String, productIds: Collection<ObjectId>): Cart?

    @Query("{ 'customerId' : ?0, 'lineItems.variant._id': ?1 }")
    suspend fun findByCustomerIdAndVariantId(customerId: String, variantId: ObjectId): Cart?
}

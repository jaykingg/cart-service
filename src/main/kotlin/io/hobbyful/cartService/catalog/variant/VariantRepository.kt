package io.hobbyful.cartService.catalog.variant

import kotlinx.coroutines.flow.Flow
import org.bson.types.ObjectId
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface VariantRepository : CoroutineCrudRepository<Variant, ObjectId> {
    fun getAllViewsByIdIn(variantIds: Collection<ObjectId>): Flow<Variant>
}

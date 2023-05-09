package io.hobbyful.cartService.database

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.mongodb.reactivestreams.client.MongoDatabase
import io.mongock.api.annotations.ChangeUnit
import io.mongock.api.annotations.Execution
import io.mongock.api.annotations.RollbackExecution
import io.mongock.driver.mongodb.reactive.util.MongoSubscriberSync
import org.bson.Document
import org.springframework.core.io.ClassPathResource

/**
 * `lineItem.variant`에 `productId` 및 `brandId` 필드를 추가합니다.
 * Destructive migration에 해당하지 않기에 rollback 로직은 추가하지 않습니다.
 */
@ChangeUnit(id = "cart-change-00002", order = "00002")
class CartChange00002 {
    private val resource = ClassPathResource("migration/cart-change-00002.json")

    @Execution
    fun execution(
        mapper: ObjectMapper,
        mongoDatabase: MongoDatabase,
    ) {
        val query = mapper.readValue<JsonQuery>(resource.file)

        mongoDatabase.getCollection("cart")
            .updateMany(query.filter, query.update)
            .subscribe(MongoSubscriberSync())
    }

    @RollbackExecution
    fun rollback() = Unit

    private data class JsonQuery(
        val filter: Document,
        val update: List<Document>
    )
}

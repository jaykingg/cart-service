package io.hobbyful.cartService.database

import com.mongodb.reactivestreams.client.MongoDatabase
import io.mongock.api.annotations.ChangeUnit
import io.mongock.api.annotations.Execution
import io.mongock.api.annotations.RollbackExecution
import io.mongock.driver.mongodb.reactive.util.MongoSubscriberSync

/**
 * 주문상품/준비물/브랜드 정보에 적용된 `index`들을 제거합니다.
 */
@ChangeUnit(id = "cart-change-00001", order = "00001")
class CartChange00001 {
    @Execution
    fun execution(mongoDatabase: MongoDatabase) {
        val subscriber = MongoSubscriberSync<Void>()

        mongoDatabase.getCollection("cart").apply {
            dropIndex("lineItems.product.brand._id").subscribe(subscriber)
            dropIndex("lineItems.product.brand.name").subscribe(subscriber)
        }

        /**
         * NOTE: 일반적으로 모든 종류의 Runtime Exception은 명시적인 handling 로직이 반드시 필요합니다.
         * 다만, index 제거와 같이 exception이 발생할 수 있는 케이스가 명확하고 무시해도 괜찮은 경우
         * `runCatching`으로 감싸 Runtime Exception을 흘려보낼 수 있습니다.
         */
        subscriber.runCatching { await() }
    }

    @RollbackExecution
    fun rollback() = Unit
}

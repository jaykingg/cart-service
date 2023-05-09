package io.hobbyful.cartService.eventStream

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.Message

@Configuration
class OrderPlacedHandler(
    private val orderPlacedService: OrderPlacedService
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Bean
    fun orderPlacedConsumer(): suspend (Flow<Message<OrderPlacedPayload>>) -> Unit = { flow ->
        flow
            .transform { message ->
                message
                    .runCatching { payload }
                    .onFailure { log.error("처리할 수 없는 message입니다", it) }
                    .onSuccess { emit(it) }
            }
            .collect { payload ->
                payload.runCatching {
                    orderPlacedService.clearPurchasedProducts(payload)
                }.onFailure {
                    log.error("order-placed 이벤트 처리에 실패하였습니다", it)
                }
            }
    }
}

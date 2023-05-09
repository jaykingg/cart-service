package io.hobbyful.cartService.eventStream

import io.hobbyful.cartService.cart.CartRepository
import io.hobbyful.cartService.fixtures.cart
import io.hobbyful.cartService.fixtures.lineItem
import io.hobbyful.cartService.fixtures.orderPlacedPayload
import io.kotest.assertions.timing.eventually
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.stream.binder.test.InputDestination
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration
import org.springframework.context.annotation.Import
import org.springframework.messaging.support.MessageBuilder
import java.util.*
import kotlin.time.Duration.Companion.seconds

@SpringBootTest
@Import(TestChannelBinderConfiguration::class)
class ClearPurchaseProductsHandlerTest(
    private val cartRepository: CartRepository,
    private val inputDestination: InputDestination
) : BehaviorSpec({
    val destination = "order-placed"

    Given("ClearPurchaseProductsHandler Test") {
        When("payload 가 유효하지 않을 때") {
            val customerId = UUID.randomUUID().toString()
            val payload = orderPlacedPayload{
                this.customerId = customerId
                lineItems = emptyList()
            }
            val payloadBuilder = MessageBuilder
                .withPayload(payload)
                .build()

            val cart = cart {
                this.customerId = customerId
                lineItems = listOf(lineItem(), lineItem())
            }

            beforeEach {
                cartRepository.save(cart)
            }

            afterEach {
                cartRepository.deleteAll()
            }

            Then("payload 의 준비물, 장바구니에서 삭제") {
                inputDestination.send(payloadBuilder, destination)
                val result = cartRepository.findByCustomerId(customerId)
                result!!.lineItems.size shouldBe cart.lineItems.size

            }
        }

        When("isOk") {
            val payload = orderPlacedPayload()
            val payloadBuilder = MessageBuilder
                .withPayload(payload)
                .build()

            val cart = cart {
                customerId = payload.customerId
                lineItems = payload.lineItems.plus(lineItem()).plus(lineItem())
            }

            beforeEach {
                cartRepository.save(cart)
            }

            afterEach {
                cartRepository.deleteAll()
            }

            Then("payload 의 준비물, 장바구니에서 삭제") {
                inputDestination.send(payloadBuilder, destination)
                eventually(5.seconds) {
                    val result = cartRepository.findByCustomerId(payload.customerId)
                    result!!.lineItems.size shouldBe cart.lineItems.size - payload.lineItems.size
                }
            }
        }
    }
})
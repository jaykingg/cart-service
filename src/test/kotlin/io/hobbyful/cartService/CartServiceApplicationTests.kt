package io.hobbyful.cartService

import io.hobbyful.cartService.cart.CartView
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration
import org.springframework.context.annotation.Import
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockOpaqueToken
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody

@SpringBootTest
@AutoConfigureWebTestClient
@Import(TestChannelBinderConfiguration::class)
class CartServiceApplicationTests(
    private val webTestClient: WebTestClient
) : DescribeSpec({
    describe("GET /cart - 장바구니 조회") {
        context("인증되지 않은 요청") {
            it("401 Unauthorized") {
                webTestClient.get()
                    .uri("/cart")
                    .exchange()
                    .expectStatus().isUnauthorized
            }
        }

        context("인증된 요청") {
            val opaqueToken = mockOpaqueToken().authorities(SimpleGrantedAuthority("customer"))

            it("고객의 새로운 장바구니를 생성한다") {
                webTestClient
                    .mutateWith(opaqueToken)
                    .get().uri("/cart")
                    .exchange()
                    .expectStatus().isOk
                    .expectBody<CartView>()
                    .returnResult()
                    .responseBody!!.apply {
                        products.shouldBeEmpty()
                        lineItems.shouldBeEmpty()
                        shippingRate.apply {
                            baseCost shouldBe 3_000
                            jejuCost shouldBe 3_000
                            minOrderAmount shouldBe 50_000
                        }
                    }
            }
        }
    }
})

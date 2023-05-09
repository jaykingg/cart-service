package io.hobbyful.cartService.api

import io.hobbyful.cartService.cart.CartRepository
import io.hobbyful.cartService.cart.CartView
import io.hobbyful.cartService.catalog.product.ProductRepository
import io.hobbyful.cartService.catalog.variant.VariantRepository
import io.hobbyful.cartService.core.SecurityConstants
import io.hobbyful.cartService.fixtures.addItemPayload
import io.hobbyful.cartService.fixtures.product
import io.hobbyful.cartService.fixtures.variant
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration
import org.springframework.context.annotation.Import
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockOpaqueToken
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import java.util.*

@SpringBootTest
@AutoConfigureWebTestClient
@Import(TestChannelBinderConfiguration::class)
class AddLineItemsIT(
    private val cartRepository: CartRepository,
    private val productRepository: ProductRepository,
    private val variantRepository: VariantRepository,
    private val webTestClient: WebTestClient
) : BehaviorSpec({
    val path = "/cart/line-items"
    val customerId = UUID.randomUUID().toString()

    fun getOpaqueToken(authority: String = SecurityConstants.CUSTOMER) = mockOpaqueToken()
        .authorities(SimpleGrantedAuthority(authority))
        .attributes { it["sub"] = customerId }

    Given("API 요청 인증 실패") {
        When("Authentication에 실패한 경우") {
            val request = webTestClient
                .post().uri(path)
                .bodyValue(addItemPayload())

            Then("Response 401 UNAUTHORIZED") {
                request
                    .exchange()
                    .expectStatus().isUnauthorized
            }
        }

        When("Authorization에 실패한 경우") {
            val request = webTestClient
                .mutateWith(getOpaqueToken("unknown"))
                .post().uri(path)
                .bodyValue(addItemPayload())

            Then("Response 403 FORBIDDEN") {
                request
                    .exchange()
                    .expectStatus().isForbidden
            }
        }
    }

    Given("API 요청 인증 성공") {
        val request = webTestClient
            .mutateWith(getOpaqueToken("customer"))
            .post().uri(path)

        When("payload 가 유효하지 않을 때") {
            val payload = addItemPayload {
                items = emptyList()
            }

            Then("Response 400 BAD_REQUEST") {
                request
                    .bodyValue(payload)
                    .exchange()
                    .expectStatus().isBadRequest
            }
        }

        When("준비물이 존재하지 않을때") {
            val payload = addItemPayload()

            Then("Response 404 NOT_FOUND") {
                request
                    .bodyValue(payload)
                    .exchange()
                    .expectStatus().isNotFound
            }
        }

        When("isOk") {
            val payload = addItemPayload()
            val product = product {
                id = payload.productId
            }
            val variant = variant {
                id = payload.items[0].variantId
            }

            beforeEach {
                productRepository.save(product)
                variantRepository.save(variant)
            }

            afterEach {
                cartRepository.deleteAll()
                productRepository.deleteAll()
                variantRepository.deleteAll()
            }

            Then("Response 200 OK Return CartView") {
                request
                    .bodyValue(payload)
                    .exchange()
                    .expectStatus().isOk
                    .expectBody<CartView>()
                    .returnResult().responseBody
                    .shouldNotBeNull()

                val cart = cartRepository.findByCustomerId(customerId)!!
                cart.createdAt.shouldNotBeNull()
                cart.updatedAt.shouldNotBeNull()

            }
        }
    }
})

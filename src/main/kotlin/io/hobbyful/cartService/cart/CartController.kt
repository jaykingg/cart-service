package io.hobbyful.cartService.cart

import io.hobbyful.cartService.catalog.product.ProductPicksPayload
import io.hobbyful.cartService.core.subject
import io.hobbyful.cartService.lineitem.AddItemsPayload
import io.hobbyful.cartService.lineitem.LineItemQuery
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.bson.types.ObjectId
import org.springdoc.core.annotations.ParameterObject
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication
import org.springframework.web.bind.annotation.*

@Tag(name = "장바구니")
@SecurityRequirement(name = "aegis")
@RestController
@RequestMapping("/cart", produces = [APPLICATION_JSON_VALUE])
class CartController(
    private val mapper: CartMapper,
    private val cartFacadeService: CartFacadeService
) {
    @Operation(summary = "장바구니 조회")
    @GetMapping
    suspend fun activeCart(token: BearerTokenAuthentication): CartView =
        cartFacadeService.getCart(token.subject).run(mapper::toView)

    @Operation(summary = "준비물 선택여부 수정")
    @PutMapping("/products", consumes = [APPLICATION_JSON_VALUE])
    suspend fun updateProductPicks(
        token: BearerTokenAuthentication,
        @RequestBody @Valid
        payload: ProductPicksPayload
    ): CartView =
        cartFacadeService.updateProductPicks(token.subject, payload).run(mapper::toView)

    /**
     * - `productsId` parameter가 존재하면 **선택된 준비물**만 제거
     * - `productsId` parameter가 존재하지 않을 경우 **전체 준비물** 제거
     *
     * @param productIds 제거할 준비물의 Id 리스트
     */
    @Operation(summary = "준비물 제거")
    @DeleteMapping("/products")
    suspend fun deleteProducts(
        token: BearerTokenAuthentication,
        @RequestParam productIds: Set<ObjectId>?
    ): CartView {
        val cart = if (productIds == null) {
            cartFacadeService.removeAllProducts(token.subject)
        } else {
            cartFacadeService.removeProductsByIds(token.subject, productIds.toList())
        }

        return mapper.toView(cart)
    }

    @Operation(summary = "주문상품 담기")
    @PostMapping("/line-items", consumes = [APPLICATION_JSON_VALUE])
    suspend fun addLineItems(
        token: BearerTokenAuthentication,
        @RequestBody @Valid
        payload: AddItemsPayload
    ): CartView = cartFacadeService.addLineItems(token.subject, payload).run(mapper::toView)

    /**
     * @param variantId 주문품목의 상품 Id
     */
    @Operation(summary = "상품별 구매수량 수정")
    @PutMapping("/line-items/{variantId}")
    suspend fun updateLineItemQuantity(
        token: BearerTokenAuthentication,
        @PathVariable variantId: ObjectId,
        @ParameterObject @Valid
        query: LineItemQuery
    ): CartView =
        cartFacadeService.updateLineItemQuantity(token.subject, variantId, query.quantity).run(mapper::toView)

    /**
     * @param variantId 주문상품 Id
     */
    @Operation(summary = "단일 주문상품 제거")
    @DeleteMapping("/line-items/{variantId}")
    suspend fun deleteLineItem(
        token: BearerTokenAuthentication,
        @PathVariable variantId: ObjectId
    ): CartView = cartFacadeService.removeLineItem(token.subject, variantId).run(mapper::toView)
}

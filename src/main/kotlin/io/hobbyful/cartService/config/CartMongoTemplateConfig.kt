package io.hobbyful.cartService.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories

@Configuration
@EnableReactiveMongoRepositories(
    basePackages = ["io.hobbyful.cartService.cart"]
)
@EnableReactiveMongoAuditing
class CartMongoTemplateConfig

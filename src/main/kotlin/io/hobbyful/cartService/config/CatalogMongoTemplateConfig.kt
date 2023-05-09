package io.hobbyful.cartService.config

import com.mongodb.reactivestreams.client.MongoClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.SimpleReactiveMongoDatabaseFactory
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories

@Configuration
@EnableReactiveMongoRepositories(
    basePackages = ["io.hobbyful.cartService.catalog"],
    reactiveMongoTemplateRef = "catalogMongoTemplate"
)
class CatalogMongoTemplateConfig(
    private val mongoClient: MongoClient
) {
    @Bean
    fun catalogMongoTemplate(): ReactiveMongoTemplate = ReactiveMongoTemplate(
        SimpleReactiveMongoDatabaseFactory(mongoClient, "catalog-db")
    )
}

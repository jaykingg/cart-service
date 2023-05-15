import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
    id("org.springframework.boot") version "3.0.2"
    id("io.spring.dependency-management") version "1.1.0"
    id("org.springdoc.openapi-gradle-plugin") version "1.6.0"

    kotlin("jvm") version "1.7.22"
    kotlin("plugin.spring") version "1.7.22"
    kotlin("kapt") version "1.7.22"
}

group = "io.hobbyful"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

/**
 * Spring Dependency Management
 * https://docs.spring.io/spring-boot/docs/current/reference/html/dependency-versions.html
 */
dependencies {
    kapt("com.github.therapi:therapi-runtime-javadoc-scribe:0.15.0")
    kapt("org.mapstruct:mapstruct-processor:1.5.3.Final")
    kapt("org.springframework.boot:spring-boot-configuration-processor")

    // Bill of Materials (BOM)
    implementation(platform("io.awspring.cloud:spring-cloud-aws-dependencies:3.0.1"))
    implementation(platform("io.kotest:kotest-bom:5.5.4"))
    implementation(platform("io.mongock:mongock-bom:5.2.2"))
    implementation(platform("org.springdoc:springdoc-openapi:2.0.2"))
    implementation(platform("org.springframework.cloud:spring-cloud-dependencies:2022.0.0"))
    implementation(platform("org.testcontainers:testcontainers-bom:1.17.6"))

    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.awspring.cloud:spring-cloud-starter-aws-secrets-manager-config")
    implementation("io.mongock:mongock-springboot")
    implementation("io.mongock:mongodb-reactive-driver")
    implementation("io.netty:netty-resolver-dns-native-macos:4.1.85.Final:osx-aarch_64")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.mapstruct:mapstruct:1.5.3.Final")
    implementation("org.springdoc:springdoc-openapi-starter-common")
    implementation("org.springdoc:springdoc-openapi-starter-webflux-api")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap")
    implementation("org.springframework.cloud:spring-cloud-stream")
    implementation("org.springframework.cloud:spring-cloud-stream-binder-kafka-reactive")
    implementation("software.amazon.msk:aws-msk-iam-auth:1.1.5")

    runtimeOnly("com.nimbusds:oauth2-oidc-sdk:10.4")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    testImplementation("com.ninja-squad:springmockk:4.0.0")
    testImplementation("io.github.serpro69:kotlin-faker:1.13.0")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.2")
    testImplementation("io.kotest.extensions:kotest-extensions-testcontainers:1.3.4")
    testImplementation("io.kotest:kotest-assertions-core")
    testImplementation("io.kotest:kotest-framework-datatest")
    testImplementation("io.kotest:kotest-runner-junit5")
    testImplementation("io.mockk:mockk:1.13.3")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "mockito-core")
        exclude(module = "mockito-junit-jupiter")
    }
    testImplementation("org.springframework.cloud:spring-cloud-stream-test-binder")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.testcontainers:mongodb")
}

/**
 * Kapt 설정
 */
kapt {
    arguments {
        /**
         * Mapstruct Configurations
         */
        arg("mapstruct.defaultComponentModel", "spring")
        arg("mapstruct.unmappedSourcePolicy", "IGNORE")
        arg("mapstruct.unmappedTargetPolicy", "ERROR")
    }
}

openApi {
    customBootRun {
        args.add("--spring.profiles.active=oas")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.named<BootBuildImage>("bootBuildImage") {
    imageName.set(System.getenv("IMAGE_NAME"))
    builder.set("paketobuildpacks/builder:tiny")
    buildpacks.set(
        listOf(
            "docker://gcr.io/paketo-buildpacks/amazon-corretto",
            "docker://gcr.io/paketo-buildpacks/java",
            "docker://gcr.io/paketo-buildpacks/health-checker"
        )
    )
    environment.set(
        mapOf(
            "BP_JVM_VERSION" to "17",
            "BP_HEALTH_CHECKER_DEPENDENCY" to "thc",
            "BP_HEALTH_CHECKER_ENABLED" to "true",
            "THC_PATH" to "/actuator/health/liveness",
        )
    )

    docker {
        publishRegistry {
            url.set(System.getenv("DOCKER_REGISTRY"))
            username.set(System.getenv("DOCKER_USERNAME"))
            password.set(System.getenv("DOCKER_PASSWORD"))
        }
    }
}

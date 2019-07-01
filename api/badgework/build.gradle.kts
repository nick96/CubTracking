import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

repositories {
    mavenCentral()
    jcenter()
}

plugins {
    id("org.jetbrains.kotlin.jvm").version("1.3.31")
    id("org.springframework.boot").version("2.1.6.RELEASE")
//    id("jacoco")
    application
    java
    checkstyle
}

apply(plugin = "io.spring.dependency-management")

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
}


dependencies {
    val springbootVer = "2.1.6.RELEASE"
    val kotlinVer = "1.3.40"
    val hikakuVer = "2.2.0"

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVer")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVer")

    implementation("org.springframework.boot:spring-boot-starter-web:$springbootVer")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:$springbootVer")
    implementation("org.springframework.boot:spring-boot-starter-security:$springbootVer")

    implementation("com.auth0:auth0-spring-security-api:1.2.3")

    runtime("com.h2database:h2:1.4.199")

    testImplementation("de.codecentric.hikaku:hikaku-openapi:$hikakuVer")
    testImplementation("de.codecentric.hikaku:hikaku-spring:$hikakuVer")

    testImplementation("org.springframework.boot:spring-boot-starter-test:$springbootVer")

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.junit.platform:junit-platform-launcher")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
}

application {
    mainClassName = "me.nspain.cubtracking.badgework.BadgeworkApplicationKt"
}

tasks.withType<Test> {
    useJUnitPlatform()
}
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm").version("1.3.41")
    id("org.springframework.boot").version("2.1.6.RELEASE")
//    id("io.spring.dependency-management").version("1.0.6.RELEASE")
    id("com.google.cloud.tools.jib").version("1.4.0")

    application
    java
    idea
}

repositories {
    mavenCentral()
    jcenter()
    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    val springbootVer = "2.1.6.RELEASE"
    val springSecVer = "5.1.5.RELEASE"
    val kotlinVer = "1.3.41"
    val hikakuVer = "2.3.0"
    val junitVer = "5.5.1"
    val h2Ver = "1.4.199"
    val auth0Ver = "1.+"

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVer")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVer")

    implementation("org.springframework.boot:spring-boot-starter-web:$springbootVer")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:$springbootVer")
    implementation("org.springframework.boot:spring-boot-starter-security:$springbootVer")

    implementation("com.auth0:auth0-spring-security-api:$auth0Ver")
    implementation("com.auth0:auth0:$auth0Ver")

    runtime("com.h2database:h2:$h2Ver")

    testImplementation("de.codecentric.hikaku:hikaku-openapi:$hikakuVer")
    testImplementation("de.codecentric.hikaku:hikaku-spring:$hikakuVer")

    testImplementation("org.springframework.boot:spring-boot-starter-test:$springbootVer")
    testImplementation("org.springframework.boot:spring-boot-starter-webflux:$springbootVer")
    testImplementation("org.springframework.security:spring-security-test:$springSecVer")


    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVer")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVer")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:$kotlinVer")
}

application {
    mainClassName = "me.nspain.cubtracking.badgework.BadgeworkApplicationKt"
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    withType<Test> {
        useJUnitPlatform()
    }
}
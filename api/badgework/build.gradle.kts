import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm").version("1.3.41")
    id("org.springframework.boot").version("2.1.6.RELEASE")
    application
    java
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    val springbootVer = "2.1.6.RELEASE"
    val kotlinVer = "1.3.41"
    val hikakuVer = "2.3.0"
    val junitLauncherVer = "1.5.0"
    val junitJupiterVer = "5.5.0"
    val h2Ver = "1.4.199"

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVer")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVer")

    implementation("org.springframework.boot:spring-boot-starter-web:$springbootVer")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:$springbootVer")

    runtime("com.h2database:h2:$h2Ver")

    testImplementation("de.codecentric.hikaku:hikaku-openapi:$hikakuVer")
    testImplementation("de.codecentric.hikaku:hikaku-spring:$hikakuVer")

    testImplementation("org.springframework.boot:spring-boot-starter-test:$springbootVer")
    testImplementation("org.junit.platform:junit-platform-launcher:$junitLauncherVer")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:$junitJupiterVer")
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
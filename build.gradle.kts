plugins {
	java
	id("org.springframework.boot") version "3.5.7"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.iljaproject"
version = "0.0.1-SNAPSHOT"
description = "Highly efficient URL Shortener service built with Spring Boot"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.flywaydb:flyway-core")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("org.postgresql:postgresql")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

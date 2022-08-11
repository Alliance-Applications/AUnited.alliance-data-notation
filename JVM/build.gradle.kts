import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

repositories {
    gradlePluginPortal()
    mavenCentral()
    mavenLocal()
    maven {
        url = uri("https://maven.pkg.jetbrains.space/alliance-software/p/dngames/kyukez")
        credentials {
            username = "4f275f73-3678-49d0-b749-16a16347d97f"
            password = "eyJhbGciOiJSUzUxMiJ9.eyJzdWIiOiI0ZjI3NWY3My0zNjc4LTQ5ZDAtYjc0OS0xNmExNjM0N2Q5N2YiLCJhdWQiOiI0ZjI3NWY3My0zNjc4LTQ5ZDAtYjc0OS0xNmExNjM0N2Q5N2YiLCJvcmdEb21haW4iOiJhbGxpYW5jZS1zb2Z0d2FyZSIsIm5hbWUiOiJLeXVrZXoiLCJpc3MiOiJodHRwczpcL1wvYWxsaWFuY2Utc29mdHdhcmUuamV0YnJhaW5zLnNwYWNlIiwicGVybV90b2tlbiI6IjVXRzRqME9OSkc0IiwicHJpbmNpcGFsX3R5cGUiOiJTRVJWSUNFIiwiaWF0IjoxNjQzNjI5MzAzfQ.kFXjgVOK7uf71hZZMsPISzhV6OnUkXw3SFsHPj9HjlE1o2C9vqyzkIyG7tCHyzIl8e-qNsXTJk4qOOmpGyxeDlKu5Qenhjdt9usUwLSCPzRMfhxliUyYC7U58I0cmLz5qazR69dKKwL9g9oqk_b_qNCv1XMj4uFPLEVBHv_uI2A"
        }
    }
}

plugins {
    java
    `java-library`
    kotlin("jvm") version "1.7.20-Beta"

    // Plugin which checks for dependency updates with help/dependencyUpdates task.
    id("com.github.ben-manes.versions") version "0.42.0"

    // Plugin which can update Gradle dependencies, use help/useLatestVersions
    id("se.patrikerdes.use-latest-versions") version "0.2.18"

    `maven-publish`

    // Test coverage
    jacoco
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        mavenCentral()
        mavenLocal()
        maven {
            url = uri("https://maven.pkg.jetbrains.space/alliance-software/p/dngames/kyukez")
            credentials {
                username = "4f275f73-3678-49d0-b749-16a16347d97f"
                password = "eyJhbGciOiJSUzUxMiJ9.eyJzdWIiOiI0ZjI3NWY3My0zNjc4LTQ5ZDAtYjc0OS0xNmExNjM0N2Q5N2YiLCJhdWQiOiI0ZjI3NWY3My0zNjc4LTQ5ZDAtYjc0OS0xNmExNjM0N2Q5N2YiLCJvcmdEb21haW4iOiJhbGxpYW5jZS1zb2Z0d2FyZSIsIm5hbWUiOiJLeXVrZXoiLCJpc3MiOiJodHRwczpcL1wvYWxsaWFuY2Utc29mdHdhcmUuamV0YnJhaW5zLnNwYWNlIiwicGVybV90b2tlbiI6IjVXRzRqME9OSkc0IiwicHJpbmNpcGFsX3R5cGUiOiJTRVJWSUNFIiwiaWF0IjoxNjQzNjI5MzAzfQ.kFXjgVOK7uf71hZZMsPISzhV6OnUkXw3SFsHPj9HjlE1o2C9vqyzkIyG7tCHyzIl8e-qNsXTJk4qOOmpGyxeDlKu5Qenhjdt9usUwLSCPzRMfhxliUyYC7U58I0cmLz5qazR69dKKwL9g9oqk_b_qNCv1XMj4uFPLEVBHv_uI2A"
            }
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "11"
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "11"
}

tasks {
    create<Delete>("clean-output") {
        group = "clean"
        delete(rootProject.buildDir)
    }

    test {
        useJUnitPlatform {
            includeEngines("junit-jupiter")
            // excludeEngines("junit-vintage")
        }

        systemProperty("junit.jupiter.conditions.deactivate", "*")
        systemProperty("junit.jupiter.extensions.autodetection.enabled", true)
        systemProperty("junit.jupiter.testinstance.lifecycle.default", "per_class")
    }
}

afterEvaluate {
    publishing {
        publications {
            repositories {
                maven {
                    url = uri("")
                    credentials {
                        username = "e1aad9c9-b16f-4fbe-a4ac-05aa27af9a52"
                        password = "58014d491e86822f911a6612a947a3cf1b337d06fa745c541d63537ed3c65bbb"
                    }
                }
            }

            create<MavenPublication>("maven") {
                groupId = "io.alliance.toolset"
                artifactId = "adn"
                version = "1.0.0-SNAPSHOT"
                from(components["java"])
            }
        }
    }
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")

    testCompileOnly("org.projectlombok:lombok:1.18.24")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.24")

    implementation("org.jetbrains:annotations:23.0.0")

    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation(kotlin("test"))
    implementation(kotlin("test-junit"))

    // JUnit 5
    testImplementation(platform("org.junit:junit-bom:5.9.0"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine:5.9.0")
    testRuntimeOnly("org.junit.platform:junit-platform-console:1.9.0")
    testRuntimeOnly("org.junit.platform:junit-platform-engine:1.9.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.9.0")
    testRuntimeOnly("org.junit.platform:junit-platform-reporting:1.9.0")
    testRuntimeOnly("org.junit.platform:junit-platform-commons:1.9.0")

    // Kotlintest
    testImplementation("io.kotlintest:kotlintest-core:3.4.2")
    testImplementation("io.kotlintest:kotlintest-assertions:3.4.2")
    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.4.2")
}
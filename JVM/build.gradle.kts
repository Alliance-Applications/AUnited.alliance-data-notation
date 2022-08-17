import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val metaGroupId: String by extra { "io.alliance.toolset" }
val metaArtifactId: String by extra { "adn-jvm" }
val metaVersionName: String by extra { "1.0.1-FIXUP" }
val metaVersionCode: Int by extra { 1 }

plugins {
    // Languages
    java
    `java-library`
    kotlin("jvm") version "1.7.20-Beta"

    // Testing
    jacoco

    // Publishing
    `maven-publish`

    // QOL
    id("com.github.ben-manes.versions") version "0.42.0"
    id("se.patrikerdes.use-latest-versions") version "0.2.18"
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks {
    withType<KotlinCompile>().all {
        kotlinOptions {
            jvmTarget = "11"
        }
    }

    withType<AbstractPublishToMaven> {
        dependsOn("test")
    }

    create<Delete>("clean-output") {
        group = "clean"
        delete(rootProject.buildDir)
    }

    test {
        useJUnitPlatform {
            includeEngines("junit-jupiter")
            excludeEngines("junit-vintage")
        }

        systemProperty("junit.jupiter.conditions.deactivate", "*")
        systemProperty("junit.jupiter.extensions.autodetection.enabled", true)
        systemProperty("junit.jupiter.testinstance.lifecycle.default", "per_class")
    }
}

afterEvaluate {
    publishing {
        repositories {
            maven {
                credentials {
                    // Automation has a special account for authentication in Space
                    // account credentials are accessible via env vars
                    username = System.getenv("JB_SPACE_CLIENT_ID")
                    password = System.getenv("JB_SPACE_CLIENT_SECRET")
                }

                url = uri("https://maven.pkg.jetbrains.space/alliance-software/p/alliance-public/maven")
            }
        }

        publications {
            create<MavenPublication>("maven") {
                groupId = metaGroupId
                artifactId = metaArtifactId
                version = metaVersionName
                from(components["java"])

                pom.name.set("Alliance data notation")
                pom.description.set("A description of my library")
            }
        }
    }
}

dependencies {
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
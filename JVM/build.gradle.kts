// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        mavenCentral()
        maven {
            url = uri("https://maven.pkg.jetbrains.space/alliance-software/p/dngames/kyukez")
            credentials {
                username = "4f275f73-3678-49d0-b749-16a16347d97f"
                password = "eyJhbGciOiJSUzUxMiJ9.eyJzdWIiOiI0ZjI3NWY3My0zNjc4LTQ5ZDAtYjc0OS0xNmExNjM0N2Q5N2YiLCJhdWQiOiI0ZjI3NWY3My0zNjc4LTQ5ZDAtYjc0OS0xNmExNjM0N2Q5N2YiLCJvcmdEb21haW4iOiJhbGxpYW5jZS1zb2Z0d2FyZSIsIm5hbWUiOiJLeXVrZXoiLCJpc3MiOiJodHRwczpcL1wvYWxsaWFuY2Utc29mdHdhcmUuamV0YnJhaW5zLnNwYWNlIiwicGVybV90b2tlbiI6IjVXRzRqME9OSkc0IiwicHJpbmNpcGFsX3R5cGUiOiJTRVJWSUNFIiwiaWF0IjoxNjQzNjI5MzAzfQ.kFXjgVOK7uf71hZZMsPISzhV6OnUkXw3SFsHPj9HjlE1o2C9vqyzkIyG7tCHyzIl8e-qNsXTJk4qOOmpGyxeDlKu5Qenhjdt9usUwLSCPzRMfhxliUyYC7U58I0cmLz5qazR69dKKwL9g9oqk_b_qNCv1XMj4uFPLEVBHv_uI2A"
            }
        }
    }
}

plugins {
    id("java-library")
    `maven-publish`
}

tasks.create<Delete>("clean-output") {
    group = "clean"
    delete(rootProject.buildDir)
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

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.22")
    annotationProcessor("org.projectlombok:lombok:1.18.22")

    implementation("org.jetbrains:annotations:23.0.0")

    testCompileOnly("org.projectlombok:lombok:1.18.22")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.22")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://maven.pkg.jetbrains.space/alliance-software/p/dngames/kyukez")
        credentials {
            username = "4f275f73-3678-49d0-b749-16a16347d97f"
            password = "eyJhbGciOiJSUzUxMiJ9.eyJzdWIiOiI0ZjI3NWY3My0zNjc4LTQ5ZDAtYjc0OS0xNmExNjM0N2Q5N2YiLCJhdWQiOiI0ZjI3NWY3My0zNjc4LTQ5ZDAtYjc0OS0xNmExNjM0N2Q5N2YiLCJvcmdEb21haW4iOiJhbGxpYW5jZS1zb2Z0d2FyZSIsIm5hbWUiOiJLeXVrZXoiLCJpc3MiOiJodHRwczpcL1wvYWxsaWFuY2Utc29mdHdhcmUuamV0YnJhaW5zLnNwYWNlIiwicGVybV90b2tlbiI6IjVXRzRqME9OSkc0IiwicHJpbmNpcGFsX3R5cGUiOiJTRVJWSUNFIiwiaWF0IjoxNjQzNjI5MzAzfQ.kFXjgVOK7uf71hZZMsPISzhV6OnUkXw3SFsHPj9HjlE1o2C9vqyzkIyG7tCHyzIl8e-qNsXTJk4qOOmpGyxeDlKu5Qenhjdt9usUwLSCPzRMfhxliUyYC7U58I0cmLz5qazR69dKKwL9g9oqk_b_qNCv1XMj4uFPLEVBHv_uI2A"
        }
    }
}
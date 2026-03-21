import com.google.protobuf.gradle.*
import org.gradle.kotlin.dsl.protobuf

plugins {
    id("org.jetbrains.kotlin.jvm") version "2.3.20"
    id("org.jetbrains.kotlin.plugin.allopen") version "2.3.20"
    id("org.jetbrains.kotlin.kapt") version "2.3.20"
    id("io.micronaut.application") version "4.6.2"
    id("com.google.protobuf") version "0.9.6"
    id("com.gradleup.shadow") version "8.3.9"
}

version = "0.1"
group = "com.example"


val kotlinVersion=project.properties.get("kotlinVersion")

repositories {
    mavenCentral()
}

dependencies {
    kapt("io.micronaut:micronaut-http-validation")
    kapt("io.micronaut.openapi:micronaut-openapi")
    kapt("io.micronaut.serde:micronaut-serde-processor")
    kapt("io.micronaut.data:micronaut-data-processor")
    kapt("io.micronaut.validation:micronaut-validation-processor")
    kapt("io.micronaut.security:micronaut-security-annotations")
    kapt("io.micronaut.tracing:micronaut-tracing-opentelemetry-annotation")

    // GRPC
    implementation("io.micronaut.grpc:micronaut-grpc-runtime")
    implementation("io.micronaut.grpc:micronaut-grpc-client-runtime")
    implementation("io.micronaut.grpc:micronaut-grpc-server-runtime")
    api(libs.grpc)
    api(libs.grpc.kt)
    implementation(libs.kotlinx.coroutines.core)
    implementation("io.micronaut:micronaut-discovery-core")
    implementation("io.micronaut.grpc:micronaut-grpc-runtime")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("io.micronaut.serde:micronaut-serde-jackson")
    implementation("javax.annotation:javax.annotation-api")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")
    runtimeOnly("ch.qos.logback:logback-classic")
    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

sourceSets {
    main {
        proto {
            srcDirs(
                "${rootDir.absolutePath}/protorepo/compliance",
                "${rootDir.absolutePath}/protorepo/company-registry",
                "${rootDir.absolutePath}/protorepo/common"
            )
        }

        java {
            srcDirs(
                "build/generated/source/proto/main/grpc",
                "build/generated/source/proto/main/grpckt",
                "build/generated/source/proto/main/java"
            )
        }
    }
}

application {
    mainClass = "com.example.ApplicationKt"
}

java {
    sourceCompatibility = JavaVersion.toVersion("21")
}






sourceSets {
    main {
        java {
            srcDirs("build/generated/source/proto/main/grpc")
            srcDirs("build/generated/source/proto/main/java")
        }
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.25.8"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.75.0"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:1.5.0:jdk8@jar"
        }
    }
    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins {
                // Apply the "grpc" plugin whose spec is defined above, without options.
                id("grpc")
                id("grpckt")
            }
        }
    }
}



//kapt {
//    correctErrorTypes = true
//    keepJavacAnnotationProcessors = true
//}
//
//tasks.matching { it.name.startsWith("kapt") }.configureEach {
//    dependsOn("generateProto")
//}

micronaut {
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("com.example.*")
    }
}


tasks.named<io.micronaut.gradle.docker.NativeImageDockerfile>("dockerfileNative") {
    jdkVersion = "21"
}








plugins {
    val kotlinVersion = "1.9.0"
    kotlin("plugin.serialization") version kotlinVersion

    id("net.mamoe.mirai-console") version "2.16.0"
    id("org.jetbrains.kotlin.jvm") version "1.9.0"
    id("com.github.johnrengelman.shadow") version "8.0.0"
    id("io.freefair.lombok") version "8.6"
    id("maven-publish")
}

dependencies {
    val ktor_version = "2.3.5"
    val overflow_version = "0.9.9.515-f8d867b-SNAPSHOT"
    implementation(kotlin("stdlib"))
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("ch.qos.logback:logback-classic:1.4.12")
    implementation("com.github.promeg:tinypinyin:2.0.3")
    implementation("com.github.promeg:tinypinyin-lexicons-java-cncity:2.0.3")
    implementation("org.json:json:20231013")
    implementation("org.apache.httpcomponents:httpclient:4.5.14")
    implementation("org.jsoup:jsoup:1.15.3")
    implementation("io.github.mymonstercat:rapidocr-onnx-windows-x86_64:1.2.2")
    implementation("io.github.mymonstercat:rapidocr-onnx-linux-x86_64:1.2.2")
    implementation("org.xerial:sqlite-jdbc:3.46.0.0")
    implementation("org.apache.commons:commons-lang3:3.16.0")
    implementation("org.apache.commons:commons-imaging:1.0-alpha1")
    implementation("ai.djl.onnxruntime:onnxruntime-engine:0.23.0")
    implementation(platform("ai.djl:bom:0.23.0"))
    implementation("ai.djl.tensorflow:tensorflow-engine:0.23.0")
    implementation("ai.djl.tensorflow:tensorflow-model-zoo:0.23.0")
    implementation("ai.djl.tensorflow:tensorflow-native-cpu:2.10.0:win-x86_64")
    implementation("ai.djl.tensorflow:tensorflow-native-cpu:2.10.0:linux-x86_64")

    compileOnly("top.mrxiaom.mirai:overflow-core-api:$overflow_version")
    compileOnly("top.mrxiaom.mirai:overflow-core:$overflow_version")
    compileOnly("org.projectlombok:lombok:1.18.34")

    compileOnly(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
}

mirai {
    noTestCore = true
    setupConsoleTestRuntime {
        // 移除 mirai-core 依赖
        classpath = classpath.filter {
            !it.nameWithoutExtension.startsWith("mirai-core-jvm") ||
                    !it.nameWithoutExtension.startsWith("overflow-api")
        }
    }
}

version = "0.3.5"

repositories {
    maven("https://maven.aliyun.com/repository/public") // 阿里云国内代理仓库
    maven { url = uri("https://repo.repsy.io/mvn/chrynan/public") }
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots")
    mavenCentral()
}

mirai {
    jvmTarget = JavaVersion.VERSION_11
}

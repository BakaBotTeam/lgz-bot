plugins {
    val kotlinVersion = "1.9.0"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("net.mamoe.mirai-console") version "2.16.0"
    id("com.github.johnrengelman.shadow") version "1.2.3"
}

dependencies {
    val ktor_version = "2.3.5"
    val overflow_version = "2.16.0-eb376cc-SNAPSHOT"
    implementation(kotlin("stdlib"))
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("ch.qos.logback:logback-classic:1.2.11")
    implementation("com.github.promeg:tinypinyin:2.0.3")
    implementation("com.github.promeg:tinypinyin-lexicons-java-cncity:2.0.3")
    implementation("org.json:json:20230227")
    implementation("org.apache.httpcomponents:httpclient:4.5.14")
    implementation("org.jsoup:jsoup:1.15.3")

    compileOnly("top.mrxiaom:overflow-core-api:$overflow_version")
    testConsoleRuntime("top.mrxiaom:overflow-core:$overflow_version")
}

mirai {
    noTestCore = true
    setupConsoleTestRuntime {
        // 移除 mirai-core 依赖
        classpath = classpath.filter {
            !it.nameWithoutExtension.startsWith("mirai-core-jvm")
        }
    }
}

version = "0.3.3"

repositories {
    maven("https://maven.aliyun.com/repository/public") // 阿里云国内代理仓库
    maven { url = uri("https://repo.repsy.io/mvn/chrynan/public") }
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots")
    mavenCentral()
}

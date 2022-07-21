plugins {
    val kotlinVersion = "1.6.10"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("net.mamoe.mirai-console") version "2.11.1"
}

dependencies {
    api("com.github.promeg:tinypinyin:2.0.3")
    api("com.github.promeg:tinypinyin-lexicons-java-cncity:2.0.3")
    api("org.json:json:20220320")
}

version = "0.1.2"

repositories {
    maven("https://maven.aliyun.com/repository/public") // 阿里云国内代理仓库
    mavenCentral()
}

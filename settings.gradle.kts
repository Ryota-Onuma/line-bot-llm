rootProject.name = "line-bot-llm"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            version("kotlin", "1.9.22")
            version("ktor", "2.3.7")
            
            plugin("kotlin.jvm", "org.jetbrains.kotlin.jvm").versionRef("kotlin")
            plugin("ktor", "io.ktor.plugin").versionRef("ktor")
            
            library("ktor.server.core", "io.ktor", "ktor-server-core").versionRef("ktor")
            library("ktor.server.netty", "io.ktor", "ktor-server-netty").versionRef("ktor")
            library("ktor.server.double.receive", "io.ktor", "ktor-server-double-receive").versionRef("ktor")
            library("ktor.line.webhook.plugin", "io.ktor", "ktor-server-line-webhook").versionRef("ktor")
            library("ktor.server.openapi", "io.ktor", "ktor-server-openapi").versionRef("ktor")
            library("ktor.server.config.yaml", "io.ktor", "ktor-server-config-yaml").versionRef("ktor")
            library("ktor.server.test.host", "io.ktor", "ktor-server-test-host").versionRef("ktor")
            
            library("logback.classic", "ch.qos.logback", "logback-classic").version("1.4.11")
            library("kotlin.test.junit", "org.jetbrains.kotlin", "kotlin-test-junit").versionRef("kotlin")
        }
    }
}

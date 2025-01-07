import com.adarshr.gradle.testlogger.theme.ThemeType

plugins {
    id("java")
    id("io.freefair.lombok") version "8.11"
    id("com.adarshr.test-logger") version "4.0.0"
}

group = "com.hetacz"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    implementation("org.seleniumhq.selenium:selenium-java:4.27.0")
    testImplementation("org.testng:testng:7.10.2")
    implementation("com.google.inject:guice:7.0.0")
    implementation("org.slf4j:slf4j-api:2.1.0-alpha1")
    runtimeOnly("ch.qos.logback:logback-classic:1.5.15")
    implementation("io.github.bonigarcia:webdrivermanager:5.9.2")
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.14")
    testImplementation("org.assertj:assertj-core:3.27.2")
}

tasks.test {
    useTestNG {
        suites(System.getProperty("suiteFile") ?: "src/test/resources/testng.xml")
    }
    testlogger {
        theme = ThemeType.STANDARD
        showStackTraces = true
        slowThreshold = 10000
    }
    systemProperties = System.getProperties().map { e -> Pair(e.key as String, e.value) }.toMap()
}

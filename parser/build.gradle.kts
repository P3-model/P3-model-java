plugins {
    id("java")
    id("java-library")
    id("maven-publish")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

group = "org.p3model"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
}


dependencies {
    implementation("io.github.classgraph:classgraph:4.8.162")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.p3model:p3-model-annotations:0.1-SNAPSHOT")
    implementation("com.scalified:tree:0.2.5")
    implementation("org.slf4j:slf4j-api:2.0.10")
    implementation("ch.qos.logback:logback-classic:1.4.14")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:3.24.2")
    testImplementation("io.hosuaby:inject-resources-junit-jupiter:0.3.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")


}

tasks.test {
    useJUnitPlatform()
}

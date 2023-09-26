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
    implementation("org.p3model:p3-model-annotations:0.1-SNAPSHOT")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

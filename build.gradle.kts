plugins {
    `java-library`
    `maven-publish`
}

repositories {
    mavenCentral();
}

dependencies {
    implementation("org.yaml:snakeyaml:1.18")
    compileOnly("com.google.guava:guava:19.0")
    compileOnly("com.google.auto.service:auto-service:1.0-rc7") {
        exclude(group = "org.checkerframework", module = "checker-qual")
    }
    compileOnly("com.squareup:javapoet:1.13.0")
}

group = "com.forestfull.devops"
version = "1.0.5"
description = "KoLogger"
java.sourceCompatibility = JavaVersion.VERSION_1_6

java{
    withJavadocJar()
    withSourcesJar()
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc>() {
    options.encoding = "UTF-8"
}
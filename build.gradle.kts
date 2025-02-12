plugins {
    `java-library`
    `maven-publish`
}

repositories {
    mavenLocal()
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

dependencies {
    annotationProcessor("com.google.auto.service:auto-service:1.1.1")
    implementation("com.google.auto.service:auto-service:1.1.1")
    implementation("com.squareup:javapoet:1.13.0")
    implementation("org.yaml:snakeyaml:1.18")
}

group = "com.forestfull.devops"
version = "1.0.5"
description = "KoLogger"
java.sourceCompatibility = JavaVersion.VERSION_1_6

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc>() {
    options.encoding = "UTF-8"
}
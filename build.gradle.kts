/*
 * This file was generated by the Gradle 'init' task.
 *
 * This is a general purpose Gradle build.
 * Learn how to create Gradle builds at https://guides.gradle.org/creating-new-gradle-builds/
 */

plugins {
    `java`
}

repositories {
    maven {
        url = uri("http://first.wpi.edu/FRC/roborio/maven/release/")
    }
}

val wpilibVersion by extra("2019.1.1-beta-2")

dependencies {
    compile("edu.wpi.first.wpilibj:wpilibj-java:$wpilibVersion")
}

sourceSets {
    main {
        java {
            srcDir("src/")
        }
    }
}

task("printProps") {
    doLast {
    }
}

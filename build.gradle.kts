/*
 * This file was generated by the Gradle 'init' task.
 *
 * This is a general purpose Gradle build.
 * Learn how to create Gradle builds at https://guides.gradle.org/creating-new-gradle-builds/
 */

group = "com.spikes2212"


plugins {
    `java-library`
    `maven-publish`
}

repositories {
    mavenCentral()
    maven {
        url = uri("http://first.wpi.edu/FRC/roborio/maven/release/")
    }

    maven {
        url = uri("http://devsite.ctr-electronics.com/maven/release/")
    }
}

val wpilibVersion by extra("2019.2.1")
val cscoreVersion by extra("1.3.0")
val opencvVersion by extra("3.4.3")
val guiceVersion by extra("4.2.2")
val ctreVersion by extra("5.14.1")

dependencies {
    api("edu.wpi.first.wpilibj:wpilibj-java:$wpilibVersion")
    api("edu.wpi.first.cscore:cscore-java:$cscoreVersion")
    api("edu.wpi.first.thirdparty.frc2019.opencv:opencv-java:$opencvVersion")
    api("edu.wpi.first.cameraserver:cameraserver-java:$wpilibVersion")
    api("com.google.inject:guice:$guiceVersion")
    api("com.ctre.phoenix:wpiapi-java:$ctreVersion")
    api("com.ctre.phoenix:api-java:$ctreVersion")
}


sourceSets {
    main {
        java {
            srcDir("src/")
        }
    }
}


publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.spikes2212"
            artifactId = "sl"
            version = "4.0.2"

            from(components["java"])
        }
    }

    repositories {
        maven {
            name = "publish"
            // we wont store our url here, you aren't allowed to publish artifacts to our repo.
            url = uri("")
        }
    }
}
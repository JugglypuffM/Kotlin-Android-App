import com.google.protobuf.gradle.id

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.protobuf") version "0.9.4"
}

android {
    namespace = "com.project.kotlin_android_app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.project.kotlin_android_app"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    implementation(libs.grpc.okhttp.v1570)
    implementation(libs.grpc.protobuf.lite)
    implementation(libs.grpc.stub)
    implementation(libs.javax.annotation.api)
    implementation(libs.protobuf.javalite.v3234)
    implementation(libs.perfmark.api)
    implementation(libs.okio)
    
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:4.28.2"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.56.0"
        }
    }
    generateProtoTasks {
        all().forEach { task ->
            task.plugins {
                id("grpc"){
                    option("lite")
                }
            }

            task.builtins {
                id("java"){
                    option("lite")
                }
            }
        }
    }
}

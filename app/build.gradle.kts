plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
}

android {
  namespace = "me.chentao.redpacket"
  compileSdk = 33

  defaultConfig {
    applicationId = "me.chentao.redpacket"
    minSdk = 24
    targetSdk = 33
    versionCode = 100
    versionName = "1.0.0"
  }

  signingConfigs {
    create("config") {
      keyAlias = "red"
      keyPassword = "leochan"
      storeFile = file("../red.jks")
      storePassword = "leochan"
    }
  }

  buildFeatures {
    viewBinding {
      enable = true
    }
  }

  buildTypes {

    val config = signingConfigs.getByName("config")

    debug {
      signingConfig = config
      isMinifyEnabled = false
    }

    release {
      signingConfig = config
      isMinifyEnabled = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = "1.8"
  }

  val appName = "让红包飞"
  applicationVariants.all {
    val variant = this
    variant.outputs
      .map { it as com.android.build.gradle.internal.api.BaseVariantOutputImpl }
      .forEach { output ->
        val outputFileName = "$appName-${variant.versionName}.apk"
        output.outputFileName = outputFileName
      }
  }

}

dependencies {
  implementation("androidx.appcompat:appcompat:1.6.1")
  implementation("com.google.android.material:material:1.9.0")
  implementation("androidx.constraintlayout:constraintlayout:2.1.4")
  implementation("com.jakewharton.timber:timber:5.0.1")
  implementation("com.google.android.flexbox:flexbox:3.0.0")

}
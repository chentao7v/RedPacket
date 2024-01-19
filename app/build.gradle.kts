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
    versionCode = 90
    versionName = "1.0.0"

    resourceConfigurations += listOf("zh-rCN")
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
      isShrinkResources = true
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
  implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

  implementation("com.squareup.retrofit2:retrofit:2.9.0")
  implementation("com.google.code.gson:gson:2.10.1")
  implementation("com.squareup.retrofit2:converter-gson:2.9.0")
  implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")

  // RxJava
  implementation("io.reactivex.rxjava3:rxjava:3.1.6")
  implementation("io.reactivex.rxjava3:rxandroid:3.0.2")
  implementation("com.squareup.retrofit2:adapter-rxjava3:2.9.0")
  implementation("com.jakewharton.rxrelay3:rxrelay:3.0.1")
  implementation("com.jakewharton.rxrelay3:rxrelay:3.0.1")

  implementation("com.uber.autodispose2:autodispose-android:2.1.1")
  implementation("com.uber.autodispose2:autodispose-androidx-lifecycle:2.1.1")

  implementation("com.github.bumptech.glide:glide:4.16.0")
  implementation("com.drakeet.multitype:multitype:4.3.0")

  implementation("io.github.scwang90:refresh-layout-kernel:2.1.0")
  implementation("io.github.scwang90:refresh-header-material:2.1.0")
  implementation("io.github.scwang90:refresh-footer-classics:2.1.0")

}
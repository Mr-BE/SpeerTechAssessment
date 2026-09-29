plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
//    alias(libs.plugins.javaagent)
}

android {
    // FIX: namespace must match the Kotlin source package; it resolves ".MainActivity" in the manifest.
    // With "dev.mrbe..." the app crashed on launch (ClassNotFoundException). applicationId alone sets the Play Store ID.
    namespace = "com.example.speertechassessment"
    compileSdk = 35

    defaultConfig {
        applicationId = "dev.mrbe.speertechassessment"
        minSdk = 28
        targetSdk = 35
        versionCode = 3 // re-release: 2 (1.1) crashed on launch
        versionName = "1.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            // OPT: R8 code + resource shrinking for release (was off). Gson keep rules live in proguard-rules.pro
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }

    tasks.withType<Test> {
        jvmArgs("-XX:+EnableDynamicAgentLoading")
        // FIX: Compose UI tests need the host activity from the debug-only ui-test-manifest; release can't run them
        if (name == "testReleaseUnitTest") filter.excludeTestsMatching("*SearchScreenTest")
    }
    //
      testOptions.unitTests.isIncludeAndroidResources = true
}


dependencies {
//retrofit
    implementation (libs.retrofit2)
    implementation(libs.gson)

    //compose Nav
    implementation(libs.composeNav)

//    coil
    implementation(libs.coilNetwork)
    implementation(libs.coilCompose)

    //splash
    implementation(libs.splash)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose) // OPT: collectAsStateWithLifecycle
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // FIX: the Compose UI test lib was an `implementation` dep, shipping JUnit + Espresso in the release APK
    testImplementation(libs.androidx.ui.test.junit4)
    testImplementation(libs.robolectric)
    testImplementation(libs.coroutinesTest)
    testImplementation(libs.mockk)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    testImplementation(kotlin("test"))
}
plugins {
    id(libs.plugins.convention.app.get().pluginId)
}

android {
    packaging {
        resources {
            excludes.addAll(setOf("META-INF/LICENSE.txt", "META-INF/NOTICE.txt", "META-INF/LICENSE"))
        }
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(projects.kotlinextensions)
    implementation(projects.security)
    implementation(projects.retrofit)
    implementation(projects.coroutines)
    implementation(projects.rx)
    implementation(projects.viewbinding)
    implementation(projects.customviews)
    implementation(projects.recyclerview)
    implementation(projects.biometrics)
    implementation(projects.internetDetector)
    implementation(projects.activity)
    implementation(projects.context)
    implementation(projects.common)
    implementation(projects.animations)
    implementation(projects.view)
    implementation(projects.collections)
    implementation(projects.lifecycle)
    implementation(projects.fragment)

    testImplementation(libs.junit)

    implementation(libs.converter.moshi)
    implementation(libs.retrofit)
    implementation(libs.adapter.rxjava3)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

    //joda time
    implementation(libs.android.joda)

    //moshi
    implementation(libs.moshi.kotlin)
    ksp(libs.moshi.kotlin.codegen)


    // glide
    implementation(libs.glide)
    ksp(libs.glide.compiler)

    //ui
    implementation(libs.androidx.vectordrawable)

    implementation(libs.androidx.constraintlayout)

    //nav
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    //coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)

    //auth
    implementation(libs.androidx.biometric)

    //util
    implementation(libs.androidx.exifinterface)

    //core
    implementation(libs.androidx.preference)
    implementation(libs.androidx.palette.ktx)

    implementation(libs.androidx.core.ktx)


    //android apis
    implementation(libs.androidx.viewpager2)
    implementation(libs.material)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.documentfile)

    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.activity.ktx)

    //room
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    testImplementation(libs.junit)
    testImplementation(libs.hamcrest.all)
    testImplementation(libs.androidx.junit.ktx)
    testImplementation(libs.androidx.test.core.ktx)
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.kotlinx.coroutines.test)

    // AndroidX Test - Instrumented testing
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

}
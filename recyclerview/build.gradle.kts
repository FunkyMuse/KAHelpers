plugins {
    id(libs.plugins.convention.library.get().pluginId)
}

android {
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.recyclerview)
    implementation(libs.material)
    implementation(libs.androidx.core.ktx)
}
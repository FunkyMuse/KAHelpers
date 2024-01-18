plugins {
    id(libs.plugins.convention.library.get().pluginId)
}

android {
    buildFeatures {
        viewBinding = true
    }
}
dependencies {
    implementation(projects.viewbinding)
    implementation(libs.material)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.fragment.ktx)
}
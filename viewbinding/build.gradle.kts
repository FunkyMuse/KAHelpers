plugins {
    id(libs.plugins.convention.library.get().pluginId)
}

android {
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.lifecycle.common.java8)
    implementation(libs.androidx.recyclerview)
}
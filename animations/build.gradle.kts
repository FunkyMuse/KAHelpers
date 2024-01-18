plugins {
    id(libs.plugins.convention.library.get().pluginId)
}

dependencies {
    implementation(projects.view)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.interpolator)
    implementation(libs.androidx.transition)
    implementation(libs.androidx.recyclerview)
}
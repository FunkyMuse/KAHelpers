plugins {
    id(libs.plugins.convention.library.get().pluginId)
}

dependencies {
    implementation(projects.common)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    implementation(libs.androidx.swiperefreshlayout)
}
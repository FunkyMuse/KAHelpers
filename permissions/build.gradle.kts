plugins {
    id(libs.plugins.convention.library.get().pluginId)
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
}
plugins {
    id(libs.plugins.convention.library.get().pluginId)
    id(libs.plugins.convention.compose.library.get().pluginId)
}

dependencies {
    implementation(libs.bundles.lifecycle)
    androidTestImplementation(libs.bundles.android.test)
}
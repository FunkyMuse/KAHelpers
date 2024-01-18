plugins {
    id(libs.plugins.convention.library.get().pluginId)
}

dependencies {
    implementation(projects.common)
    implementation(libs.androidx.collection.ktx)
    implementation(libs.androidx.core.ktx)
    testImplementation(libs.bundles.unit.test)
}
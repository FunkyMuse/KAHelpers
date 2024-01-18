plugins {
    id(libs.plugins.convention.library.get().pluginId)
}

dependencies {
    implementation(projects.string)
    implementation(projects.common)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.documentfile)
}

plugins {
    id(libs.plugins.convention.library.get().pluginId)
}

dependencies {
    implementation(projects.bytearray)
    implementation(projects.common)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
}
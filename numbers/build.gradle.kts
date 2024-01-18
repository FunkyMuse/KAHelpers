plugins {
    id(libs.plugins.convention.library.get().pluginId)
}

dependencies {
    implementation(projects.bytearray)
    implementation(projects.math)
    implementation(libs.androidx.annotation)
}
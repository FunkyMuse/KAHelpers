plugins {
    id(libs.plugins.convention.library.get().pluginId)
}

dependencies {
    implementation(projects.common)
    implementation(projects.numbers)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
}
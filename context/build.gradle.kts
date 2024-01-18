plugins {
    id(libs.plugins.convention.library.get().pluginId)
}

dependencies {
    implementation(projects.common)
    implementation(projects.contextGetters)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
}
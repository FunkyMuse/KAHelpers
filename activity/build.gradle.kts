plugins {
    id(libs.plugins.convention.library.get().pluginId)
}
dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(projects.common)
}
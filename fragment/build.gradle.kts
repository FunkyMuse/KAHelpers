plugins {
    id(libs.plugins.convention.library.get().pluginId)
}

dependencies {
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.appcompat)
    implementation(projects.common)
    implementation(libs.androidx.lifecycle.common.java8)
}

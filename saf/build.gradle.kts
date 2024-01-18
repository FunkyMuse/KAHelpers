plugins {
    id(libs.plugins.convention.library.get().pluginId)
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.exifinterface)
    implementation(libs.androidx.documentfile)
}

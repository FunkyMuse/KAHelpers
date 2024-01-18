plugins {
    id(libs.plugins.convention.library.get().pluginId)
}

dependencies {
    //security
    api(libs.androidx.security.crypto.ktx)
    testImplementation(libs.bundles.unit.test)
}
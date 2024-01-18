plugins {
    id(libs.plugins.convention.library.get().pluginId)
}

dependencies {
    implementation(projects.common)
    implementation(projects.coroutines)
    implementation(libs.androidx.lifecycle.lifecycle.viewmodel.savedstate)

    //retrofit
    api(libs.retrofit)
    api(libs.logging.interceptor)

    testImplementation(libs.bundles.unit.test)
}
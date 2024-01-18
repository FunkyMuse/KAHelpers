plugins {
    id(libs.plugins.convention.library.get().pluginId)
}

dependencies {
    //rx
    api(libs.rxkotlin)
    api(libs.rxandroid)

    implementation(libs.androidx.lifecycle.reactivestreams)
}
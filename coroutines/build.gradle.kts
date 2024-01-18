plugins {
    id(libs.plugins.convention.library.get().pluginId)
}

dependencies {
    //coroutines
    api(libs.kotlinx.coroutines.android)
    api(libs.kotlinx.coroutines.core)

    //live data
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.appcompat)
}
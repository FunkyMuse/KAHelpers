plugins {
    id(libs.plugins.convention.library.get().pluginId)
}

dependencies {
    implementation(projects.common)
    implementation(projects.context)
    implementation(projects.activity)
    implementation(projects.bytearray)
    implementation(projects.intent)
    implementation(projects.sharedpreferences)
    implementation(projects.color)

    //live data
    implementation(libs.androidx.lifecycle.lifecycle.viewmodel.savedstate)
    implementation(libs.androidx.lifecycle.reactivestreams)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.common.java8)
    implementation(libs.androidx.lifecycle.service)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.lifecycle.viewmodel.savedstate)
    implementation(libs.androidx.lifecycle.process)

    //ui
    implementation(libs.androidx.vectordrawable)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)

    //util
    implementation(libs.androidx.exifinterface)

    //core
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.preference.ktx)
    implementation(libs.androidx.palette.ktx)
    implementation(libs.androidx.core.ktx)

    //android apis
    implementation(libs.androidx.viewpager2)
    implementation(libs.androidx.documentfile)

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.appcompat.resources)

    testImplementation(libs.bundles.unit.test)
}

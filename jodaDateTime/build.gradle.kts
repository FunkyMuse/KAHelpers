plugins {
    id(libs.plugins.convention.library.get().pluginId)
}

android {
    packaging {
        resources {
            excludes.addAll(setOf("META-INF/LICENSE.txt", "META-INF/NOTICE.txt", "META-INF/LICENSE"))
        }
    }
}

dependencies {
    //joda time
    implementation(libs.android.joda)
}
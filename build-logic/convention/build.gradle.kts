plugins {
    `kotlin-dsl`
}

group = libs.versions.app.version.appId

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(libs.versions.app.build.kotlinJVMTarget.get()))
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidConventionComposeLibrary") {
            id = "convention.android.compose.library"
            implementationClass = "LibraryComposeConventionPlugin"
        }
        register("androidConventionComposeApp") {
            id = "convention.android.compose.app"
            implementationClass = "AppComposeConventionPlugin"
        }
        register("moshiConventionPlugin") {
            id = "convention.android.moshi"
            implementationClass = "MoshiConventionPlugin"
        }
        register("roomConventionPlugin") {
            id = "convention.android.room"
            implementationClass = "RoomConventionPlugin"
        }
        register("appConventionPlugin") {
            id = "convention.android.app"
            implementationClass = "AppConventionPlugin"
        }
        register("libraryConventionPlugin") {
            id = "convention.android.library"
            implementationClass = "LibraryConventionPlugin"
        }
        register("kotlinConventionLibraryPlugin") {
            id = "convention.android.kotlin.library"
            implementationClass = "KotlinLibraryConventionPlugin"
        }
    }
}

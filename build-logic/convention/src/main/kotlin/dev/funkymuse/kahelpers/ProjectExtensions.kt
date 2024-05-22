package dev.funkymuse.kahelpers

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.TestExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

fun CommonExtension<*, *, *, *, *, *>.configureBuildFeatures() {
    buildFeatures.apply {
        aidl = false
        renderScript = false
        resValues = false
        shaders = false
    }
}

fun LibraryExtension.addLibrariesConfig() {
    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
        }
    }
}


fun Project.configureKotlinOptions() {
    tasks
        .withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile>().configureEach {
            compilerOptions {
                jvmTarget.set(JvmTarget.valueOf(versionCatalog.getVersion("app-build-jVMTarget")))
                freeCompilerArgs.addAll(
                    listOf(
                        "-opt-in=kotlin.RequiresOptIn",
                        "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                        "-opt-in=kotlinx.coroutines.FlowPreview",
                        "-Xcontext-receivers"
                    )
                )
            }
        }
}

fun Project.configureJavaCompatibilityCompileOptions(commonExtensions: CommonExtension<*, *, *, *, *, *>) {
    commonExtensions.compileOptions {
        val currentJavaVersionFromLibs = JavaVersion.valueOf(
            versionCatalog.findVersion("app-build-javaVersion").get().toString()
        )
        sourceCompatibility = currentJavaVersionFromLibs
        targetCompatibility = currentJavaVersionFromLibs
    }
}

fun Project.configureAppPluginPackageAndNameSpace(
    commonExtensions: ApplicationExtension
) {
    commonExtensions.apply {
        namespace = packageName
        defaultConfig {
            applicationId = packageName
        }
    }
}

val Project.packageName get() = versionCatalog.getVersion("app-version-appId")

fun Project.commonVersioning(libraryExtension: LibraryExtension) {
    libraryExtension.apply {
        setCompileSdkVersion(getCompileSDKVersion())
        defaultConfig {
            minSdk = getMinSDKVersion()
            testInstrumentationRunner = getTestRunner()
        }
    }
}

fun Project.commonVersioning(libraryExtension: TestExtension) {
    libraryExtension.apply {
        setCompileSdkVersion(getCompileSDKVersion())
        defaultConfig {
            minSdk = getMinSDKVersion()
            testInstrumentationRunner = getTestRunner()
        }
    }
}

fun Project.commonVersioning(libraryExtension: ApplicationExtension) {
    libraryExtension.apply {
        compileSdk = getCompileSDKVersion()
        defaultConfig {
            minSdk = getMinSDKVersion()
            targetSdk = getMinSDKVersion()
            testInstrumentationRunner = getTestRunner()
            versionName = versionCatalog.getVersion("app-version-versionName")
            versionCode = versionCatalog.getVersion("app-version-versionCode").toInt()
        }
    }
}

fun Project.getTestRunner() = versionCatalog.getVersion("app-build-testRunner")

fun Project.getMinSDKVersion() = versionCatalog.getVersion("app-build-minimumSDK").toInt()
fun Project.getCompileSDKVersion() =
    versionCatalog.getVersion("app-build-compileSDKVersion").toInt()

fun Project.configureLibraryAndTestNameSpace() {
    configure<BaseExtension> {
        namespace = packageName.plus(path.replace(":", ".").replace("-", "."))
    }
}
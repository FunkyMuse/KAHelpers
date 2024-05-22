import com.diffplug.gradle.spotless.SpotlessPlugin
import com.android.build.gradle.LibraryPlugin

plugins {
    id(libs.versions.gradlePlugins.maven.publish.get())
    alias(libs.plugins.android).apply(false)
    alias(libs.plugins.library).apply(false)
    alias(libs.plugins.kotlinAndroid).apply(false)
    alias(libs.plugins.kotlinJvm).apply(false)
    alias(libs.plugins.ksp).apply(false)
    alias(libs.plugins.test).apply(false)
    alias(libs.plugins.spotless).apply(false)
    alias(libs.plugins.compose.compiler).apply(false)
    alias(libs.plugins.dokka)
}

val Project.composeMetricsDir get() = layout.buildDirectory.asFile.get().absolutePath + "/compose_metrics"
val Project.composeReportsDir get() = layout.buildDirectory.asFile.get().absolutePath + "/compose_reports"

tasks.register("cleanComposeMetrics") {
    group = "Cleaning"
    description = "Clean compose metrics folders"
    subprojects.forEach {
        if (
            it.pluginManager.hasPlugin(libs.plugins.convention.compose.library.get().pluginId)
            ||
            it.pluginManager.hasPlugin(libs.plugins.convention.compose.app.get().pluginId)
        ) {
            val dir = File(it.composeMetricsDir)
            if (dir.exists()) {
                dir.also { file ->
                    println("Deleting compose metrics >>> ${file.path} <<<")
                }.deleteRecursively()
            }
        }
    }
}
tasks.register("cleanComposeReports") {
    group = "Cleaning"
    description = "Clean compose reports folders"
    subprojects.forEach {
        if (
            it.pluginManager.hasPlugin(libs.plugins.convention.compose.library.get().pluginId)
            ||
            it.pluginManager.hasPlugin(libs.plugins.convention.compose.app.get().pluginId)
        ) {
            val dir = File(it.composeReportsDir)
            if (dir.exists()) {
                dir.also { file ->
                    println("Deleting compose reports >>> ${file.path} <<<")
                }.deleteRecursively()
            }
        }
    }
}

allprojects {
    plugins.matching { anyPlugin -> anyPlugin is SpotlessPlugin }
        .whenPluginAdded {
            extensions.configure<com.diffplug.gradle.spotless.SpotlessExtension> {
                kotlin {
                    target("**/*.kt")
                    targetExclude("${layout.buildDirectory.asFile.get()}/**/*.kt")
                    ktlint()
                        .setEditorConfigPath("${project.rootDir}/spotless/.editorconfig")
                }
            }
        }
}

subprojects {
    plugins.matching { anyPlugin -> supportedPlugins(anyPlugin) }.whenPluginAdded {
        val artifactName = if (parent?.name == "KAHelpers") name else "${parent?.name ?: ""}-$name"
        apply(plugin = libs.versions.gradlePlugins.maven.publish.get())
        plugins.withType<JavaLibraryPlugin> {
            publishing.publications {
                create<MavenPublication>("kotlin") {
                    artifactId = artifactName
                    version = libs.versions.app.version.versionName.get()
                    afterEvaluate {
                        from(components["kotlin"])
                    }
                }
            }
        }
        plugins.withType<LibraryPlugin> {
            afterEvaluate {
                publishing.publications {
                    create<MavenPublication>("release") {
                        artifactId = artifactName
                        version = libs.versions.app.version.versionName.get()
                        afterEvaluate {
                            from(components["release"])
                        }
                    }
                }
            }
        }
    }
}

fun supportedPlugins(anyPlugin: Plugin<*>?) =
    anyPlugin is LibraryPlugin || anyPlugin is JavaLibraryPlugin
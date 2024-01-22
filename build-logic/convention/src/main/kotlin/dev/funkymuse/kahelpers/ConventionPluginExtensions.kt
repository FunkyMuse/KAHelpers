package dev.funkymuse.kahelpers

import org.gradle.api.Project
import org.gradle.api.artifacts.ExternalModuleDependencyBundle
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.getByType

val Project.versionCatalog: VersionCatalog get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

val VersionCatalog.kspPluginId: String get() = getPluginId("ksp")

fun VersionCatalog.getPluginId(pluginName :String): String =  findPlugin(pluginName).get().get().pluginId

fun VersionCatalog.getLibrary(alias:String): Provider<MinimalExternalModuleDependency> =
    findLibrary(alias).get()

fun VersionCatalog.getBundle(alias:String): Provider<ExternalModuleDependencyBundle> =
    findBundle(alias).get()

fun VersionCatalog.getVersion(alias:String): String =
    findVersion(alias).get().toString()

const val implementation = "implementation"
const val debugImplementation = "debugImplementation"
const val ksp = "ksp"
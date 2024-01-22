package dev.funkymuse.kahelpers

import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.jvm.toolchain.JavaLanguageVersion

val Project.javaVersion get() = JavaLanguageVersion.of(versionCatalog.getVersion("app-build-javaVersion"))

fun Project.configureJava(configure: Action<JavaPluginExtension>): Unit =
    (this as ExtensionAware).extensions.configure("java", configure)

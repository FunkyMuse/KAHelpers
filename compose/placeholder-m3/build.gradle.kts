plugins {
    id(libs.plugins.convention.library.get().pluginId)
    id(libs.plugins.convention.compose.library.get().pluginId)
}

dependencies {
    api(projects.compose.placeholder)
}
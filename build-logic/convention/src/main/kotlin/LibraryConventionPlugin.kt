import com.android.build.gradle.LibraryExtension
import dev.funkymuse.kahelpers.addLibrariesConfig
import dev.funkymuse.kahelpers.commonVersioning
import dev.funkymuse.kahelpers.configureBuildFeatures
import dev.funkymuse.kahelpers.configureJavaCompatibilityCompileOptions
import dev.funkymuse.kahelpers.configureKotlinOptions
import dev.funkymuse.kahelpers.configureLibraryAndTestNameSpace
import dev.funkymuse.kahelpers.getPluginId
import dev.funkymuse.kahelpers.getVersion
import dev.funkymuse.kahelpers.versionCatalog
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class LibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(versionCatalog.getPluginId("library"))
                apply(versionCatalog.getPluginId("kotlinAndroid"))
                apply(versionCatalog.getPluginId("spotless"))
                apply(versionCatalog.getPluginId("dokka"))
            }
            configureKotlinOptions()
            configureLibraryAndTestNameSpace()
            extensions.configure<LibraryExtension> {
                commonVersioning(this)
                configureBuildFeatures()
                addLibrariesConfig()
                configureJavaCompatibilityCompileOptions(this)
            }
        }
    }
}
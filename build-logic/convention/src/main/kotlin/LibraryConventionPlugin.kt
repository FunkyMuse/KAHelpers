import com.android.build.gradle.LibraryExtension
import com.funkymuse.kahelpers.addLibrariesConfig
import com.funkymuse.kahelpers.commonVersioning
import com.funkymuse.kahelpers.configureBuildFeatures
import com.funkymuse.kahelpers.configureJavaCompatibilityCompileOptions
import com.funkymuse.kahelpers.configureKotlinOptions
import com.funkymuse.kahelpers.configureLibraryAndTestNameSpace
import com.funkymuse.kahelpers.getPluginId
import com.funkymuse.kahelpers.getVersion
import com.funkymuse.kahelpers.versionCatalog
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
                apply(versionCatalog.getVersion("gradlePlugins-maven-publish"))
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
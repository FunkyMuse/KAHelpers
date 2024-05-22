import com.android.build.api.dsl.ApplicationExtension
import dev.funkymuse.kahelpers.commonVersioning
import dev.funkymuse.kahelpers.configureAppPluginPackageAndNameSpace
import dev.funkymuse.kahelpers.configureBuildFeatures
import dev.funkymuse.kahelpers.configureJavaCompatibilityCompileOptions
import dev.funkymuse.kahelpers.configureKotlinOptions
import dev.funkymuse.kahelpers.getPluginId
import dev.funkymuse.kahelpers.versionCatalog
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AppConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(versionCatalog.getPluginId("android"))
                apply(versionCatalog.getPluginId("kotlinAndroid"))
                apply(versionCatalog.getPluginId("ksp"))
                apply(versionCatalog.getPluginId("convention-compose-app"))
                apply(versionCatalog.getPluginId("spotless"))
                apply(versionCatalog.getPluginId("compose-compiler"))
            }
            configureKotlinOptions()
            extensions.configure<ApplicationExtension>{
                commonVersioning(this)
                configureAppPluginPackageAndNameSpace(this)
                configureBuildFeatures()
                configureJavaCompatibilityCompileOptions(this)
            }
        }
    }
}
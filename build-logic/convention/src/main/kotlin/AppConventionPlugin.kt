import com.android.build.api.dsl.ApplicationExtension
import com.funkymuse.kahelpers.commonVersioning
import com.funkymuse.kahelpers.configureAppPluginPackageAndNameSpace
import com.funkymuse.kahelpers.configureBuildFeatures
import com.funkymuse.kahelpers.configureJavaCompatibilityCompileOptions
import com.funkymuse.kahelpers.configureKotlinOptions
import com.funkymuse.kahelpers.getPluginId
import com.funkymuse.kahelpers.versionCatalog
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
import com.funkymuse.kahelpers.configureJava
import com.funkymuse.kahelpers.getPluginId
import com.funkymuse.kahelpers.getVersion
import com.funkymuse.kahelpers.versionCatalog
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.jvm.toolchain.JavaLanguageVersion

class KotlinLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(versionCatalog.getPluginId("java-library"))
                apply(versionCatalog.getPluginId("kotlinJvm"))
                apply(versionCatalog.getPluginId("dokka"))
                apply(versionCatalog.getVersion("gradlePlugins-maven-publish"))
            }
            configureJava {
                toolchain {
                    languageVersion.set(JavaLanguageVersion.of(versionCatalog.getVersion("app-build-kotlinJVMTarget")))
                }
            }
        }
    }
}
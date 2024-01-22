import dev.funkymuse.kahelpers.configureJava
import dev.funkymuse.kahelpers.getPluginId
import dev.funkymuse.kahelpers.getVersion
import dev.funkymuse.kahelpers.versionCatalog
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
            }
            configureJava {
                toolchain {
                    languageVersion.set(JavaLanguageVersion.of(versionCatalog.getVersion("app-build-kotlinJVMTarget")))
                }
            }
        }
    }
}
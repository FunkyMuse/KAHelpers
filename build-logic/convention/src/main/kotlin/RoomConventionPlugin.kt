import com.funkymuse.kahelpers.getBundle
import com.funkymuse.kahelpers.getLibrary
import com.funkymuse.kahelpers.implementation
import com.funkymuse.kahelpers.ksp
import com.funkymuse.kahelpers.kspPluginId
import com.funkymuse.kahelpers.versionCatalog
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class RoomConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val libs = versionCatalog
            pluginManager.apply(versionCatalog.kspPluginId)
            dependencies {
                add(implementation, libs.getBundle("room"))
                add(ksp, libs.getLibrary("androidx-room-compiler"))
            }
        }
    }
}
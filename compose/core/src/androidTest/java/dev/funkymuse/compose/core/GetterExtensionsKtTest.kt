package dev.funkymuse.compose.core

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.runtime.InternalComposeApi
import androidx.compose.runtime.saveable.LocalSaveableStateRegistry
import androidx.compose.runtime.tooling.LocalInspectionTables
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalAutofill
import androidx.compose.ui.platform.LocalAutofillTree
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.platform.LocalTextToolbar
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Assert.assertSame
import org.junit.Rule
import org.junit.Test



class GetterExtensionsKtTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun contextInstanceVerification() {
        rule.setContent {
            sameInstanceAs(context, LocalContext.current)
        }
    }

    @Test
    fun lifecycleOwnerInstanceVerification() {
        rule.setContent {
            sameInstanceAs(lifecycleOwner, LocalLifecycleOwner.current)
        }
    }

    @Test
    fun configurationInstanceVerification() {
        rule.setContent {
            sameInstanceAs(configuration, LocalConfiguration.current)
        }
    }

    @Test
    fun localViewInstanceVerification() {
        rule.setContent {
            sameInstanceAs(view, LocalView.current)
        }
    }


    @ExperimentalComposeUiApi
    @Test
    fun autoFillInstanceVerification() {
        rule.setContent {
            sameInstanceAs(autoFill, LocalAutofill.current)
        }
    }

    @ExperimentalComposeUiApi
    @Test
    fun autoFillTreeInstanceVerification() {
        rule.setContent {
            sameInstanceAs(autoFillTree, LocalAutofillTree.current)
        }
    }

    @Test
    fun clipboardManagerInstanceVerification() {
        rule.setContent {
            sameInstanceAs(clipboardManager, LocalClipboardManager.current)
        }
    }

    @Test
    fun densityInstanceVerification() {
        rule.setContent {
            sameInstanceAs(density, LocalDensity.current)
        }
    }

    @Test
    fun focusManagerInstanceVerification() {
        rule.setContent {
            sameInstanceAs(focusManager, LocalFocusManager.current)
        }
    }

    @Test
    fun fontLoaderInstanceVerification() {
        rule.setContent {
            sameInstanceAs(fontLoader, LocalFontFamilyResolver.current)
        }
    }

    @Test
    fun hapticFeedbackInstanceVerification() {
        rule.setContent {
            sameInstanceAs(hapticFeedback, LocalHapticFeedback.current)
        }
    }

    @Test
    fun inspectionModeInstanceVerification() {
        rule.setContent {
            sameInstanceAs(inspectionMode, LocalInspectionMode.current)
        }
    }

    @Test
    fun layoutDirectionInstanceVerification() {
        rule.setContent {
            sameInstanceAs(layoutDirection, LocalLayoutDirection.current)
        }
    }

    @Test
    fun savedStateRegistryOwnerInstanceVerification() {
        rule.setContent {
            sameInstanceAs(savedStateRegistryOwner, LocalSavedStateRegistryOwner.current)
        }
    }

    @Test
    fun textInputServiceInstanceVerification() {
        rule.setContent {
            sameInstanceAs(textInputService, LocalTextInputService.current)
        }
    }

    @Test
    fun uriHandlerInstanceVerification() {
        rule.setContent {
            sameInstanceAs(uriHandler, LocalUriHandler.current)
        }
    }

    @Test
    fun textToolbarInstanceVerification() {
        rule.setContent {
            sameInstanceAs(textToolbar, LocalTextToolbar.current)
        }
    }

    @Test
    fun viewConfigurationInstanceVerification() {
        rule.setContent {
            sameInstanceAs(viewConfiguration, LocalViewConfiguration.current)
        }
    }

    @Test
    fun windowInfoInstanceVerification() {
        rule.setContent {
            sameInstanceAs(windowInfo, LocalWindowInfo.current)
        }
    }

    @Test
    fun textSelectionColorsInstanceVerification() {
        rule.setContent {
            sameInstanceAs(textSelectionColors, LocalTextSelectionColors.current)
        }
    }

    @Test
    fun indicationInstanceVerification() {
        rule.setContent {
            sameInstanceAs(indication, LocalIndication.current)
        }
    }

    @InternalComposeApi
    @Test
    fun inspectionTablesInstanceVerification() {
        rule.setContent {
            sameInstanceAs(inspectionTables, LocalInspectionTables.current)
        }
    }

    @Test
    fun saveableStateRegistryInstanceVerification() {
        rule.setContent {
            sameInstanceAs(saveableStateRegistry, LocalSaveableStateRegistry.current)
        }
    }

    /*@OptIn(ExperimentalComposeUiApi::class)
    @Test
    fun keyboardControllerInstanceVerification() {
        rule.setContent {
            sameInstanceAs(keyboardController, LocalSoftwareKeyboardController.current)
        }
    }*/

    private fun <T> sameInstanceAs(typeVariable: T, variable: T) {
        assertSame(typeVariable, variable)
    }

}
package dev.funkymuse.compose.core

import android.content.Context
import android.content.res.Configuration
import android.view.View
import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.InternalComposeApi
import androidx.compose.runtime.saveable.LocalSaveableStateRegistry
import androidx.compose.runtime.saveable.SaveableStateRegistry
import androidx.compose.runtime.tooling.CompositionData
import androidx.compose.runtime.tooling.LocalInspectionTables
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.autofill.Autofill
import androidx.compose.ui.autofill.AutofillTree
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.platform.ClipboardManager
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
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalTextToolbar
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.platform.TextToolbar
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.platform.ViewConfiguration
import androidx.compose.ui.platform.WindowInfo
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.LifecycleOwner
import androidx.savedstate.SavedStateRegistryOwner

/**
 * Created by FunkyMuse, date 3/31/21
 */

val lifecycleOwner: LifecycleOwner
    @Composable get() {
        return androidx.lifecycle.compose.LocalLifecycleOwner.current
    }

val context: Context
    @Composable get() {
        return LocalContext.current
    }

val configuration: Configuration
    @Composable get() {
        return LocalConfiguration.current
    }

val view: View
    @Composable get() {
        return LocalView.current
    }

@ExperimentalComposeUiApi
val autoFill: Autofill?
    @Composable get() {
        return LocalAutofill.current
    }


@ExperimentalComposeUiApi
val autoFillTree: AutofillTree
    @Composable get() {
        return LocalAutofillTree.current
    }

val clipboardManager: ClipboardManager
    @Composable get() {
        return LocalClipboardManager.current
    }

val density: Density
    @Composable get() {
        return LocalDensity.current
    }

val focusManager: FocusManager
    @Composable get() {
        return LocalFocusManager.current
    }


val fontLoader: FontFamily.Resolver
    @Composable get() {
        return LocalFontFamilyResolver.current
    }

val hapticFeedback: HapticFeedback
    @Composable get() {
        return LocalHapticFeedback.current
    }

val inspectionMode: Boolean
    @Composable get() {
        return LocalInspectionMode.current
    }

val layoutDirection: LayoutDirection
    @Composable get() {
        return LocalLayoutDirection.current
    }

val savedStateRegistryOwner: SavedStateRegistryOwner
    @Composable get() {
        return LocalSavedStateRegistryOwner.current
    }

val uriHandler: UriHandler
    @Composable get() {
        return LocalUriHandler.current
    }

val textToolbar: TextToolbar
    @Composable get() {
        return LocalTextToolbar.current
    }

val viewConfiguration: ViewConfiguration
    @Composable get() {
        return LocalViewConfiguration.current
    }

val windowInfo: WindowInfo
    @Composable get() {
        return LocalWindowInfo.current
    }


val textSelectionColors: TextSelectionColors
    @Composable get() {
        return LocalTextSelectionColors.current
    }

val indication: Indication
    @Composable get() {
        return LocalIndication.current
    }

@InternalComposeApi
val inspectionTables: MutableSet<CompositionData>?
    @Composable get() {
        return LocalInspectionTables.current
    }

val saveableStateRegistry: SaveableStateRegistry?
    @Composable get() {
        return LocalSaveableStateRegistry.current
    }

val keyboardController: SoftwareKeyboardController?
    @Composable get() {
        return LocalSoftwareKeyboardController.current
    }


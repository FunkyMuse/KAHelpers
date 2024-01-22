package dev.funkymuse.compose.core.m3

import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
suspend fun BottomSheetScaffoldState.toggleSheetState() {
    if (bottomSheetState.isVisible) bottomSheetState.hide() else bottomSheetState.expand()
}

suspend fun DrawerState.toggle() {
    if (isClosed) open() else close()
}

@OptIn(ExperimentalMaterial3Api::class)
fun BottomSheetScaffoldState.dismissSnack() {
    snackbarHostState.currentSnackbarData?.dismiss()
}

@OptIn(ExperimentalMaterial3Api::class)
fun BottomSheetScaffoldState.performSnackAction() {
    snackbarHostState.currentSnackbarData?.performAction()
}


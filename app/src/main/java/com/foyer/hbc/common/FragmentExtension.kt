package com.foyer.hbc.common

import androidx.fragment.app.Fragment
import androidx.navigation.NavBackStackEntry
import androidx.navigation.fragment.findNavController
import com.foyer.hbc.R

// To use with Fragment
fun <T> Fragment.getNavigationResult(key: String = "result") =
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)

fun <T> Fragment.setNavigationResult(result: T, key: String = "result") {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
    findNavController().popBackStack()
}

// To use with DialogFragment
fun Fragment.containsResult(key: String = "result"): Boolean {
    return getBackStackEntry()
        .savedStateHandle.contains(key)
}

fun <T> Fragment.removeResult(key: String = "result") {
    getBackStackEntry()
        .savedStateHandle.remove<T>(key)
}

fun <T> Fragment.getNavigationDialogResult(key: String = "result"): T? {
    return getBackStackEntry()
        .savedStateHandle.get<T>(key)
}

fun Fragment.getBackStackEntry(): NavBackStackEntry {
    val navController = findNavController()
    return navController.getBackStackEntry(
        navController.currentDestination?.id ?: R.id.dashboardFragment
    )
}
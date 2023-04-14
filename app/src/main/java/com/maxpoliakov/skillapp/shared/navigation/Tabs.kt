package com.maxpoliakov.skillapp.shared.navigation

import androidx.navigation.NavDestination

fun switchedTabs(previousDestination: NavDestination?, currentDestination: NavDestination): Boolean {
    if (previousDestination == null) return false
    return currentDestination.id !in previousDestination.getDestinationsInTab()
}

fun NavDestination.getDestinationsInTab(): Set<Int> {
    return destinationIdsGroupedByTab.find { it.contains(this.id) } ?: setOf()
}

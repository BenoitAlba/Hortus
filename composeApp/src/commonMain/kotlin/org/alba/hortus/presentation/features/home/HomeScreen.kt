package org.alba.hortus.presentation.features.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hortus.composeapp.generated.resources.Res
import hortus.composeapp.generated.resources.baseline_location_pin_24
import hortus.composeapp.generated.resources.baseline_menu_24
import hortus.composeapp.generated.resources.hortus2
import kotlinx.coroutines.launch
import org.alba.hortus.presentation.features.location.LocationScreen
import org.alba.hortus.presentation.features.new.AddPlantScreen
import org.alba.hortus.presentation.utils.safeNavigate
import org.jetbrains.compose.resources.painterResource

class HomeScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinScreenModel<HomeScreenViewModel>()
        viewModel.initScreen() // instead of using the init of the VM because of Voyager
        val snackBarHostState = remember { SnackbarHostState() }
        val coroutineScope = rememberCoroutineScope()

        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    Image(
                        modifier = Modifier.height(280.dp),
                        painter = painterResource(Res.drawable.hortus2),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                    )

                    NavigationDrawerItem(
                        modifier = Modifier.padding(top = 4.dp),
                        label = {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    painterResource(Res.drawable.baseline_location_pin_24),
                                    contentDescription = null
                                )
                                Text(text = "Location")
                            }
                        },
                        selected = false,
                        onClick = {
                            scope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                            navigator.safeNavigate(coroutineScope, LocationScreen())
                        }
                    )
                }
            },
        ) {
            Scaffold(
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            navigator.safeNavigate(coroutineScope, AddPlantScreen())
                        },
                    ) {
                        Icon(Icons.Filled.Add, "Adding plant")
                    }
                },
                snackbarHost = {
                    SnackbarHost(hostState = snackBarHostState)
                },
                topBar = {
                    TopAppBar(
                        title = {
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        drawerState.apply {
                                            if (isClosed) open() else close()
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.baseline_menu_24),
                                    contentDescription = "Localized description"
                                )
                            }
                        }
                    )
                }
            ) {
                Column(
                    modifier = Modifier.padding(
                        top = it.calculateTopPadding(),
                        bottom = it.calculateBottomPadding(),
                        start = 16.dp,
                        end = 16.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    var showForecast by remember { mutableStateOf(true) }
                    AnimatedVisibility(
                        visible = showForecast,
                        enter = fadeIn() + expandVertically(
                            spring(
                                stiffness = Spring.StiffnessVeryLow,
                            )
                        ),
                        exit = fadeOut() + shrinkVertically(
                            spring(
                                stiffness = Spring.StiffnessVeryLow,
                            )
                        ),
                    ) {
                        ForecastView(viewModel)
                    }

                    PlantListView(viewModel) { noPlants ->
                        showForecast = !noPlants
                    }
                }

            }
        }
    }
}
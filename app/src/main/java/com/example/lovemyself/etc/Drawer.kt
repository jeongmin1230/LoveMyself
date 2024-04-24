package com.example.lovemyself.etc

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.lovemyself.R
import com.example.lovemyself.ui.theme.BasicBlack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDrawer(drawerNavController: NavHostController, drawerState: DrawerState, scope: CoroutineScope, content: @Composable () -> Unit) {
    val items = listOf(
        ImageVector.vectorResource(R.drawable.ic_home),
        ImageVector.vectorResource(R.drawable.ic_write),
        ImageVector.vectorResource(R.drawable.ic_all),
        ImageVector.vectorResource(R.drawable.ic_settings)
    )
    val itemNames = stringArrayResource(id = R.array.menu_item)
    val selectedItem = remember { mutableStateOf(items[0]) }
    var gestureEnabled by remember { mutableStateOf(true) }
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = gestureEnabled,
        drawerContent = {
            Column(modifier = Modifier
                .background(Color.White)
                .fillMaxWidth(0.7f)) {
                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_close),
                    contentDescription = stringResource(id = R.string.close_description),
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(all = 8.dp)
                )
                Text(
                    text = stringResource(id = R.string.hello).format(User.name),
                    style = MaterialTheme.typography.bodyLarge.copy(BasicBlack),
                    modifier = Modifier.padding(all = 8.dp))
                ModalDrawerSheet(
                    drawerShape = RectangleShape,
                    drawerContainerColor = Color.White
                ) {
                    items.forEachIndexed { index, item ->
                        NavigationDrawerItem(
                            icon = { Image(imageVector = item, contentDescription = itemNames[index]) },
                            label = { Text(
                                text = itemNames[index],
                                style = MaterialTheme.typography.bodyMedium.copy(BasicBlack)) },
                            selected = item == selectedItem.value,
                            onClick = {
                                scope.launch { drawerState.close() }
                                selectedItem.value = item
                                drawerNavController.navigate(itemNames[index])
                            },
                            shape = RectangleShape,
                            colors = NavigationDrawerItemDefaults.colors(
                                unselectedContainerColor = Color.White,
                                selectedContainerColor = Color.White
                            )
                        )
                    }
                }
            }
        },
        content = { content() }
    )
}

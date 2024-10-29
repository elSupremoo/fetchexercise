/*
*
* Name - Supreme Dallakoti
*
* This file contains the View for the fetch coding exercise.
* It is a simple lazy loading column that displays the items
* processed by the ViewModel. Items are first grouped by their
* listID and then by their name. Items with a blank name or null
* name are omitted.
*
* */


package com.thesupreme.fetchexercise

import ItemViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.thesupreme.fetchexercise.ui.theme.FetchExerciseTheme


// Boilerplate
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FetchExerciseTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // ItemsView as the child of Surface to display our items
                    ItemsView()
                }
            }
        }
    }
}


// gets data exposed by the ViewModel and lays it out on a lazy loading column
@Composable
fun ItemsView() {
    val itemViewModel: ItemViewModel =  viewModel()
    val items by itemViewModel.items.collectAsState()

    LazyColumn {
        val groupedItems = items.groupBy { it.listId }
        groupedItems.forEach { (listId, items) ->
            item {
                Text(text = "Items with ListID $listId are as:",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp)
                )
            }
            items(items) { item ->

                // populating the list with ListItemViews
                ListItemView(item)
            }
        }
    }
}

// Displaying the name of the item
@Composable
fun ListItemView(item: Item) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(all = 12.dp)) {
        Text(text = item.name ?: "",
            fontSize = 24.sp,
            modifier = Modifier.fillMaxWidth()
        )
    }

}

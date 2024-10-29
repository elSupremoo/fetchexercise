/*
*
* Name - Supreme Dallakoti
*
* This file contains the ViewModel for the fetch coding exercise.
* I am using MVVM for this but since we don't have a model
* to work with, its just the View and the ViewModel.
*
* I have 4 methods to this class. One for fetching the data off the
* url, one for setting up a connection to the url, one for parsing
* the data into a suitable data class, and one for some help with
* sorting the names

* */

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thesupreme.fetchexercise.Item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import org.json.JSONArray

class ItemViewModel : ViewModel() {

    // to store the items
    private val _items = MutableStateFlow<List<Item>>(emptyList())
    val items: StateFlow<List<Item>> = _items

    // to fetch items when the object is instantiated
    init {
        fetchItems("https://fetch-hiring.s3.amazonaws.com/hiring.json")
    }


    // to get the items off the json
    private fun fetchItems(urlString: String) {
        viewModelScope.launch {
            try {
                val itemsList = withContext(Dispatchers.IO) {

                    // calling the fetchJson method defined below
                    val json = fetchJson(urlString)
                    parseJson(json)
                }
                // updating the value with the fetched data and sorting as asked by the problem
                _items.value = itemsList.sortedWith(compareBy<Item> { it.listId }.thenBy { extractNumeric(it.name)})
            } catch (e: Exception) {
                e.printStackTrace()

                // setting to empty in case it doesnt work
                _items.value = emptyList()
            }
        }
    }


    // to connect to the url and fetch the data
    private fun fetchJson(urlString: String): String? {
        val connection = URL(urlString).openConnection() as HttpURLConnection
        return try {
            connection.requestMethod = "GET"
            connection.connect()
            if (connection.responseCode == 200) {
                connection.inputStream.bufferedReader().use { it.readText() }
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()

            // returning null on error
            null
        } finally {
            connection.disconnect()
        }
    }


    // extracting individual parts of the data as per requirements
    private fun parseJson(json: String?): List<Item> {
        val itemsList = mutableListOf<Item>()
        json?.let {
            val jsonArray = JSONArray(it)
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val id = jsonObject.optInt("id")
                val listId = jsonObject.optInt("listId")
                val name: String? = jsonObject.optString("name", null)

                // took  me a while to figure out, null is actually saved as the string null so checking for that
                if (!(name == "" || name == "null")) {

                    // adding to the list if not null or empty
                    itemsList.add(Item(id, listId, name))
                }
            }
        }
        return itemsList
    }


    /*
    *
    * After a lot of consideration, I did decide to write this method. The requirements
    * page states to sort by listId and then by name. That could mean either treat the name
    * as a string and sort it in the default way, or it could mean to sort by the numerical
    * part of the name. The page also states to use my best judgement to determine expected
    * result. I assume a reasonable person would think sorting by the names should result in
    * the sorting of the numerical section. So, I did exactly that.
    *
    * I could do the sorting with their ids instead of parsing the id number out of the name
    * but since the problem explicitly says to do by their names, I chose to do it this way
    *
    * */
    private fun extractNumeric(name: String?) : Int {
        return name?.filter { it.isDigit() }?.toIntOrNull() ?: 0
    }
}

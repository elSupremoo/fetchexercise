/*
*
* A data class representing an Item from the json.
* Has an id, listId, and a name part which may be null
*
* */


package com.thesupreme.fetchexercise

data class Item(
    val id: Int,
    val listId: Int,
    val name: String?
)

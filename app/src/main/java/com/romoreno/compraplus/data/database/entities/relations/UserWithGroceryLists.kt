package com.romoreno.compraplus.data.database.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.romoreno.compraplus.data.database.entities.GroceryList
import com.romoreno.compraplus.data.database.entities.User

data class UserWithGroceryLists(
    @Embedded val user: User,
    @Relation(
        parentColumn = "id",
        entityColumn = "user_id"
    )
    val groceryLists: List<GroceryList>
)
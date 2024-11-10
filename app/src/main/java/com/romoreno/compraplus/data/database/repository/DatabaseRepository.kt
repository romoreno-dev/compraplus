package com.romoreno.compraplus.data.database.repository

import com.google.firebase.auth.FirebaseUser
import com.romoreno.compraplus.domain.model.GroceryListModel
import kotlinx.coroutines.flow.Flow

interface DatabaseRepository {

    fun getGroceryListsFromUser(firebaseUser: FirebaseUser?): Flow<List<GroceryListModel>>
}
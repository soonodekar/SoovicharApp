package com.soonodekar.myapp1.thoughts.thoughtsService

import android.util.Log
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.soonodekar.myapp1.thoughts.modules.Thought
import com.soonodekar.myapp1.thoughts.roomdatabase.BaseService

object ThoughtsService : BaseService(){

    suspend fun getSpecificThoughts(category: String?, language : String?, callback: (List<Thought>) -> Unit){
        val fireStore = Firebase.firestore
        val fireStoreCollection = fireStore.collection("UploadedThoughts")
        var query : Query? = null
        if(category == null){
            query = fireStoreCollection.whereEqualTo("language", language)
        }
        if(language == null){
            query = fireStoreCollection.whereEqualTo("category", category)
        }
        if(language != null && category != null){
            query = fireStoreCollection.whereEqualTo("category", category).whereEqualTo("language", language)
        }


        query!!
            .get()
            .addOnSuccessListener {
                val users = it.toObjects(Thought::class.java)
                Log.e("specific added", "successfully")
                callback(users)
            }
            .addOnFailureListener {

            }


    }

    suspend fun getToadySThought(callback: (List<Thought>) -> Unit){
        val fireStore = Firebase.firestore

        val thoughtsCollection = fireStore.collection("Today'sCollections").document("todays_thoughts").collection("thougths")

        thoughtsCollection.get()
            .addOnSuccessListener {
                val response = it.toObjects(Thought::class.java)
                Log.e("respo", "$response")
                callback(response)
            }
            .addOnFailureListener {

            }
    }

    suspend fun getAllThoughts(callback : (List<Thought>)-> Unit ){
        val fireStore = Firebase.firestore
        val thoughtCollection = fireStore.collection("UploadedThoughts")

        thoughtCollection
            .get()
            .addOnSuccessListener {
                val thoughts = it.toObjects(Thought::class.java)
                callback(thoughts)
                Log.e("data added", "successfully")
            }
            .addOnFailureListener {

            }
    }

}
package com.soonodekar.myapp1.jokes.jokesservice

import android.util.Log
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.soonodekar.myapp1.jokes.modules.Joke

object JokesService {

    suspend fun getSpecificJokes(language: String?, callback: (List<Joke>) -> Unit){
        val fireStore = Firebase.firestore
        val fireStoreCollection = fireStore.collection("UploadedJokes")
        var query : Query? = null


        if(language != null ){
            query = fireStoreCollection.whereEqualTo("language", language)
        }

        query!!
            .get()
            .addOnSuccessListener {
                val users = it.toObjects(Joke::class.java)
                Log.e("specific added", "successfully")
                callback(users)
            }
            .addOnFailureListener {

            }


    }

    suspend fun getToadySJoke(callback: (List<Joke>) -> Unit){
        val fireStore = Firebase.firestore

        val jokesCollection = fireStore.collection("Today'sCollections").document("todays_jokes").collection("jokes")

        jokesCollection.get()
            .addOnSuccessListener {
                val response = it.toObjects(Joke::class.java)
                Log.e("jokes_respo", "$response")
                callback(response)
            }
            .addOnFailureListener {

            }
    }

    suspend fun getAllJokes(callback : (List<Joke>)-> Unit ){
        val fireStore = Firebase.firestore
        val jokesCollection = fireStore.collection("UploadedJokes")

        jokesCollection
            .get()
            .addOnSuccessListener {
                val jokes = it.toObjects(Joke::class.java)
                callback(jokes)
                Log.e("data added", "successfully")
            }
            .addOnFailureListener {

            }
    }

}
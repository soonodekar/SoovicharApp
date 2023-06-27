package com.soonodekar.myapp1.jokes.repo

import com.soonodekar.myapp1.BaseRepo
import com.soonodekar.myapp1.jokes.modules.Joke
import com.soonodekar.myapp1.jokes.jokesservice.JokesService
import com.soonodekar.myapp1.thoughts.roomdatabase.ThoughtDatabase

class JokesRepo(private val jokesService: JokesService, private val JokeDatabase: ThoughtDatabase) : BaseRepo() {

   /* suspend fun getSavedJokes(): List<Joke> {
        return JokeDatabase.getDatabase().getJokeDao().getSavedjokes()
    }

    suspend fun addJokeToSaved(Joke: Joke) : Boolean{

        return if(!JokeDatabase.getDatabase().getJokeDao().isJokeAlreadyExist(Joke.id!!)) {
            JokeDatabase.getDatabase().getJokeDao()
                .addJokeToSaved(Joke)
            Log.e(
                "saved Joke",
                JokeDatabase.getDatabase().getJokeDao()
                    .getSavedJokeById(Joke.id!!).toString()
            )
            true

        }else{
            false
        }
    }*/

    suspend fun getSpecificJokes(
        language: String?,
        callback: (List<Joke>) -> Unit
    ) {
        jokesService.getSpecificJokes(language) {
            callback(it)
        }
    }

    suspend fun getAllJokes(callback: (List<Joke>) -> Unit) {
        jokesService.getAllJokes {
            callback(it)
        }
    }

    suspend fun getTodaySJokes(callback: (List<Joke>) -> Unit) {
        jokesService.getToadySJoke {
            callback(it)
        }
    }
}
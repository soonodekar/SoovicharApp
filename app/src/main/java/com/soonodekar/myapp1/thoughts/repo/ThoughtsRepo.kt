package com.soonodekar.myapp1.thoughts.repo

import android.util.Log
import com.soonodekar.myapp1.BaseRepo
import com.soonodekar.myapp1.thoughts.modules.Thought
import com.soonodekar.myapp1.thoughts.roomdatabase.BaseService
import com.soonodekar.myapp1.thoughts.roomdatabase.ThoughtDatabase
import com.soonodekar.myapp1.thoughts.thoughtsService.ThoughtsService

class ThoughtsRepo(private val thoughtsService: BaseService, private val thoughtDatabase: ThoughtDatabase) : BaseRepo() {

    suspend fun getSavedThoughts(): List<Thought> {
        return thoughtDatabase.getDatabase().getThoughtDao().getSavedThoughts()
    }

    suspend fun addThoughtToSaved(thought: Thought) : Boolean{

        return if(!thoughtDatabase.getDatabase().getThoughtDao().isThoughtAlreadyExist(thought.id!!)) {
            thoughtDatabase.getDatabase().getThoughtDao()
                .addThoughtToSaved(thought)
            Log.e(
                "saved Thought",
                thoughtDatabase.getDatabase().getThoughtDao()
                    .getSavedThoughtById(thought.id!!).toString()
            )
            true

        }else{
            false
        }
    }

    suspend fun getSpecificThoughts(
        category: String?,
        language: String?,
        callback: (List<Thought>) -> Unit
    ) {
        (thoughtsService as ThoughtsService).getSpecificThoughts(category, language) {
            callback(it)
        }
    }

    suspend fun getAllThoughts(callback: (List<Thought>) -> Unit) {
        (thoughtsService as ThoughtsService).getAllThoughts {
            callback(it)
        }
    }

    suspend fun getTodaySThoughts(callback: (List<Thought>) -> Unit) {
        (thoughtsService as ThoughtsService).getToadySThought {
            callback(it)
        }
    }
}
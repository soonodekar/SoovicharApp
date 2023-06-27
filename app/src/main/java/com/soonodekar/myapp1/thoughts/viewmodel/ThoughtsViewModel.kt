package com.soonodekar.myapp1.thoughts.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.soonodekar.myapp1.thoughts.modules.Thought
import com.soonodekar.myapp1.thoughts.repo.ThoughtsRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ThoughtsViewModel(private val thoughtsRepo: ThoughtsRepo) : ViewModel() {
    val thoughtsLiveData = MutableLiveData<ArrayList<Thought>>()
    val todaySThoughtsLiveData = MutableLiveData<ArrayList<Thought>>()
    val specificThoughtsLiveData = MutableLiveData<ArrayList<Thought>>()
    val savedThoughtLiveData = MutableLiveData<ArrayList<Thought>>()
    val savedStatusLiveData = MutableLiveData<Boolean>()

     fun getAllThoughts(){
        CoroutineScope(Dispatchers.IO).launch{
            thoughtsRepo.getAllThoughts {
                thoughtsLiveData.postValue(it.toCollection(ArrayList<Thought>()))
            }
        }
    }

    fun getSpecificThoughts(category: String?, language: String?){
        CoroutineScope(Dispatchers.IO).launch {
            thoughtsRepo.getSpecificThoughts(category, language){
                specificThoughtsLiveData.postValue(it.toCollection(ArrayList<Thought>()))
            }
        }
    }


    fun addThoughtToSaved(thought: Thought){
        CoroutineScope(Dispatchers.IO).launch{
            savedStatusLiveData.postValue(thoughtsRepo.addThoughtToSaved(thought))
        }
    }

    fun getSavedThoughts(){
        CoroutineScope(Dispatchers.IO).launch{
                val response = thoughtsRepo.getSavedThoughts()

            withContext(Dispatchers.Main){
                savedThoughtLiveData.postValue(response.toCollection(ArrayList<Thought>()))
            }
        }
    }

    fun getTodaysThoughts(){
        CoroutineScope(Dispatchers.IO).launch{
            thoughtsRepo.getTodaySThoughts {
                todaySThoughtsLiveData.postValue(it.toCollection(ArrayList<Thought>()))
            }
        }
    }
}
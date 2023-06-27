package com.soonodekar.myapp1.jokes.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.soonodekar.myapp1.jokes.modules.Joke
import com.soonodekar.myapp1.jokes.repo.JokesRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class JokesViewModel(private val jokesRepo: JokesRepo) : ViewModel() {
    val JokesLiveData = MutableLiveData<ArrayList<Joke>>()
    val todaySJokesLiveData = MutableLiveData<ArrayList<Joke>>()
    val specificJokesLiveData = MutableLiveData<ArrayList<Joke>>()
    val savedJokeLiveData = MutableLiveData<ArrayList<Joke>>()
    val savedStatusLiveData = MutableLiveData<Boolean>()

     fun getAllJokes(){
        CoroutineScope(Dispatchers.IO).launch{
            jokesRepo.getAllJokes {
                JokesLiveData.postValue(it.toCollection(ArrayList<Joke>()))
            }
        }
    }

    fun getSpecificJokes(language: String?){
        CoroutineScope(Dispatchers.IO).launch {
            jokesRepo.getSpecificJokes(language){
                specificJokesLiveData.postValue(it.toCollection(ArrayList<Joke>()))
            }
        }
    }


    /*fun addJokeToSaved(Joke: Joke){
        CoroutineScope(Dispatchers.IO).launch{
            savedStatusLiveData.postValue(jokesRepo.addJokeToSaved(Joke))
        }
    }

    fun getSavedJokes(){
        CoroutineScope(Dispatchers.IO).launch{
                val response = jokesRepo.getSavedJokes()

            withContext(Dispatchers.Main){
                savedJokeLiveData.postValue(response.toCollection(ArrayList<Joke>()))
            }
        }
    }*/

    fun getTodaysJokes(){
        CoroutineScope(Dispatchers.IO).launch{
            jokesRepo.getTodaySJokes {
                todaySJokesLiveData.postValue(it.toCollection(ArrayList<Joke>()))
            }
        }
    }
}
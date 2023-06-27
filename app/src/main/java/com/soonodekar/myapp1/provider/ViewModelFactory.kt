package com.soonodekar.myapp1.provider

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.soonodekar.myapp1.BaseRepo
import com.soonodekar.myapp1.jokes.repo.JokesRepo
import com.soonodekar.myapp1.jokes.viewmodel.JokesViewModel
import com.soonodekar.myapp1.thoughts.repo.ThoughtsRepo
import com.soonodekar.myapp1.thoughts.viewmodel.ThoughtsViewModel

class ViewModelFactory(private val repo : BaseRepo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if(modelClass.isAssignableFrom(ThoughtsViewModel :: class.java) && repo is ThoughtsRepo){
            return ThoughtsViewModel(repo) as T
        }

        if(modelClass.isAssignableFrom(JokesViewModel :: class.java) && repo is JokesRepo){
            return JokesViewModel(repo) as T
        }

        throw java.lang.Exception()
    }
}
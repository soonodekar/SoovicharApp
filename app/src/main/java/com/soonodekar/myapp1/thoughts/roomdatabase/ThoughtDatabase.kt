package com.soonodekar.myapp1.thoughts.roomdatabase

import android.content.Context
import androidx.room.Room

class ThoughtDatabase(val context: Context) : BaseService(){

    fun getDatabase(): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "saved_Thoughts_db"
        ).allowMainThreadQueries()
            .build()

    }

}
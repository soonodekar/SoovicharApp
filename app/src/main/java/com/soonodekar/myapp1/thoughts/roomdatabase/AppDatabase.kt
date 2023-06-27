package com.soonodekar.myapp1.thoughts.roomdatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.soonodekar.myapp1.thoughts.modules.Thought

@Database(entities = [Thought::class], version = 1)
abstract class AppDatabase: RoomDatabase(){
    abstract fun getThoughtDao() : ThoughtDao

}
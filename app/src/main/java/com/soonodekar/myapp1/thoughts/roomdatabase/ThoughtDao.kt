package com.soonodekar.myapp1.thoughts.roomdatabase

import androidx.room.*
import com.soonodekar.myapp1.thoughts.modules.Thought

@Dao
interface ThoughtDao {
    @Query("select * from saved_thoughts")
    fun getSavedThoughts() : List<Thought>

    @Query("select * from saved_thoughts where id = :id limit 1")
    fun getSavedThoughtById(id : Long) : Thought?

    @Query("SELECT EXISTS(SELECT * FROM saved_thoughts WHERE id = :id)")
    fun isThoughtAlreadyExist(id: Long) : Boolean

    @Insert
    fun addThoughtToSaved(thought: Thought)

    @Query("delete from saved_thoughts where id = :id")
    fun deleteThought(id : Long)

}

package com.soonodekar.myapp1.thoughts.modules

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.soonodekar.myapp1.BaseData

@Entity(tableName = "saved_thoughts")
data class Thought(
    @PrimaryKey
    var id : Long? = null,
    var text : String? = null,
    var author : String? = null,
    var category : String? = null,
    var imageUri : String? = null,
    var language : String? = null
) : BaseData(){

    companion object{

        const val MARATHI = "Marathi"
        const val HINDI = "Hindi"
        const val ENGLISH = "English"


        const val MOTIVATION = "Motivation"
        const val MORNING = "Morning"
        const val GOOD_THOUGHT = "Good Thought"
    }

}

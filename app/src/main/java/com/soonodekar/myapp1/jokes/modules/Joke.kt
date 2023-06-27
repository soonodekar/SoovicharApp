package com.soonodekar.myapp1.jokes.modules

import com.soonodekar.myapp1.BaseData

//@Entity(tableName = "saved_jokes")
data class Joke(
    //@PrimaryKey
    var id : Long? = null,
    var text : String? = null,
    var imageUri : String? = null,
    var language : String? = null
) : BaseData() {

    companion object{

        const val MARATHI = "Marathi"
        const val HINDI = "Hindi"
        const val ENGLISH = "English"
    }

}

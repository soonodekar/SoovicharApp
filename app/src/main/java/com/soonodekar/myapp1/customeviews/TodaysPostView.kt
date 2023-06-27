package com.soonodekar.myapp1.customeviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.soonodekar.myapp1.R
import com.soonodekar.myapp1.databinding.TodaysPostViewBinding
import com.soonodekar.myapp1.BaseData
import com.soonodekar.myapp1.MainActivity
import com.soonodekar.myapp1.jokes.modules.Joke
import com.soonodekar.myapp1.thoughts.modules.Thought

class TodaysPostView(context: Context, attributeSet: AttributeSet?) :
    LinearLayout(context, attributeSet) {

    var binding: TodaysPostViewBinding
    private val storage = Firebase.storage

    var baseData: BaseData? = null
        set(value) {
            field = value
            bindData()
        }

    interface OnTodaySPPostClickListeners{
        fun onShareBtnClick()
    }

    var onPostClickListeners : OnTodaySPPostClickListeners? = null


    constructor(context: Context) : this(context, null)

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.todays_post_view, null)
        binding = TodaysPostViewBinding.bind(view)
        this.layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT
        )
        setUpListeners()
        this.addView(view)
    }


    private fun setUpListeners() {
        binding.imgSaveBtn.setOnClickListener {
            popUpDialog()
        }

        binding.imgShareBtn.setOnClickListener {
            if(onPostClickListeners == null)return@setOnClickListener
            onPostClickListeners!!.onShareBtnClick()
        }
    }


    private fun popUpDialog() {

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Save ?")

        val baseDataText: String = when (baseData) {
            is Thought -> "Thought"
            is Joke -> "Jokes"
            else -> {
                "Data"
            }
        }

        builder.setMessage("Save the $baseDataText")

        builder.setPositiveButton("Save") { dialog, which ->
            if (baseData is Thought) {
                val thoughtCopy = baseData as Thought
                thoughtCopy.imageUri = null
                MainActivity.thoughtsViewModel.addThoughtToSaved(thoughtCopy)
                dialog.dismiss()
            }
        }

        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()

    }

    private fun bindData() {
        Firebase
        if (baseData == null) return

        when (baseData) {
            is Thought -> {
                val thought = baseData as Thought
                binding.txtTodaysPostText.text = "${thought.text}\n\n -${thought.author}"

                if (thought.imageUri != null) {
                    loadImageIntoImageView(thought.imageUri!!, binding.imgText)
                } else {
                    when(thought.id!! % 3){
                        0L ->binding.imgText.setImageResource(R.drawable.thoughts_background1)
                        1L ->binding.imgText.setImageResource(R.drawable.istockphoto)
                        2L ->binding.imgText.setImageResource(R.drawable.istockphoto)
                    }
                }

            }

            is Joke -> {

                val joke = baseData as Joke
                binding.txtTodaysPostText.text = "${joke.text}"
                binding.txtTodaysPostText.setTextColor(resources.getColor(R.color.black))

                if (joke.imageUri != null) {
                    loadImageIntoImageView(joke.imageUri!!, binding.imgText)
                } else {
                     when(joke.id!! % 3){
                         0L ->binding.imgText.setImageResource(R.drawable.jokes_background1)
                         1L ->binding.imgText.setImageResource(R.drawable.jokes_background2)
                         2L ->binding.imgText.setImageResource(R.drawable.jokes_background3)
                     }

                }
            }
        }


    }

    private fun loadImageIntoImageView(imageUrl: String?, imageView: ImageView) {
        Glide.with(imageView.context).load(imageUrl).placeholder(R.drawable.istockphoto)
            .into(imageView)
    }
}
package com.soonodekar.myapp1.jokes.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.soonodekar.myapp1.BaseData
import com.soonodekar.myapp1.R
import com.soonodekar.myapp1.adapotor.LargePostAdaptor
import com.soonodekar.myapp1.customeviews.LargePostView
import com.soonodekar.myapp1.databinding.PostsListFragementBinding
import com.soonodekar.myapp1.jokes.repo.JokesRepo
import com.soonodekar.myapp1.jokes.jokesservice.JokesService
import com.soonodekar.myapp1.jokes.modules.Joke
import com.soonodekar.myapp1.jokes.viewmodel.JokesViewModel
import com.soonodekar.myapp1.provider.ViewModelFactory
import com.soonodekar.myapp1.thoughts.modules.Thought
import com.soonodekar.myapp1.thoughts.roomdatabase.ThoughtDatabase

class JokesFragment : Fragment(){

    private lateinit var jokeLargePostAdaptor: LargePostAdaptor
    private lateinit var binding: PostsListFragementBinding
    private lateinit var jokesViewModel: JokesViewModel

    //private var category : String? = null
    private var language : String? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.posts_list_fragement, null)
        binding = PostsListFragementBinding.bind(view)
        bindData()
        addSpinners()
        initViewModel()
        initObserver()
        initAdaptor()
        jokesViewModel.getAllJokes()
        setUpListeners()
        return view
    }

    private fun bindData(){
        binding.txtDataTitle.text = resources.getString(R.string.jokesText)
    }

    private fun addSpinners(){
        /*val catItems = arrayOf("Category", Thought.MOTIVATION, Thought.GOOD_THOUGHT, Thought.MORNING) // Replace with your actual items

        val catAdapter: ArrayAdapter<String> = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            catItems
        )

        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)*/
        binding.categoryFilterSpinner.visibility = View.GONE


        val langItems = arrayOf("Language", Thought.MARATHI, Thought.ENGLISH, Thought.HINDI) // Replace with your actual items

        val langAdapter: ArrayAdapter<String> = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            langItems
        )

        langAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.langFilterSpinner.adapter = langAdapter


    }

    private fun setUpListeners(){
        onSpinnerItemClickListener()
        binding.txtAplFilter.setOnClickListener {
            if ( language != null) {
                jokesViewModel.getSpecificJokes(language)
            }
        }
    }

    private fun onSpinnerItemClickListener() {

        /*binding.categoryFilterSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    when (parent.getItemAtPosition(position).toString()) {
                        "Category" ->{
                            category = null
                           // binding.txtCatFilter.text = "Category"
                        }
                        Thought.MORNING -> {
                            category = Thought.MORNING
                           // binding.txtCatFilter.text = Thought.MORNING
                        }
                        Thought.MOTIVATION -> {
                            category = Thought.MOTIVATION
                            //binding.txtCatFilter.text = Thought.MOTIVATION
                        }
                        Thought.GOOD_THOUGHT -> {
                            category = Thought.GOOD_THOUGHT
                           // binding.txtCatFilter.text = Thought.GOOD_THOUGHT
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }*/

        binding.langFilterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                when(parent.getItemAtPosition(position).toString()){
                    "Language" ->{
                        language = null
                        //binding.txtLangFilter.text = "Language"
                    }
                    Thought.MARATHI -> {
                        language = Thought.MARATHI
                        //binding.txtLangFilter.text = Thought.MARATHI
                    }
                    Thought.HINDI -> {
                        language = Thought.HINDI
                       // binding.txtLangFilter.text = Thought.HINDI
                    }
                    Thought.ENGLISH -> {
                        language = Thought.ENGLISH
                        //binding.txtLangFilter.text = Thought.ENGLISH
                    }
                }

            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                 language = null
            }
        }

    }

    private fun initViewModel(){
        jokesViewModel =
            ViewModelProvider(
                this,
                ViewModelFactory(
                    JokesRepo(
                        JokesService,
                        ThoughtDatabase(requireContext())
                    )
                )
            )[JokesViewModel::class.java]
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObserver(){

        jokesViewModel.JokesLiveData.observe(viewLifecycleOwner){
            jokeLargePostAdaptor = LargePostAdaptor(it)
            binding.recyclerAllDatas.adapter = jokeLargePostAdaptor
            binding.progressbarDatas.visibility = View.GONE
            jokeLargePostAdaptor.onLargePostViewClickListeners = OnShareBtnClickListener()

        }

        jokesViewModel.specificJokesLiveData.observe(viewLifecycleOwner){
            jokeLargePostAdaptor.data = it
            jokeLargePostAdaptor.notifyDataSetChanged()
            binding.recyclerAllDatas.adapter = jokeLargePostAdaptor
            binding.progressbarDatas.visibility = View.GONE
            jokeLargePostAdaptor.onLargePostViewClickListeners = OnShareBtnClickListener()
        }


            /*MainActivity.thoughtsViewModel.savedStatusLiveData.observe(
                viewLifecycleOwner
            ) {
                if (it) {
                    MainActivity.mt("Saved", requireContext())
                }
                else{
                    MainActivity.mt("Thought is AlreadySaved", requireContext())
                }
            }*/

    }

    private inner class OnShareBtnClickListener : LargePostAdaptor.OnLargePostViewClickListeners{

        override fun onShareBtnClick(position: Int, data: BaseData, view: LargePostView) {
            shareThought(data as Joke, view)
        }

        override fun onSaveBtnClick(position: Int, data: BaseData, view: LargePostView) {
            TODO("Not yet implemented")
        }

    }

    private fun shareThought(joke: Joke, todaysPostView: LargePostView){

        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, todaysPostView.binding.txtPostText.text) // Replace "Text to share" with the actual text you want to share

        if (shareIntent.resolveActivity(activity!!.packageManager) != null) {
            startActivity(Intent.createChooser(shareIntent, "Share via"))
        } else {
            // Handle case when no apps can handle the intent
            // For example, show an error message to the user
        }

    }

    private fun initAdaptor(){
        binding.recyclerAllDatas.layoutManager =
            LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
    }

}



package com.soonodekar.myapp1.thoughts.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.soonodekar.myapp1.R
import com.soonodekar.myapp1.adapotor.LargePostAdaptor
import com.soonodekar.myapp1.databinding.PostsListFragementBinding
import com.soonodekar.myapp1.provider.ViewModelFactory
import com.soonodekar.myapp1.thoughts.modules.Thought
import com.soonodekar.myapp1.thoughts.roomdatabase.ThoughtDatabase
import com.soonodekar.myapp1.thoughts.repo.ThoughtsRepo
import com.soonodekar.myapp1.thoughts.thoughtsService.ThoughtsService
import com.soonodekar.myapp1.thoughts.viewmodel.ThoughtsViewModel


class SavedThoughtsFragment : Fragment(){

    private lateinit var postAdaptor: LargePostAdaptor
    private lateinit var binding: PostsListFragementBinding
    private lateinit var thoughtsViewModel: ThoughtsViewModel

    private var category : String? = null
    private var language : String? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.posts_list_fragement, null)
        binding = PostsListFragementBinding.bind(view)
        bindTexts()
        addSpinners()
        initViewModel()
        initObserver()
        initAdaptor()
        thoughtsViewModel.getSavedThoughts()
        setUpListeners()
        return view
    }

    private fun bindTexts(){
        binding.progressbarDatas.visibility = View.GONE
        binding.txtDataTitle.text = "Saved Thoughts"
    }


    private fun addSpinners(){
        val catItems = arrayOf("Category", Thought.MOTIVATION, Thought.GOOD_THOUGHT, Thought.MORNING) // Replace with your actual items

        val catAdapter: ArrayAdapter<String> = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            catItems
        )

        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categoryFilterSpinner.adapter = catAdapter


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

    }

    private fun onSpinnerItemClickListener() {

        binding.categoryFilterSpinner.onItemSelectedListener =
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
            }

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
                category = null
            }
        }

    }

    private fun initViewModel(){
        thoughtsViewModel =
            ViewModelProvider(
                this,
                ViewModelFactory(
                    ThoughtsRepo(
                        ThoughtsService,
                        ThoughtDatabase(requireContext())
                    )
                )
            )[ThoughtsViewModel::class.java]
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObserver(){

        thoughtsViewModel.savedThoughtLiveData.observe(viewLifecycleOwner){
            postAdaptor = LargePostAdaptor(it)
            binding.recyclerAllDatas.adapter = postAdaptor
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
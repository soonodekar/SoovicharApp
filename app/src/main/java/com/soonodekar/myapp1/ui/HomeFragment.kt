package com.soonodekar.myapp1.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.soonodekar.myapp1.BaseData
import com.soonodekar.myapp1.MainActivity
import com.soonodekar.myapp1.R
import com.soonodekar.myapp1.SecondaryActivity
import com.soonodekar.myapp1.adapotor.PostAdaptor
import com.soonodekar.myapp1.customeviews.LargePostView
import com.soonodekar.myapp1.customeviews.TodaysPostView
import com.soonodekar.myapp1.databinding.HomeFragmentBinding
import com.soonodekar.myapp1.jokes.jokesservice.JokesService
import com.soonodekar.myapp1.jokes.repo.JokesRepo
import com.soonodekar.myapp1.jokes.ui.JokesFragment
import com.soonodekar.myapp1.jokes.viewmodel.JokesViewModel
import com.soonodekar.myapp1.provider.ViewModelFactory
import com.soonodekar.myapp1.thoughts.modules.Thought
import com.soonodekar.myapp1.thoughts.roomdatabase.ThoughtDatabase
import com.soonodekar.myapp1.thoughts.repo.ThoughtsRepo
import com.soonodekar.myapp1.thoughts.thoughtsService.ThoughtsService
import com.soonodekar.myapp1.thoughts.ui.ThoughtsFragment
import com.soonodekar.myapp1.thoughts.viewmodel.ThoughtsViewModel

class HomeFragment : Fragment() {
    private lateinit var binding : HomeFragmentBinding
    private lateinit var thoughtsAdaptor: PostAdaptor
    private lateinit var jokesAdaptor: PostAdaptor
    private lateinit var thoughtsViewModel: ThoughtsViewModel
    private lateinit var jokesViewModel: JokesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.home_fragment, null)
        binding = HomeFragmentBinding.bind(view)
        initViewModel()
        initObserver()
        setUpRecyclerViews()
        thoughtsViewModel.getTodaysThoughts()
        jokesViewModel.getTodaysJokes()

        setUpListeners(view)
        return view
    }


    private fun setUpListeners(view: View){
        view.setOnClickListener {}

        binding.jokesBtn.setOnClickListener {
            startSecondaryActivity(JokesFragment::class.java)
        }

        binding.relativeContainer.setOnClickListener {
            startSecondaryActivity(JokesFragment::class.java)
        }


        binding.relativeContainerThoughts.setOnClickListener {
            startSecondaryActivity(ThoughtsFragment::class.java)
        }

        binding.thoughtBtn.setOnClickListener {
            startSecondaryActivity(ThoughtsFragment::class.java)
        }

    }


    private fun shareThought(data: BaseData, todaysPostView: TodaysPostView){

        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, todaysPostView.binding.txtTodaysPostText.text) // Replace "Text to share" with the actual text you want to share

        if (shareIntent.resolveActivity(activity!!.packageManager) != null) {
            startActivity(Intent.createChooser(shareIntent, "Share via"))
        } else {
            // Handle case when no apps can handle the intent
            // For example, show an error message to the user
        }

    }

    private fun initObserver() {
        thoughtsViewModel.todaySThoughtsLiveData.observe(
            viewLifecycleOwner
        ) {thoughts->

            thoughtsAdaptor = PostAdaptor(thoughts)
            binding.todaySRecyclerThoughts1.adapter = thoughtsAdaptor
            binding.progressbarThoughts.visibility = View.GONE
            thoughtsAdaptor.onTodaySPostViewClickListeners =
                object : PostAdaptor.OnTodaySPostViewClickListeners{
                    override fun onShareBtnClick(position: Int, data: BaseData, view: TodaysPostView) {
                        shareThought(thoughts[position], view)
                    }
                }
        }


        jokesViewModel.todaySJokesLiveData.observe(
            viewLifecycleOwner
        ) {jokes->
            jokesAdaptor = PostAdaptor(jokes)
            binding.todaySRecyclerJokes.adapter = jokesAdaptor
            binding.progressbarJokes.visibility = View.GONE

            object : PostAdaptor.OnTodaySPostViewClickListeners{
                override fun onShareBtnClick(position: Int, data: BaseData, view: TodaysPostView) {
                    shareThought(jokes[position], view)
                }
            }
        }


        thoughtsViewModel.savedStatusLiveData.observe(
            viewLifecycleOwner
        ) {
            if (it) {
                MainActivity.mt("Saved", requireContext())
            } else {
                MainActivity.mt("Thought is AlreadySaved", requireContext())
            }
        }

    }

    private fun initViewModel() {

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

    private fun setUpRecyclerViews() {
        binding.todaySRecyclerThoughts1.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )

        binding.todaySRecyclerJokes.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )

    }

    private fun startSecondaryActivity(fragmentType: Class<*>){
        val intent = Intent(requireContext(), SecondaryActivity::class.java)
        intent.putExtra("fragment", fragmentType)
        startActivity(intent)
    }
}
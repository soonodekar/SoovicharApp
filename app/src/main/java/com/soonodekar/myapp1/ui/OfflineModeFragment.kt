package com.soonodekar.myapp1.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.soonodekar.myapp1.R
import com.soonodekar.myapp1.databinding.OfflineModeFragementBinding
import com.soonodekar.myapp1.thoughts.ui.SavedThoughtsFragment

class OfflineModeFragment : Fragment(){

    private lateinit var binding : OfflineModeFragementBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.offline_mode_fragement, null)
        binding = OfflineModeFragementBinding.bind(view)
        setUpListener()
        return view
    }

    private fun setUpListener(){
        binding.savedThoughtBtn.setOnClickListener {
            addFragment(SavedThoughtsFragment())
        }
    }

    private fun addFragment(fragment: Fragment){
        parentFragmentManager.beginTransaction()
            .add(R.id.secondaryContainer, fragment)
            .addToBackStack(fragment::class.java.name)
            .commit()
    }

}
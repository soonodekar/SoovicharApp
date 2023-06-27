package com.soonodekar.myapp1.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.soonodekar.myapp1.R
import com.soonodekar.myapp1.SecondaryActivity
import com.soonodekar.myapp1.databinding.MoreFragmentBinding
import com.soonodekar.myapp1.settings.SettingsFragment

class MoreFragment : Fragment() {

    private lateinit var binding: MoreFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.more_fragment, null)
        binding = MoreFragmentBinding.bind(view)
        setUpListeners(view)
        return view
    }

    private fun setUpListeners(view: View){


        binding.settingsTab.setOnClickListener {
            startSecondaryActivity(SettingsFragment::class.java)
        }

        binding.SavedTab.setOnClickListener {
            startSecondaryActivity(OfflineModeFragment::class.java)
        }

    }

    private fun startSecondaryActivity(fragmentType: Class<*>){
        val intent = Intent(requireContext(), SecondaryActivity::class.java)
        intent.putExtra("fragment", fragmentType)
        startActivity(intent)
    }


}
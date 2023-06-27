package com.soonodekar.myapp1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.soonodekar.myapp1.databinding.ActivitySecondaryBinding
import com.soonodekar.myapp1.jokes.ui.JokesFragment
import com.soonodekar.myapp1.ui.HomeFragment
import com.soonodekar.myapp1.settings.SettingsFragment
import com.soonodekar.myapp1.thoughts.ui.ThoughtsFragment
import com.soonodekar.myapp1.ui.OfflineModeFragment

class SecondaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondaryBinding
    private lateinit var classType : Class<*>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondaryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initFragment()
    }


    private fun initFragment(){

        classType = intent.getSerializableExtra("fragment") as Class<*>

        supportFragmentManager.beginTransaction()
            .add(
                R.id.secondaryContainer, when (classType) {

                    ThoughtsFragment::class.java -> ThoughtsFragment()
                    JokesFragment::class.java -> JokesFragment()
                    SettingsFragment::class.java -> SettingsFragment()
                    OfflineModeFragment::class.java -> OfflineModeFragment()

                    else -> {
                        HomeFragment()
                    }
                }
            )
            .commit()
    }

}


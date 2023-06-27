package com.soonodekar.myapp1.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.soonodekar.myapp1.MainActivity
import com.soonodekar.myapp1.R
import com.soonodekar.myapp1.databinding.SettingsFragmentBinding

class SettingsFragment : Fragment() {

    private lateinit var binding: SettingsFragmentBinding


    companion object {
        const val THOUGHTS_NOTIFICATION = "thoughts_notification"
        const val PROVERBS_NOTIFICATION = "proverb_notification"
        const val JOKES_NOTIFICATION = "jokes_notification"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.settings_fragment, null)
        binding = SettingsFragmentBinding.bind(view)
        bindData()
        setUpListeners()

        return view
    }

    private fun bindData() {
        binding.thoughtsNoteChooserNo.text =
            Settings
                .getNotificationsNumber(
                    requireContext(),
                    THOUGHTS_NOTIFICATION
                ).toString()

        binding.jokesNoteChooserNo.text =
            Settings
                .getNotificationsNumber(
                    requireContext(),
                    JOKES_NOTIFICATION
                ).toString()

        binding.proverbsNoteChooserNo.text =
            Settings
                .getNotificationsNumber(
                    requireContext(),
                    PROVERBS_NOTIFICATION
                ).toString()
    }



    private fun setUpListeners() {

        setUpOnSingsClick(
            binding.thoughtsNoteChooserNo,
            binding.thoughtsNoteChooserPlus,
            binding.thoughtsNoteChooserMinus,
        )

        setUpOnSingsClick(
            binding.proverbsNoteChooserNo,
            binding.proverbsNoteChooserPlus,
            binding.proverbsNoteChooserMinus
        )

        setUpOnSingsClick(
            binding.jokesNoteChooserNo,
            binding.jokesNoteChooserPlus,
            binding.jokesNoteChooserMinus
        )

        binding.btnSaveChanges.setOnClickListener {
            Settings.setNotificationsNumber(requireContext(), binding.proverbsNoteChooserNo.text.toString().toInt(), PROVERBS_NOTIFICATION)
            Settings.setNotificationsNumber(requireContext(), binding.thoughtsNoteChooserNo.text.toString().toInt(), THOUGHTS_NOTIFICATION)
            Settings.setNotificationsNumber(requireContext(), binding.jokesNoteChooserNo.text.toString().toInt(), JOKES_NOTIFICATION)

            parentFragmentManager.beginTransaction()
                .remove(this)
                .commit()
            requireActivity().finish()
        }



    }

    private fun setUpOnSingsClick(view: TextView, plusSing: TextView, minusSing: TextView){

        view.text = when(view){
            binding.thoughtsNoteChooserNo -> Settings.getNotificationsNumber(requireContext(), THOUGHTS_NOTIFICATION).toString()
            binding.jokesNoteChooserNo -> Settings.getNotificationsNumber(requireContext(), JOKES_NOTIFICATION).toString()
            binding.proverbsNoteChooserNo -> Settings.getNotificationsNumber(requireContext(), PROVERBS_NOTIFICATION).toString()
            else -> {
                "0"
            }
        }

        var count = view.text.toString().toInt()

        plusSing.setOnClickListener {
            if(view.text.toString().toInt() >= 3){
                MainActivity.mt("Maximum 3 Notification Allowed",requireContext())
            }else{
                count += 1
                view.text = count.toString()
            }
        }

        minusSing.setOnClickListener {
            if(view.text.toString().toInt() == 0){
                view.text = count.toString()
            }
            else{
                count -= 1
                view.text = count.toString()
            }
        }

    }

}
package com.soonodekar.myapp1

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.soonodekar.myapp1.databinding.ActivityMainBinding
import com.soonodekar.myapp1.ui.HomeFragment
import com.soonodekar.myapp1.ui.MoreFragment
import com.soonodekar.myapp1.provider.ViewModelFactory
import com.soonodekar.myapp1.recievers.ConnectionCheckReceiver
import com.soonodekar.myapp1.recievers.OnTimeReceiver
import com.soonodekar.myapp1.settings.Settings
import com.soonodekar.myapp1.settings.SettingsFragment
import com.soonodekar.myapp1.thoughts.modules.Thought
import com.soonodekar.myapp1.thoughts.roomdatabase.ThoughtDatabase
import com.soonodekar.myapp1.thoughts.repo.ThoughtsRepo
import com.soonodekar.myapp1.thoughts.thoughtsService.ThoughtsService
import com.soonodekar.myapp1.thoughts.viewmodel.ThoughtsViewModel
import com.soonodekar.myapp1.ui.OfflineModeFragment
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var onTimeReceiver: OnTimeReceiver
    private lateinit var connectionCheckReceiver: ConnectionCheckReceiver
    private lateinit var todaysThought: java.util.ArrayList<Thought>

    companion object {


        lateinit var thoughtsViewModel: ThoughtsViewModel
        const val CHANNEL_ID = "dailyNotification"
        const val CHANNEL_NAME = "Daily Notifications"
        const val NOTIFICATION_ID = 1

        fun mt(text : String, context: Context){
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeFragment())
        createNotificationChannel(this)
        scheduleNotification()
        handleDataChecks()
        initViewModel()
        initObserver()
        thoughtsViewModel.getTodaysThoughts()
        setUpListeners()
    }

    override fun onResume() {
        super.onResume()

        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(connectionCheckReceiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(connectionCheckReceiver)
    }


    private fun initObserver(){
        thoughtsViewModel.todaySThoughtsLiveData.observe(
            this
        ){
           todaysThought = it
        }



    }

    private fun initViewModel(){
        thoughtsViewModel =
            ViewModelProvider(
                this,
                ViewModelFactory(
                    ThoughtsRepo(
                        ThoughtsService,
                        ThoughtDatabase(this)
                    )
                )
            )[ThoughtsViewModel::class.java]
    }



    private fun handleDataChecks(){
       connectionCheckReceiver = ConnectionCheckReceiver()
        connectionCheckReceiver.onConnectivityChangeListener =
            object : ConnectionCheckReceiver.OnConnectivityChangeListener{
                override fun onConnectivityChanges(
                    isConnected: Boolean,
                    isInternetAvailable: Boolean
                ) {
                    if(!isInternetAvailable){
                        startSecondaryActivity(OfflineModeFragment::class.java)
                        finish()
                    }

                }

            }
    }

    private fun startSecondaryActivity(fragmentType: Class<*>){
        val intent = Intent(this, SecondaryActivity::class.java)
        intent.putExtra("fragment", fragmentType)
        startActivity(intent)
    }

    /*private fun isDataConnectionAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(network) ?: return false

        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }*/

    private fun setUpListeners(){
        binding.iconHome.setOnClickListener {
            changeIconUi(binding.iconHome)
            supportFragmentManager.popBackStack()
            supportFragmentManager.beginTransaction()
                .add(R.id.primaryContainer, HomeFragment())
                .commit()
            //supportFragmentManager.popBackStack()
        }

        binding.iconMore.setOnClickListener {
            changeIconUi(binding.iconMore)
            replaceFragment(MoreFragment())

        }
    }

    private fun changeIconUi(view: View){
        if(view == binding.iconHome){
            binding.iconHome.setImageResource(R.drawable.homefill)
            binding.iconMore.setImageResource(R.drawable.more)
        }

        if(view == binding.iconMore){
            binding.iconHome.setImageResource(R.drawable.home)
            binding.iconMore.setImageResource(R.drawable.morefill)
        }

    }

    private fun replaceFragment(fragment: Fragment){

            supportFragmentManager.beginTransaction()
                .replace(R.id.primaryContainer, fragment)
                .commit()
    }

    private fun createNotificationChannel(context: Context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun scheduleNotification() {

        // Set the desired time for the notification
        onTimeReceiver = OnTimeReceiver()

        onTimeReceiver.onTimeReceiverListener =
            object : OnTimeReceiver.OnTimeReceiverListener{
                @SuppressLint("MissingPermission")
                override fun onTimeReached(context: Context, intent: Intent) {
                    val bitmap = BitmapFactory.decodeResource(context.resources,
                        R.drawable.ic_launcher_background
                    )

                    for(i in 1 .. Settings.getNotificationsNumber(this@MainActivity, SettingsFragment.THOUGHTS_NOTIFICATION)) {
                        if(i-1 == 0)return
                        val notificationBuilder =
                            NotificationCompat.Builder(context, CHANNEL_ID)
                                .setSmallIcon(R.drawable.ic_launcher_background)
                                .setLargeIcon(bitmap)
                                .setContentTitle("Today's Thought")
                                .setContentText("${todaysThought[i-1].text}\nby- ${todaysThought[i-1].author}")
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                        val notificationManager = NotificationManagerCompat.from(context)

                        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
                    }
                }

            }

        val calendar: Calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 8) // Set the hour (24-hour format)
        calendar.set(Calendar.MINUTE, 0) // Set the minute
        calendar.set(Calendar.SECOND, 0) // Set the second

        // Create an intent to start the broadcast receiver

        val intent = Intent(this, OnTimeReceiver::class.java)

        val pendingIntent =
            PendingIntent.getBroadcast(this, 0, intent, 0)

        // Schedule the notification using AlarmManager

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

}
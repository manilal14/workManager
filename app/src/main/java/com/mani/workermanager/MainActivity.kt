package com.mani.workermanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.work.*
import com.mani.workermanager.CommonData.Companion.KEY_PAYLOAD
import com.mani.workermanager.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object{
        private const val TAG = "MainActivity"
    }

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressCircular.visibility = View.GONE
        clickListener()
    }

    private fun clickListener(){

        binding.btnCancelAllWorker.setOnClickListener {
            WorkManager.getInstance(this)
                .cancelAllWork()
            binding.progressCircular.visibility =View.GONE
            Toast.makeText(this,"All work is cancelled", Toast.LENGTH_SHORT).show()
        }

        binding.btnStartWorker.setOnClickListener {

            binding.tvMessage.text = "started...."
            binding.progressCircular.visibility =View.VISIBLE


            var constraints : Constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val inputData : Data = Data.Builder()
                .putString(KEY_PAYLOAD, "Data from Activity")
                .build()

            val workRequest : WorkRequest = OneTimeWorkRequestBuilder<MyWorker>()
                .setConstraints(constraints)
                .setInputData(inputData)
                .build()

//            val periodicWorkRequest : WorkRequest = PeriodicWorkRequestBuilder<MyWorker>(15, TimeUnit.MINUTES)
//                .build()

            WorkManager
                .getInstance(this)
                .enqueue(workRequest)

            
            //Getting data from MyWorker ( from background thread to main thread )
            WorkManager.getInstance(this)
                .getWorkInfoByIdLiveData(workRequest.id)
                .observe(this, Observer {
                    Log.i(TAG, "state = "+it.state)

                    when (it.state) {
                        WorkInfo.State.SUCCEEDED -> {
                            val res = it.outputData.getString(KEY_PAYLOAD)
                            Log.i(TAG, "Result = $res")
                            binding.tvMessage.text = res
                            binding.progressCircular.visibility =View.GONE
                        }
                        WorkInfo.State.CANCELLED -> Log.e(TAG,"Work is cancelled, workRequest id = ${it.id}")
                        else -> Log.e(TAG,"No data from MyWorker class")

                    }
                })

        }

        binding.btnNotify.setOnClickListener {
            CommonData.displayNotification(this,"title", "content of the notification will be here")
        }
    }




}
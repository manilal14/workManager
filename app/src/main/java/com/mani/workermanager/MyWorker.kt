package com.mani.workermanager

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.mani.workermanager.CommonData.Companion.KEY_PAYLOAD

class MyWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    companion object{
        private const val TAG = "MyWorker"
    }

    //runs on background(worker) thread
    override fun doWork(): Result {
        Log.i(TAG, "doWork() is called")
        Log.i(TAG, "Current thread = " +Thread.currentThread().name + ", id = "+Thread.currentThread().id)

        //passing data to activity
        var data = Data.Builder()
            .putString(KEY_PAYLOAD, "payload from Worker class")
            .build()

        //Receiving data from activity
        val inputData :String? = inputData.getString(KEY_PAYLOAD)
        if (inputData!=null) Log.i(TAG, inputData)

        Thread.sleep(6000)

        return Result.success(data)
    }

    override fun onStopped() {
        super.onStopped()
        Log.e(TAG, "onStopped is called")
    }

}
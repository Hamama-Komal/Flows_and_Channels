package com.example.channelsflows

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val channel = Channel<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Produce data for Channel
        producer()

        // Consume the data produce by Channel
        consumer()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun consumer() {

        GlobalScope.launch {

            val result1 = channel.receive()
            val result2 = channel.receive()
            val result3 = channel.receive()

            Log.d("CHANNEL_RESULT", result1.toString())
            Log.d("CHANNEL_RESULT", result2.toString())        // delay don't effect the values
            Log.d("CHANNEL_RESULT", result3.toString())

        }





    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun producer() {

        GlobalScope.launch(Dispatchers.Main) {
            channel.send(1)
            delay(2000)
            channel.send(50)
            delay(4000)
            channel.send(100)
        }


    }
}
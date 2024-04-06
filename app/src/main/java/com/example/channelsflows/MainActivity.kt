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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {


    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Context Switching

        //  flowOn()
        // Consumer
        GlobalScope.launch(Dispatchers.Main) {  // consumer => Main Thread
            producer()
                .flowOn(Dispatchers.Main)      // producer => Main Thread
                .map {
                    delay(2000)
                    Log.d("FLOW_RESULT", "Map Thread - ${Thread.currentThread().name}")
                    it * 2 + 2
                }
                .filter {
                    delay(1000)
                    Log.d("FLOW_RESULT", "Filter Thread - ${Thread.currentThread().name}")
                    it <= 10
                }
                .flowOn(Dispatchers.IO)               // filter & map => IO Thread
                .collect {
                    Log.d("FLOW_RESULT", "Collector Thread - ${Thread.currentThread().name}")
                }
        }


    }

    // Producer
    private fun producer() = flow {

        val list = listOf(1, 2, 3, 4, 5)
        list.forEach {
            delay(1000)
            Log.d("FLOW_RESULT", "Emitter Thread - ${Thread.currentThread().name}")
            emit(it)
        }

    }


}
package com.example.channelsflows

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

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

        // Flow Events

        //  onStart, onEach, onCompletion
        // Consumer 1
        GlobalScope.launch {
            producer()
                .onStart {
                    emit(0)
                    Log.d("FLOW_RESULT_1", "Stream Start")

                }
                .onEach {
                    Log.d("FLOW_RESULT_1", "About to emit $it")
                }
                .onCompletion {
                    emit(6)
                    Log.d("FLOW_RESULT_1", "Stream Ends")
                }
                .collect {
                    Log.d("FLOW_RESULT_1", it.toString())
                }
        }

        // buffer
        // Consumer 2
        GlobalScope.launch {

            val time = measureTimeMillis {
                producer().buffer(2).collect {
                    delay(2000)
                    Log.d("FLOW_RESULT_2", it.toString())
                }
            }
            Log.d("FLOW_RESULT_2", "Total time consume $time")
        }


        // map & filter
        //  Consumer 3
        GlobalScope.launch {
            producer()
                .map {
                    it * 2 + 2
                }
                .filter {
                    it <= 10
                }
                .collect {
                    Log.d("FLOW_RESULT_3", it.toString())
                }
        }


    }

    // Producer
    private fun producer() = flow {

        val list = listOf(1, 2, 3, 4, 5)
        list.forEach {
            delay(1000)
            emit(it)
        }

    }


}
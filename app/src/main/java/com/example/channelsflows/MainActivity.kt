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
import kotlinx.coroutines.flow.flow
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

        // Consumer 1
        GlobalScope.launch {
            val data = producer()
            data.collect{
                Log.d("FLOW_RESULT_1",it.toString())
            }
        }

        // Consumer 2
        GlobalScope.launch {
            val data = producer()
            data.collect{
                Log.d("FLOW_RESULT_2",it.toString())
            }
        }

        // Consumer 3
        GlobalScope.launch {
            val data = producer()
            delay(3000)
            data.collect{
                Log.d("FLOW_RESULT_3",it.toString())
            }
        }


    }

    // Producer
    private fun producer() = flow {

        val list = listOf(1,2,3,4,5)
        list.forEach {
            delay(1000)
            emit(it)
        }

    }


}
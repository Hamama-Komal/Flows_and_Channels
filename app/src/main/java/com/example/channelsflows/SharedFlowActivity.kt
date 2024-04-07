@file:OptIn(DelicateCoroutinesApi::class)

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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class SharedFlowActivity : AppCompatActivity() {


    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_shared_flow)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Shared Flow   (Hot Flow)


        // Consumer 1
        GlobalScope.launch(Dispatchers.Main) {
            val result = sharedFlowProducer()
            result.collect{
                Log.d("FLOW_RESULT1 -", it.toString())
            }
        }

         // Consumer 2
        GlobalScope.launch(Dispatchers.Main) {
            val result = sharedFlowProducer()
            delay(3000)
            result.collect{
                Log.d("FLOW_RESULT2 -", it.toString())
            }
        }




    }


    private fun sharedFlowProducer() : Flow<Int> {

        val mutableSharedFlow = MutableSharedFlow<Int>(2)        // replay

        GlobalScope.launch {

            val list = listOf(1, 2, 3, 4, 5)
            list.forEach {
                delay(1000)
                mutableSharedFlow.emit(it)
            }
        }

        return mutableSharedFlow
    }
}
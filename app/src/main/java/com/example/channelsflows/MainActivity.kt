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
import kotlinx.coroutines.flow.catch
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

        // Error Handling


        // Consumer
        GlobalScope.launch(Dispatchers.Main) {
            // This block catch the error of both Consumer and Producer
            try {
                producer()
                    .collect {
                        Log.d("FLOW_RESULT", "Collector Thread - ${Thread.currentThread().name}")
                        Log.d("FLOW_RESULT", "Collector Values - $it")
                    }
            }
            catch (e: Exception){
                Log.d("FLOW_RESULT", e.message.toString())
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
            throw Exception ("Emitter Error! Something went wrong")
        }

    }
        // To separately catch the error in Producer
        .catch {
            Log.d("FLOW_RESULT", "Emitter Catch - ${it.message}")
            emit(-1)
        }


}
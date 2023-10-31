package com.example.namegenerator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    private lateinit var femaleBtn : Button
    private lateinit var maleBtn : Button
    private lateinit var childNameView : TextView

    private var namePopularityList = ArrayList<BabyNamePopularity>()

    private val callBackInfo : RequestHandler.OnFinishRequest = createNetWorkRequestCallbackActions()
    private val requestHandler = RequestHandler(callBackInfo)








    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        onFinishinflate()
        getDataRequest()

        namePopularityList.sortBy { it.numTotal }
    }

    private fun onFinishinflate(){
        femaleBtn = findViewById(R.id.female_btn)
        maleBtn = findViewById(R.id.male_btn)
        childNameView = findViewById(R.id.child_name_view)
    }

    private fun getDataRequest() {

        val networkRequest = NetworkRequest(applicationContext)
        networkRequest.getRequest( getString(R.string.api_get_all_url), requestHandler)


    }

    private fun createNetWorkRequestCallbackActions() : RequestHandler.OnFinishRequest {
        return object : RequestHandler.OnFinishRequest {
            override fun onFinishRequest(result: JSONObject) {
                Log.i("data ", result.toString())
                Toast.makeText(applicationContext,"dsdsdsds" , Toast.LENGTH_SHORT).show()
            }

        }
    }
}
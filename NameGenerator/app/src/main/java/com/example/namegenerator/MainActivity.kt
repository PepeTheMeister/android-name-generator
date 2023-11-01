package com.example.namegenerator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.gson.Gson
import org.json.JSONObject
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    private lateinit var femaleBtn: Button
    private lateinit var maleBtn: Button
    private lateinit var childNameView: TextView

    private var maleNamePopularityList = ArrayList<BabyNamePopularity>()
    private var femaleNamePopularityList = ArrayList<BabyNamePopularity>()

    private val callBackInfo: RequestHandler.OnFinishRequest = createNetWorkRequestCallbackActions()
    private val requestHandler = RequestHandler(callBackInfo)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        onFinishInflate()
        getDataRequest()

        listeners()
    }

    private fun listeners() {
        femaleBtn.setOnClickListener {
            val random = Random.nextInt(0, 6)
            childNameView.text = femaleNamePopularityList[random].name
        }

        maleBtn.setOnClickListener {
            val random = Random.nextInt(0, 6)
            childNameView.text = maleNamePopularityList[random].name
        }
    }

    private fun onFinishInflate() {
        femaleBtn = findViewById(R.id.female_btn)
        maleBtn = findViewById(R.id.male_btn)
        childNameView = findViewById(R.id.child_name_view)
    }

    private fun getDataRequest() {
        val networkRequest = NetworkRequest(applicationContext)
        networkRequest.getRequest(getString(R.string.api_get_all_url), requestHandler)
    }

    private fun createNetWorkRequestCallbackActions(): RequestHandler.OnFinishRequest {
        return object : RequestHandler.OnFinishRequest {
            override fun onFinishRequest(result: JSONObject) {
                Log.i("data ", result.toString())
                val dataBodyJson = result.getString("body")
                val gson = Gson()
                val dataBodyList =
                    gson.fromJson<ArrayList<ArrayList<String>>>(dataBodyJson, ArrayList::class.java)

                for (value in dataBodyList) {
                    val babyNamePopularity = BabyNamePopularity(
                        value[0],
                        value[1],
                        value[2],
                        value[3],
                        value[4],
                        value[5]
                    )

                    if ("FEMALE" == babyNamePopularity.gender) {
                        femaleNamePopularityList.add(babyNamePopularity)
                    } else {
                        maleNamePopularityList.add(babyNamePopularity)
                    }
                }

                maleNamePopularityList.sortByDescending { it.numTotal }
                femaleNamePopularityList.sortByDescending { it.numTotal }
            }

        }
    }
}
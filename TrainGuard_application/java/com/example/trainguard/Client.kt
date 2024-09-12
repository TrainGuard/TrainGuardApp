package com.example.trainguard

import android.location.Location
import java.net.URL

import org.json.JSONObject

class Client {
    companion object {
        var personLocationLatitude = 0.0
        var personLocationLongtitude = 0.0

        var current_train_id = 0

        var trains: Array<Train>? = null

        fun getRangeFromServer(): Float {
            getArrayOfTrains()
            var res = floatArrayOf(.0f)
            var outp = Float.MAX_VALUE
            for (tr in trains!!) {
                Location.distanceBetween(personLocationLatitude, personLocationLongtitude, tr.locationLatitude, tr.locationLongtitude, res)
                if (res[0] < outp) {
                    outp = res[0]
                }
            }

            return outp
        }

        fun updateTrainInfo() {
            URL("http://188.234.24.236:12345/update_train?id=${current_train_id}&lat=${personLocationLatitude}&long=${personLocationLongtitude}").readText()
        }

        fun addTrain(name: String) {
            var tex = URL("http://188.234.24.236:12345/add_train?name=${name}&lat=${personLocationLatitude}&long=${personLocationLongtitude}").readText()
            val userJsonObject = JSONObject(tex)
            current_train_id = userJsonObject.getInt("id")
        }

        fun getArrayOfTrains(): Array<Train> {
            //Debug.printStack(URL("http://192.168.31.138:8000/get_trains").readText(), 1)

            var counts: Int = URL("http://188.234.24.236:12345/get_trains_count").readText().trimIndent().toInt()

            val trains_list = arrayListOf<Train>()

            for (i in 0..counts-1) {
                var tex = URL("http://188.234.24.236:12345/get_trains?id=${i}").readText().trimIndent()
                val userJsonObject = JSONObject(tex)
                trains_list.add(Train(i, userJsonObject.getString("name"), userJsonObject.getDouble("lat"), userJsonObject.getDouble("long")))
            }

            trains = trains_list.toTypedArray()

            return trains as Array<Train>
        }
    }
}
package com.yitinglin.kotlinjetpackdemo.model

import com.google.gson.annotations.SerializedName

//高雄(或單一縣市)氣象
data class WeatherInfo(
        @SerializedName("records")
        val records: Records = Records(),
        @SerializedName("result")
        val result: Result = Result(),
        @SerializedName("success")
        val success: String = "" // true
) {
    data class Records(
            @SerializedName("locations")
            val locations: List<Locations> = listOf()
    )
    {
        data class Locations(
                @SerializedName("datasetDescription")
                val datasetDescription: String = "", //  "臺灣各縣市鄉鎮未來3天(72小時)逐3小時天氣預報",
                @SerializedName("locationsName")
                val locationsName: String = "", //  "高雄市",
                @SerializedName("location")
                val location: List<Location> = listOf()
        ){
            data class Location(
                @SerializedName("locationName")
                val locationName: String = "", // 新興區
                @SerializedName("weatherElement")
                val weatherElement: List<WeatherElement> = listOf()
            )
            {
                data class WeatherElement(
                    @SerializedName("elementName")
                    val elementName: String = "", // PoP12h
                    @SerializedName("description")
                    val description: String = "", // 12小時降雨機率
                    @SerializedName("time")
                    val time: List<Time> = listOf()
                )
                {
                    data class Time(
                            @SerializedName("startTime")
                            val startTime: String = "", // 2020-08-07 12:00:00
                            @SerializedName("endTime")
                            val endTime: String = "", // 2020-08-07 18:00:00
                            @SerializedName("elementValue")
                            val elementValue:  List<ElementValue> = listOf()
                    ){
                        data class ElementValue(
                                @SerializedName("value")
                                val value: String = "", //0
                                @SerializedName("measures")
                                val measures: String = ""//百分比
                        )
                    }
                }
            }
        }
    }

    data class Result(
            @SerializedName("fields")
            val fields: List<Field> = listOf(),
            @SerializedName("resource_id")
            val resourceId: String = "" // F-C0032-001
    ) {
        data class Field(
                @SerializedName("id")
                val id: String = "", // datasetDescription
                @SerializedName("type")
                val type: String = "" // String
        )
    }
}

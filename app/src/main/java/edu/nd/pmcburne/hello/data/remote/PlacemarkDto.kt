package edu.nd.pmcburne.hello.data.remote

import com.google.gson.annotations.SerializedName

data class PlacemarkDto(
    val id: Int,
    val name: String,
    val description: String,
    @SerializedName("tag_list") val tagList: List<String>,
    @SerializedName("visual_center") val visualCenter: VisualCenterDto
)

data class VisualCenterDto(
    val latitude: Double,
    val longitude: Double
)
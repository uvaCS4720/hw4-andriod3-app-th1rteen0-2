package edu.nd.pmcburne.hello.data

import edu.nd.pmcburne.hello.data.local.PlacemarkDao
import edu.nd.pmcburne.hello.data.local.PlacemarkEntity
import edu.nd.pmcburne.hello.data.remote.PlacemarkApiService

class PlacemarkRepository(
    private val dao: PlacemarkDao,
    private val apiService: PlacemarkApiService
) {
    suspend fun syncFromApi() {
        try {
            val dtos = apiService.getPlacemarks()
            for (dto in dtos) {
                val entity = PlacemarkEntity(
                    id = dto.id,
                    name = dto.name,
                    description = dto.description,
                    tagList = dto.tagList.joinToString(","),
                    latitude = dto.visualCenter.latitude,
                    longitude = dto.visualCenter.longitude
                )
                dao.insertIgnore(entity)
            }
        } catch (e: Exception) {
            // network unavailable -- app will use whatever is already in the DB
            e.printStackTrace()
        }
    }
    suspend fun getAllTags(): List<String> {
        return dao.getAll()
            .flatMap { it.tagList.split(",") }
            .map { it.trim() }
            .distinct()
            .sorted()
    }

    suspend fun getPlacemarksByTag(tag: String): List<PlacemarkEntity> {
        return dao.getAll().filter { placemark ->
            placemark.tagList.split(",").map { it.trim() }.contains(tag)
        }
    }
}
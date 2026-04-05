package edu.nd.pmcburne.hello.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import edu.nd.pmcburne.hello.data.PlacemarkRepository
import edu.nd.pmcburne.hello.data.local.AppDatabase
import edu.nd.pmcburne.hello.data.local.PlacemarkEntity
import edu.nd.pmcburne.hello.data.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MapUiState(
    val allTags: List<String> = emptyList(),
    val selectedTag: String = "core",
    val filteredPlacemarks: List<PlacemarkEntity> = emptyList(),
    val isLoading: Boolean = true
)

class MapViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    private val repository: PlacemarkRepository

    init {
        val db = AppDatabase.getDatabase(application)
        repository = PlacemarkRepository(db.placemarkDao(), RetrofitInstance.create())

        viewModelScope.launch {
            // fetch from API and insert into Room (duplicates silently ignored)
            repository.syncFromApi()

            // load dropdown options and default "core" markers
            val tags = repository.getAllTags()
            val placemarks = repository.getPlacemarksByTag("core")

            _uiState.update {
                it.copy(
                    allTags = tags,
                    selectedTag = "core",
                    filteredPlacemarks = placemarks,
                    isLoading = false
                )
            }
        }
    }

    fun onTagSelected(tag: String) {
        viewModelScope.launch {
            val placemarks = repository.getPlacemarksByTag(tag)
            _uiState.update {
                it.copy(selectedTag = tag, filteredPlacemarks = placemarks)
            }
        }
    }
}
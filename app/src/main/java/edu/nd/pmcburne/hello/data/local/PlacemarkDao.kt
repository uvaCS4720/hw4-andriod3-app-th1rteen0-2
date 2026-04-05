package edu.nd.pmcburne.hello.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PlacemarkDao {

    // IGNORE means: if this ID already exists in the DB, skip it —- no duplicates ever inserted
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIgnore(placemark: PlacemarkEntity)

    @Query("SELECT * FROM placemarks")
    suspend fun getAll(): List<PlacemarkEntity>
}
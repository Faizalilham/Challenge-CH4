package com.example.challengeroom.room

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import com.example.challengeroom.model.Note

@Dao
interface Dao {

    @Query("SELECT * FROM Note")
    fun readData(): LiveData<MutableList<Note>>

    @Query("SELECT * FROM Note WHERE id=:id")
    suspend fun readDataById(id : Int):MutableList<Note>

    @Insert
    suspend fun createData(note: Note)

    @Update
   suspend fun updateData(note : Note)

    @Delete
    suspend fun deleteData(note: Note)


}
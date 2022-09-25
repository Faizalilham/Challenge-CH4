package com.example.challengeroom.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.challengeroom.model.Note

@Database(
    entities = [Note::class],
    version = 1
)
abstract class SetupRoom  : RoomDatabase(){
    abstract fun dao() : Dao

    companion object {

        @Volatile private var instance : SetupRoom? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
             SetupRoom::class.java,
            "Prediction"
        ).build()

    }
}
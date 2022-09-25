package com.example.challengeroom.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity
@Parcelize
data class Note (
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    val  tittle : String,
    val description : String
    ):Parcelable
package com.example.travelapp.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val departure: String,
    val departureDate: String,
    val departureTime: String,
    val destination: String,
    val arrivalDate: String,
    val arrivalTime: String,
    val tripType: String
):Parcelable

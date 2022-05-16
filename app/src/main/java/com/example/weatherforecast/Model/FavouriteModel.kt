package com.example.weatherforecast.Model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FavouriteModel")

data class FavouriteModel(
                            @ColumnInfo(name = "latitude")
                            var latitude:Double,
                            @ColumnInfo(name = "longitude")
                           var longitude:Double,
                            @ColumnInfo(name = "addressAr")
                           var addressAr:String,
                            @PrimaryKey
                            @ColumnInfo(name = "addressEn")
                           var addressEn:String): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
        parcel.writeString(addressAr)
        parcel.writeString(addressEn)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FavouriteModel> {
        override fun createFromParcel(parcel: Parcel): FavouriteModel {
            return FavouriteModel(parcel)
        }

        override fun newArray(size: Int): Array<FavouriteModel?> {
            return arrayOfNulls(size)
        }
    }
}
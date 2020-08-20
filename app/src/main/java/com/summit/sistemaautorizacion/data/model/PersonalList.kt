package com.summit.sistemaautorizacion.data.model

import android.os.Parcelable
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PersonalList(
    var name:String,
    var lastname:String,
    var dni:String,
    var phone:String?="",
    val role:String="",
    var password:String?="",
    var image:String?="",
    var createdBy:String?="",
    var _id:String=""
):Parcelable
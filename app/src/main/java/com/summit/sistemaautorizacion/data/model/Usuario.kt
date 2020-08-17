package com.summit.sistemaautorizacion.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.summit.sistemaautorizacion.common.Constants

@Entity(tableName = Constants.NAME_TABLE_USER)
data class Usuario(
    var name:String,
    var lastname:String,
    var dni:String,
    var phone:String?="",
    val role:String="",
    var password:String?="",
    var image:String?="",
    var createdBy:String?="",
    @PrimaryKey(autoGenerate = false)
    var _id:String=""
)
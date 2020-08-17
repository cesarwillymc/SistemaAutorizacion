package com.summit.sistemaautorizacion.data.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.summit.sistemaautorizacion.common.Constants
import com.summit.sistemaautorizacion.data.model.Usuario

@Dao
interface UsuarioDao {
    @Insert
    fun insertProfile(profile: Usuario)
    @Update
    fun updateProfile(profile: Usuario)
    @Query("DELETE FROM ${Constants.NAME_TABLE_USER}")
    fun deleteProfile()
    @Query("SELECT * FROM ${Constants.NAME_TABLE_USER}" )
    fun selectProfile(): LiveData<Usuario>

}
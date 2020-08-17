package com.summit.sistemaautorizacion.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.summit.sistemaautorizacion.common.Constants
import com.summit.sistemaautorizacion.data.model.Usuario

@Database(entities = [ Usuario::class],version = 2)
abstract class AppDB:RoomDatabase() {
    abstract val profileDao: UsuarioDao
    companion object{
        @Volatile
        private var INSTANCE:AppDB?=null
        private val LOCK= Any()
        operator fun invoke(context: Context)= INSTANCE?: synchronized(LOCK){
            INSTANCE?:buildDatabase(context)
        }
        private fun buildDatabase(context: Context)= Room.databaseBuilder(context,AppDB::class.java, Constants.NAME_DATABASE)
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()


    }
}
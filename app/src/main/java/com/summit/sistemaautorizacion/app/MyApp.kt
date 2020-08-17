package com.summit.sistemaautorizacion.app

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.summit.sistemaautorizacion.data.repository.RepositoryAuth
import com.summit.sistemaautorizacion.data.repository.RepositoryGlobal
import com.summit.sistemaautorizacion.data.retrofit.ApiRetrofit
import com.summit.sistemaautorizacion.data.room.AppDB
import com.summit.sistemaautorizacion.ui.auth.AuthViewModelFactory
import com.summit.sistemaautorizacion.ui.init.GlobalViewModel
import com.summit.sistemaautorizacion.ui.init.GlobalViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import org.kodein.di.generic.instance

class MyApp : Application(), LifecycleObserver, KodeinAware{
    override val kodein= Kodein.lazy {
        import(androidXModule(this@MyApp) )

        bind() from provider { RepositoryAuth(instance(),instance()) }
        bind() from provider { RepositoryGlobal(instance(),instance()) }
        bind() from provider { GlobalViewModelFactory(instance(),instance()) }
        bind() from provider { AuthViewModelFactory(instance()) }
        bind() from singleton { AppDB(instance()) }
        bind() from singleton { ApiRetrofit() }

    }
    companion object{
        private lateinit var instance:MyApp
        fun getInstanceApp():MyApp = instance
        fun getContextApp(): Context =instance
        fun setInstance(instance:MyApp){
            this.instance=instance
        }

    }
    override fun onCreate() {
        super.onCreate()
        setInstance(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() { //App in background

        Log.e("LifecycleObserver", "segundoPlano")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {

        Log.e("LifecycleObserver", "primerPLANO")
    }

}
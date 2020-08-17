package com.summit.sistemaautorizacion.ui.init

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.summit.sistemaautorizacion.data.repository.RepositoryAuth
import com.summit.sistemaautorizacion.data.repository.RepositoryGlobal

class GlobalViewModelFactory(private val auth:RepositoryAuth,private val repo:RepositoryGlobal) : ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return  GlobalViewModel(auth,repo) as T
    }

}
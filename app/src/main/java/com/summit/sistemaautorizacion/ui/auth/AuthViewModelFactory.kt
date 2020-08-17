package com.summit.sistemaautorizacion.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.summit.sistemaautorizacion.data.repository.RepositoryAuth

class AuthViewModelFactory(private val repo: RepositoryAuth): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AuthViewModel(repo) as T
    }

}